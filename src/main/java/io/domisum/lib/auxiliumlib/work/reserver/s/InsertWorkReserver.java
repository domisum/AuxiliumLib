package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.work.reserver.ReservedWork;
import io.domisum.lib.auxiliumlib.work.reserver.WorkReserver;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class InsertWorkReserver<T>
	extends WorkReserver<T>
{
	
	// STATE
	private final Queue<T> queue = new LinkedList<>();
	private final Map<T, Instant> failedSubjectCooldowns = new HashMap<>();
	
	
	// CONSTANT METHODS
	protected Duration FAIL_COOLDOWN()
	{
		return Duration.ofMinutes(5);
	}
	
	
	// INTERFACE
	public synchronized void insert(T subject)
	{
		if(!reservedSubjects.contains(subject))
			if(!queue.contains(subject))
				if(!failedSubjectCooldowns.containsKey(subject))
					queue.add(subject);
	}
	
	public void insertAll(Iterable<T> subjects)
	{
		for(T subject : subjects)
			insert(subject);
	}
	
	public boolean isEmpty()
	{
		return queue.isEmpty() && reservedSubjects.isEmpty();
	}
	
	
	// IMPLEMENTATION
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		reinsertFailedSubjects();
		return Optional.ofNullable(queue.poll());
	}
	
	@Override
	protected synchronized void onFail(ReservedWork<T> work)
	{
		failedSubjectCooldowns.put(work.getSubject(), Instant.now().plus(FAIL_COOLDOWN()));
	}
	
	
	// INTERNAL
	private void reinsertFailedSubjects()
	{
		var iterator = failedSubjectCooldowns.entrySet().iterator();
		while(iterator.hasNext())
		{
			var entry = iterator.next();
			if(TimeUtil.isInPast(entry.getValue()))
			{
				iterator.remove();
				queue.add(entry.getKey());
			}
		}
	}
	
}
