package io.domisum.lib.auxiliumlib.work.reserver.s;

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
	
	
	// INTERFACE
	public void insert(T subject)
	{
		if(!reservedWorkSubjects.contains(subject))
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
		return Optional.ofNullable(queue.poll());
	}
	
}
