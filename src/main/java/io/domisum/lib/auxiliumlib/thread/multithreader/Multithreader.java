package io.domisum.lib.auxiliumlib.thread.multithreader;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@API
@RequiredArgsConstructor
public class Multithreader<I, O>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// SETTINGS
	private final Function<I, O> action;
	
	private final String name;
	private final int numberOfThreads;
	
	// STATUS
	private final AtomicInteger runCounter = new AtomicInteger(0);
	
	
	// CONSTANT METHODS
	private static int DEFAULT_NUMBER_OF_THREADS()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	
	// INIT
	@API
	public static <I, O> Multithreader<I, O> create(Function<I, O> action, String name, int numberOfThreads)
	{
		return new Multithreader<>(action, name, numberOfThreads);
	}
	
	@API
	public static <I, O> Multithreader<I, O> create(Function<I, O> action, String name)
	{
		return create(action, name, DEFAULT_NUMBER_OF_THREADS());
	}
	
	@API
	public static <I, O> Multithreader<I, O> overcreate(Function<I, O> action, String name, double overcreateFactor)
	{
		int numberOfThreads = (int) Math.ceil(overcreateFactor*DEFAULT_NUMBER_OF_THREADS());
		return new Multithreader<>(action, name, numberOfThreads);
	}
	
	
	// RUN
	public Set<O> run(MultithreaderQueue<I> queue)
	{
		int runCount = runCounter.incrementAndGet();
		var run = new Run(runCount, queue, action);
		
		return run.run();
	}
	
	@RequiredArgsConstructor
	private class Run
	{
		
		// SETTINGS
		private final int runCount;
		private final MultithreaderQueue<I> queue;
		private final Function<I, O> action;
		
		// OUTPUT
		private final Set<O> outputs = new HashSet<>();
		
		
		// RUN
		public Set<O> run()
		{
			var executorService = createExecutorService();
			var futures = scheduleThreadLoopTasks(executorService);
			
			waitForFuturesCompletion(futures);
			executorService.shutdown();
			
			// noinspection AssignmentOrReturnOfFieldWithMutableType
			return outputs;
		}
		
		private ExecutorService createExecutorService()
		{
			return Executors.newFixedThreadPool(numberOfThreads, getThreadFactory());
		}
		
		private ThreadFactory getThreadFactory()
		{
			var threadCount = new AtomicInteger(0);
			var threadGroup = Thread.currentThread().getThreadGroup();
			
			return runnable->
			{
				String threadName = name+"-run"+runCount+"-t"+threadCount.incrementAndGet();
				
				var thread = new Thread(threadGroup, runnable, threadName);
				thread.setDaemon(true);
				
				return thread;
			};
		}
		
		private ArrayList<Future<?>> scheduleThreadLoopTasks(ExecutorService executorService)
		{
			var futures = new ArrayList<Future<?>>();
			for(int i = 0; i < numberOfThreads; i++)
			{
				var future = executorService.submit(this::threadLoop);
				futures.add(future);
			}
			
			return futures;
		}
		
		private void waitForFuturesCompletion(Collection<Future<?>> futures)
		{
			try
			{
				for(var future : futures)
					future.get();
			}
			catch(InterruptedException|ExecutionException e)
			{
				throw new IllegalStateException("Failed to multithread", e);
			}
		}
		
		private void threadLoop()
		{
			while(queue.areThereMoreElements())
			{
				var elementOptional = queue.poll();
				
				if(elementOptional.isPresent())
					processElement(elementOptional.get());
				else
					ThreadUtil.sleep(Duration.ofMillis(10));
			}
		}
		
		private void processElement(I element)
		{
			try
			{
				processElementUncaught(element);
			}
			catch(Exception e)
			{
				logger.error("Exception while multithreading", e);
			}
		}
		
		private void processElementUncaught(I element)
		{
			var output = action.apply(element);
			if(output != null)
				outputs.add(output);
		}
		
	}
	
}
