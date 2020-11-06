package io.domisum.lib.auxiliumlib.work;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@API
public abstract class WorkDistributor<T>
{
	
	// STATUS
	private final WorkQueue workQueue = new WorkQueue();
	private final Lock refillLock = new ReentrantLock();
	private final Set<T> reservedWorks = new HashSet<>();
	
	
	// GET
	public Optional<ReservedWork<T>> getWork()
	{
		if(shouldRefill())
			if(refillLock.tryLock())
				try
				{
					refill();
				}
				finally
				{
					refillLock.unlock();
				}
		
		var workOptional = workQueue.poll();
		if(workOptional.isEmpty())
			return Optional.empty();
		var work = workOptional.get();
		
		reservedWorks.add(work);
		var reservedWork = ReservedWork.ofOnSuccessfulOnClose(work, this::onSuccess, this::onClose);
		return Optional.of(reservedWork);
	}
	
	
	// RESERVED WORK
	protected void onSuccess(T work)
	{
		// nothing in base impl
	}
	
	protected void onClose(T work)
	{
		reservedWorks.remove(work);
	}
	
	
	// REFILL
	protected abstract boolean shouldRefill();
	
	protected void refill()
	{
		var moreWork = getMoreWork();
		for(T w : moreWork)
		{
			if(reservedWorks.contains(w))
				continue;
			
			workQueue.insert(w);
		}
	}
	
	protected abstract Collection<T> getMoreWork();
	
	
	// QUEUE
	protected int getQueueSize()
	{
		return workQueue.size();
	}
	
	private class WorkQueue
	{
		
		private final Queue<T> queue = new LinkedList<>();
		private final Set<T> set = new HashSet<>();
		
		
		// QUEUE
		public synchronized void insert(T work)
		{
			if(set.contains(work))
				return;
			
			queue.add(work);
			set.add(work);
		}
		
		public synchronized Optional<T> poll()
		{
			var work = queue.poll();
			if(work != null)
				set.remove(work);
			
			return Optional.of(work);
		}
		
		
		// GETTERS
		public int size()
		{
			return set.size();
		}
		
	}
	
}
