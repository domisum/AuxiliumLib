package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Optional;

public class ThrottlingWorkReserver<T>
	extends WorkReserver<T>
{
	
	// INPUT
	private final WorkReserver<T> backingWorkReserver;
	private final int perMinuteCount;
	
	// STATE
	private final Deque<Instant> subjectInstants;
	
	
	// INIT
	public ThrottlingWorkReserver(WorkReserver<T> backingWorkReserver, int perMinuteCount)
	{
		this.backingWorkReserver = backingWorkReserver;
		this.perMinuteCount = perMinuteCount;
		
		subjectInstants = new ArrayDeque<>(perMinuteCount + 5);
	}
	
	
	// IMPLEMENTATION
	@Override
	protected Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		while(!subjectInstants.isEmpty() && TimeUtil.isOlderThan(subjectInstants.peek(), Duration.ofMinutes(1)))
			subjectInstants.remove();
		
		if(subjectInstants.size() >= perMinuteCount)
			return Optional.empty();
		
		var minimumDelayBetween = Duration.ofMinutes(1).dividedBy(perMinuteCount * 2L);
		var last = subjectInstants.peekLast();
		if(last != null && TimeUtil.isYoungerThan(last, minimumDelayBetween))
			return Optional.empty();
		
		var subjectOptional = backingWorkReserver.getNextSubject(reservedSubjects);
		if(subjectOptional.isPresent())
			subjectInstants.add(Instant.now());
		return subjectOptional;
	}
	
}
