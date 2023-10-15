package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.work.reserver.ReservedWork;
import io.domisum.lib.auxiliumlib.work.reserver.WorkReserver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class InsertWorkReserver<T>
	extends WorkReserver<T>
{
	
	// STATE
	private final Queue<T> queue = new LinkedList<>();
	private final Queue<T> failedQueue = new LinkedList<>();
	
	
	// INTERFACE
	public synchronized void insert(T subject)
	{
		if(!reservedWorkSubjects.contains(subject))
			if(!queue.contains(subject))
				queue.add(subject);
	}
	
	public void insertAll(Iterable<T> subjects)
	{
		for(T subject : subjects)
			insert(subject);
	}
	
	public boolean isEmpty()
	{
		return queue.isEmpty() && reservedWorkSubjects.isEmpty();
	}
	
	
	// IMPLEMENTATION
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		reinsertFailedSubjects(reservedSubjects);
		return Optional.ofNullable(queue.poll());
	}
	
	@Override
	protected synchronized void onFail(ReservedWork<T> work)
	{
		failedQueue.add(work.getSubject());
	}
	
	
	// INTERNAL
	private void reinsertFailedSubjects(Collection<T> reservedSubjects)
	{
		while(true)
		{
			var f = failedQueue.peek();
			if(f != null && !reservedSubjects.contains(f))
			{
				failedQueue.remove();
				queue.add(f);
			}
			else
				break;
		}
	}
	
}
