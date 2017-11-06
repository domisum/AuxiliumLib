package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@APIUsage
public class IntervalTaskTicker extends Ticker
{

	// CONSTANTS
	private static final Duration TICK_INTERVAL = Duration.ofMillis(100);

	// TASKS
	private Set<IntervalTask> tasks = new HashSet<>();


	// INIT
	public IntervalTaskTicker(String threadName)
	{
		super(TICK_INTERVAL, threadName);
	}

	@APIUsage public void addTask(Runnable task, Duration interval)
	{
		IntervalTask intervalTask = new IntervalTask(task, interval);
		this.tasks.add(intervalTask);
	}


	// TICK
	@Override protected void tick()
	{
		for(IntervalTask t : this.tasks)
			if(t.shouldRunNow())
				t.run();
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
			Duration sinceLastExcecution = Duration.between(this.lastExecution, Instant.now());
			return sinceLastExcecution.compareTo(this.interval) >= 0;
		}

		public void run()
		{
			try
			{
				this.task.run();
			}
			catch(Exception e) // catch all exceptions so ticker doesn't crash because of exception in task
			{
				e.printStackTrace();
			}

			this.lastExecution = Instant.now();
		}

	}

}
