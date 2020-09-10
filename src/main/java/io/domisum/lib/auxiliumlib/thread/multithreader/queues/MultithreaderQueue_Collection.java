package io.domisum.lib.auxiliumlib.thread.multithreader.queues;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.thread.multithreader.MultithreaderQueue;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@API
public class MultithreaderQueue_Collection<T>
	implements MultithreaderQueue<T>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	private static final Duration LOG_PROGRESS_INTERVAL = Duration.ofSeconds(5);
	
	// SETTINGS
	private final boolean logProgress;
	
	// DATA
	private final Queue<T> queue = new ConcurrentLinkedQueue<>();
	private final int totalElementsCount;
	
	// STATUS
	private Instant lastProgressLogInstant = Instant.MIN;
	
	
	// INIT
	@API
	public static <T> MultithreaderQueue_Collection<T> create(Collection<T> collection)
	{
		return new MultithreaderQueue_Collection<>(false, collection);
	}
	
	@API
	public static <T> MultithreaderQueue_Collection<T> createWithLogProgress(Collection<T> collection)
	{
		return new MultithreaderQueue_Collection<>(true, collection);
	}
	
	@API
	protected MultithreaderQueue_Collection(boolean logProgress, Collection<T> collection)
	{
		this.logProgress = logProgress;
		queue.addAll(collection);
		totalElementsCount = collection.size();
	}
	
	
	// QUEUE
	@Override
	public boolean areThereMoreElements()
	{
		return queue.size() > 0;
	}
	
	@Override
	public Optional<T> poll()
	{
		logProgressIfAppropriate();
		
		var calcNullable = queue.poll();
		return Optional.ofNullable(calcNullable);
	}
	
	private synchronized void logProgressIfAppropriate()
	{
		if(logProgress && TimeUtil.isOlderThan(lastProgressLogInstant, LOG_PROGRESS_INTERVAL))
		{
			int completedItems = totalElementsCount-queue.size();
			double fractionCompleted = completedItems/(double) totalElementsCount;
			double completedPercentage = fractionCompleted*100;
			String progressDisplay = MathUtil.round(completedPercentage, 1)+"%";
			
			logger.info("Multithreader progress: {}", progressDisplay);
			lastProgressLogInstant = Instant.now();
		}
	}
	
}
