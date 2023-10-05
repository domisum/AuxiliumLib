package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.work.ReservedWork;
import io.domisum.lib.auxiliumlib.work.WorkReserver;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public abstract class ForAllOnceWorkReserver<T>
	extends WorkReserver<T>
{
	
	// STATE
	private Set<T> openSubjects;
	private final Map<T, Instant> lockedUntilMap = new HashMap<>();
	
	
	// CONSTANT METHODS
	protected Duration FAIL_LOCK_DURATION()
	{
		return Duration.ofMinutes(5);
	}
	
	
	// WORK
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		if(openSubjects == null)
			openSubjects = new HashSet<>(getAllSubjects());
		
		for(T s : openSubjects)
			if(!reservedSubjects.contains(s))
			{
				var lockedUntil = lockedUntilMap.get(s);
				if(lockedUntil == null || TimeUtil.isInPast(lockedUntil))
					return Optional.of(s);
			}
		
		return Optional.empty();
	}
	
	protected abstract Set<T> getAllSubjects();
	
	
	// RESULT
	@Override
	protected synchronized void onSuccess(ReservedWork<T> work)
	{
		openSubjects.remove(work.getSubject());
	}
	
	@Override
	protected synchronized void onFail(ReservedWork<T> work)
	{
		lockedUntilMap.put(work.getSubject(), Instant.now().plus(FAIL_LOCK_DURATION()));
	}
	
}
