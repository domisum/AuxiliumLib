package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.annotations.API;
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
	private static final Duration TICK_INTERVAL = Duration.ofMillis(100);

	// TASKS
	private final List<IntervalTask> tasks = new ArrayList<>();


	// INIT
	public IntervalTaskTicker(String threadName)
	{
		super(TICK_INTERVAL, threadName);
	}

	@API public void addTask(Runnable task, Duration interval)
	{
		IntervalTask intervalTask = new IntervalTask(task, interval);
		tasks.add(intervalTask);
	}


	// TICK
	@Override protected final void tick()
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
				logger.error("error occured during execution of task {}", task.getClass().getName(), e);
			}
		}
	}


	// TASK
	@RequiredArgsConstructor
	private static class IntervalTask
	{

		@NonNull private Runnable task;
		@NonNull private Duration interval;

		private Instant lastExecution = Instant.MIN;


		protected boolean shouldRunNow()
		{
			Duration sinceLastExcecution = Duration.between(lastExecution, Instant.now());
			return sinceLastExcecution.compareTo(interval) >= 0;
		}

		protected void run()
		{
			task.run();
			lastExecution = Instant.now();
		}

	}

}
