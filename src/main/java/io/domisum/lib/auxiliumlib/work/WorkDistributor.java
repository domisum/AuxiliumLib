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
	private final Set<T> reservedWorkSubjects = new HashSet<>();
	
	
	// GET
	public Optional<ReservedWork<T>> getWorkOptional()
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
		
		var workSubjectOptional = workQueue.poll();
		if(workSubjectOptional.isEmpty())
			return Optional.empty();
		var workSubject = workSubjectOptional.get();
		
		reservedWorkSubjects.add(workSubject);
		var reservedWork = ReservedWork.ofOnSuccessfulOnClose(workSubject, this::onSuccess, this::onClose);
		return Optional.of(reservedWork);
	}
	
	
	// RESERVED WORK
	protected void onSuccess(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	protected void onClose(ReservedWork<T> work)
	{
		reservedWorkSubjects.remove(work.getSubject());
	}
	
	
	// REFILL
	protected abstract boolean shouldRefill();
	
	protected void refill()
	{
		var moreWork = getMoreWork();
		for(T w : moreWork)
		{
			if(reservedWorkSubjects.contains(w))
				continue;
			
			workQueue.insertIfNotContained(w);
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
		public synchronized void insertIfNotContained(T work)
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
			
			return Optional.ofNullable(work);
		}
		
		
		// GETTERS
		public int size()
		{
			return set.size();
		}
		
	}
	
}
