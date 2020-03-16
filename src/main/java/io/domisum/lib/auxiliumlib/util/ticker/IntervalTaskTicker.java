package io.domisum.lib.auxiliumlib.util.ticker;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@API
public class IntervalTaskTicker extends Ticker
{

	// CONSTANTS
	private static final Duration TICK_INTERVAL = Duration.ofMillis(10);

	// TASKS
	private final List<IntervalTask> tasks = new ArrayList<>();


	// INIT
	public IntervalTaskTicker(String threadName, Duration timeout)
	{
		super(threadName, TICK_INTERVAL, timeout);
	}

	@API
	public IntervalTaskTicker(String threadName, Duration timeout, Runnable task, Duration interval)
	{
		this(threadName, timeout);
		addTask(threadName+"-task", task, interval);
	}

	@API
	public void addTask(String taskName, Runnable task, Duration interval)
	{
		IntervalTask intervalTask = new IntervalTask(taskName, task, interval);
		tasks.add(intervalTask);
	}


	// TICK
	@Override
	protected void tick()
	{
		for(IntervalTask task : tasks)
		{
			if(Thread.currentThread().isInterrupted())
				return;

			if(!task.shouldRunNow())
				continue;

			try
			{
				task.run();
			}
			catch(RuntimeException e)
			{
				logger.error("error occured during execution of task {}", task.taskName, e);
			}
		}
	}


	// TASK
	@RequiredArgsConstructor
	private static class IntervalTask
	{

		private final @NonNull String taskName;
		private final @NonNull Runnable task;
		private final @NonNull Duration interval;

		private Instant lastExecution = Instant.MIN;


		protected boolean shouldRunNow()
		{
			Duration sinceLastExcecution = Duration.between(lastExecution, Instant.now());
			return sinceLastExcecution.compareTo(interval) >= 0;
		}

		protected void run()
		{
			lastExecution = Instant.now();
			task.run();
		}

	}

}
