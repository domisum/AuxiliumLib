package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.util.TimeUtil;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class ThrottlingWorkReserver<T>
	extends WorkReserver<T>
{
	
	// INPUT
	private final WorkReserver<T> backingWorkReserver;
	private final int perMinuteCount;
	
	// STATE
	private final Deque<Instant> subjectInstants = new ArrayDeque<>(perMinuteCount + 5);
	
	
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
