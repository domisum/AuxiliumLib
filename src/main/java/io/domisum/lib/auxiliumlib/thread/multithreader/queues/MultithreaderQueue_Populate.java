package io.domisum.lib.auxiliumlib.thread.multithreader.queues;

import io.domisum.lib.auxiliumlib.thread.multithreader.MultithreaderQueue;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class MultithreaderQueue_Populate<T>
	implements MultithreaderQueue<T>
{
	
	// STATE
	private final Queue<T> queue = new ConcurrentLinkedQueue<>();
	private final Lock populateLock = new ReentrantLock();
	private boolean populateComplete = false;
	
	
	// CONSTANT METHODS
	public abstract int POPULATE_THRESHOLD();
	
	
	// QUEUE
	@Override
	public boolean areThereMoreElements()
	{
		if(!populateComplete)
			return true;
		
		return queue.size() > 0;
	}
	
	@Override
	public Optional<T> poll()
	{
		if(!populateComplete && queue.size() < POPULATE_THRESHOLD())
			tryPopulate();
		
		var calcNullable = queue.poll();
		return Optional.ofNullable(calcNullable);
	}
	
	private void tryPopulate()
	{
		if(populateLock.tryLock())
			try
			{
				populate();
			}
			finally
			{
				populateLock.unlock();
			}
	}
	
	private void populate()
	{
		var moreElements = getMoreElements();
		
		if(moreElements == null)
			populateComplete = true;
		else
			queue.addAll(moreElements);
	}
	
	@Nullable
	protected abstract Collection<T> getMoreElements();
	
}
