package io.domisum.lib.auxiliumlib.thread.ticker;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@API
public class IntervalTaskTicker
	extends Ticker
{
	
	// CONSTANTS
	private static final Duration TICK_INTERVAL = Duration.ofMillis(10);
	private static final Duration TASK_ERROR_COOLDOWN = Duration.ofSeconds(10);
	
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
	public synchronized void addTask(String taskName, Runnable task, Duration interval)
	{
		if(tasksLocked)
			throw new IllegalStateException("Can't add tasks after first start");
		
		var intervalTask = new IntervalTask(taskName, task, interval);
		tasks.add(intervalTask);
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
	protected void tick(Supplier<Boolean> shouldStop)
	{
		for(var task : tasks)
		{
			if(Thread.currentThread().isInterrupted())
				return;
			if(shouldStop.get())
				return;
			
			if(task.shouldRunNow())
				task.run();
		}
	}
	
	
	// TASK
	@RequiredArgsConstructor
	private class IntervalTask
	{
		
		// BASE ATTRIBUTES
		private final String name;
		private final Runnable task;
		private final Duration interval;
		
		// STATUS
		private Instant nextExecution = Instant.MIN;
		
		
		// GETTERS
		protected boolean shouldRunNow()
		{
			return TimeUtil.hasPassed(nextExecution);
		}
		
		
		// RUN
		protected void run()
		{
			try
			{
				task.run();
				nextExecution = Instant.now().plus(interval);
			}
			catch(RuntimeException e)
			{
				logger.error("error occured during execution of task {}", name, e);
				nextExecution = Instant.now().plus(TASK_ERROR_COOLDOWN);
			}
		}
		
	}
	
}