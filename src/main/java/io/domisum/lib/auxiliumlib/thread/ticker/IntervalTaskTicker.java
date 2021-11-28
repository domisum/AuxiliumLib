package io.domisum.lib.auxiliumlib.thread.ticker;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.util.Compare;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@API
public class IntervalTaskTicker
	extends Ticker
{
	
	// CONSTANTS
	private static final Duration TICK_INTERVAL = Duration.ofMillis(10);
	private static final Duration TASK_EXCEPTION_COOLDOWN = Duration.ofSeconds(30);
	
	// TASKS
	private final Set<IntervalTask> tasks = new HashSet<>();
	private boolean tasksLocked = false;
	
	
	// INIT
	@API
	public IntervalTaskTicker(String threadName, Duration timeout)
	{
		super(threadName, TICK_INTERVAL, timeout, false);
	}
	
	@API
	public synchronized void addTask(String taskName, Runnable task, Duration timeout, Duration interval)
	{
		if(tasksLocked)
			throw new IllegalStateException("Can't add tasks after first start");
		
		var intervalTask = new IntervalTask(taskName, task, timeout, interval);
		tasks.add(intervalTask);
	}
	
	@API
	public synchronized void addTask(String taskName, Runnable task, Duration interval)
	{
		addTask(taskName, task, null, interval);
	}
	
	@API
	public synchronized void randomizeRunDelay()
	{
		for(var task : tasks)
			task.randomizeRunDelay();
	}
	
	@API
	@Override
	public synchronized void start()
	{
		tasksLocked = true;
		super.start();
	}
	
	
	// TICK
	@Override
	protected void tick()
	{
		for(var task : tasks)
		{
			if(Thread.currentThread().isInterrupted())
				return;
			if(Ticker.shouldStop())
				return;
			
			if(task.shouldRunNow())
				task.run();
		}
	}
	
	@Override
	protected void watchdogTick(Consumer<String> timeoutWithReason)
	{
		for(var task : tasks)
			task.watchdogTick(timeoutWithReason);
	}
	
	
	// TASK
	@RequiredArgsConstructor
	private class IntervalTask
	{
		
		// BASE ATTRIBUTES
		private final String name;
		private final Runnable task;
		@Nullable
		private final Duration timeout;
		private final Duration interval;
		
		// STATE
		private volatile Instant runStart = null;
		private volatile Instant runEnd = null;
		private Duration nextRunDelay = Duration.ZERO;
		
		
		public void randomizeRunDelay()
		{
			nextRunDelay = RandomUtil.getFromRange(Duration.ZERO, interval);
			runEnd = Instant.now().minus(interval.minus(nextRunDelay));
		}
		
		public boolean shouldRunNow()
		{
			if(runEnd == null)
				return true;
			
			return TimeUtil.hasPassed(runEnd.plus(nextRunDelay));
		}
		
		public void watchdogTick(Consumer<String> timeoutWithReason)
		{
			if(timeout == null)
				return;
			
			var runStart = this.runStart;
			var runEnd = this.runEnd;
			if(runStart == null)
				return;
			if(runEnd != null && Compare.greaterThan(runEnd, runStart))
				return;
			
			var runDuration = TimeUtil.since(runStart);
			if(Compare.greaterThan(runDuration, timeout))
				timeoutWithReason.accept(PHR.r("Task '{}' timed out; task timeout: {}", name, DurationDisplay.of(timeout)));
		}
		
		
		public void run()
		{
			try
			{
				runStart = Instant.now();
				task.run();
				nextRunDelay = interval;
			}
			catch(RuntimeException e)
			{
				logger.error("An exception occured during execution of task {}", name, e);
				nextRunDelay = ObjectUtils.max(interval, TASK_EXCEPTION_COOLDOWN);
			}
			finally
			{
				runEnd = Instant.now();
			}
		}
		
	}
	
}
