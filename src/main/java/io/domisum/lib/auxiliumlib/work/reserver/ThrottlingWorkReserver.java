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
	private final SubjectInstants subjectInstants;
	
	
	// INIT
	public ThrottlingWorkReserver(WorkReserver<T> backingWorkReserver, int perMinuteCount)
	{
		this.backingWorkReserver = backingWorkReserver;
		this.perMinuteCount = perMinuteCount;
		
		subjectInstants = new SubjectInstants(perMinuteCount + 5);
	}
	
	
	// INTERFACE
	public void manuallyPutSubjectInstant()
	{
		subjectInstants.addNow();
	}
	
	
	// IMPLEMENTATION
	@Override
	protected Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		if(subjectInstants.getCount() >= perMinuteCount)
			return Optional.empty();
		
		var minimumDelayBetween = Duration.ofMinutes(1).dividedBy(perMinuteCount * 2L);
		var mostRecentOptional = subjectInstants.getMostRecent();
		if(mostRecentOptional.isPresent() && TimeUtil.isYoungerThan(mostRecentOptional.get(), minimumDelayBetween))
			return Optional.empty();
		
		var subjectOptional = backingWorkReserver.getNextSubject(reservedSubjects);
		if(subjectOptional.isPresent())
			subjectInstants.addNow();
		return subjectOptional;
	}
	
	private static class SubjectInstants
	{
		
		private final Deque<Instant> subjectInstants;
		
		
		// INIT
		public SubjectInstants(int capacity)
		{
			this.subjectInstants = new ArrayDeque<>(capacity);
		}
		
		
		// INTERFACE
		public synchronized void addNow()
		{
			subjectInstants.add(Instant.now());
		}
		
		public synchronized int getCount()
		{
			removeOldInstants();
			return subjectInstants.size();
		}
		
		public synchronized Optional<Instant> getMostRecent()
		{
			removeOldInstants();
			return Optional.ofNullable(subjectInstants.peekLast());
		}
		
		
		// IMPLEMENTATION
		private void removeOldInstants()
		{
			while(!subjectInstants.isEmpty() && TimeUtil.isOlderThan(subjectInstants.peek(), Duration.ofMinutes(1)))
				subjectInstants.remove();
		}
		
	}
	
}
