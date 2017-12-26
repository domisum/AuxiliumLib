package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@API
public class IntervalTaskTicker extends Ticker
{

	// CONSTANTS
	private static final Duration TICK_INTERVAL = Duration.ofMillis(100);

	// TASKS
	private final Set<IntervalTask> tasks = new HashSet<>();


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
		for(IntervalTask t : tasks)
		{
			if(Thread.currentThread().isInterrupted())
				return;

			if(t.shouldRunNow())
				t.run();
		}
	}


	// TASK
	@RequiredArgsConstructor
	private class IntervalTask
	{

		@NonNull private Runnable task;
		@NonNull private Duration interval;

		private Instant lastExecution = Instant.MIN;


		public boolean shouldRunNow()
		{
			Duration sinceLastExcecution = Duration.between(lastExecution, Instant.now());
			return sinceLastExcecution.compareTo(interval) >= 0;
		}

		public void run()
		{
			task.run();
			lastExecution = Instant.now();
		}

	}

}
