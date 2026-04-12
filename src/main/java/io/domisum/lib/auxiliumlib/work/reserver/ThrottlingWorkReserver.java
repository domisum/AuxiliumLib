package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.time.TimeUtil;

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
	private final double perMinute;
	
	// STATE
	private final SubjectInstants subjectInstants;
	
	
	// INIT
	public static <T> ThrottlingWorkReserver<T> perMinute(WorkReserver<T> backingWorkReserver, double perMinute)
	{return new ThrottlingWorkReserver<>(backingWorkReserver, perMinute);}
	
	public static <T> ThrottlingWorkReserver<T> perDuration(WorkReserver<T> backingWorkReserver, double count, Duration timeframe)
	{return new ThrottlingWorkReserver<>(backingWorkReserver, count / TimeUtil.getMinutesDecimal(timeframe));}
	
	private ThrottlingWorkReserver(WorkReserver<T> backingWorkReserver, double perMinute)
	{
		this.backingWorkReserver = backingWorkReserver;
		this.perMinute = perMinute;
		subjectInstants = new SubjectInstants((int) Math.round(perMinute + 10));
	}
	
	
	// INTERFACE
	public void manuallyPutSubjectInstant() {subjectInstants.addNow();}
	
	
	// INTERFACE: internal
	@Override
	protected Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		double maxCount = trackingIntervalMinutes() * perMinute;
		if(subjectInstants.getCount() > maxCount)
			return Optional.empty();
		
		var minimumDelayBetween = TimeUtil.fromMinutesDecimal(trackingIntervalMinutes() / (perMinute * 2));
		var mostRecentOptional = subjectInstants.getMostRecent();
		if(mostRecentOptional.isPresent() && TimeUtil.isYoungerThan(mostRecentOptional.get(), minimumDelayBetween))
			return Optional.empty();
		
		var subjectOptional = backingWorkReserver.getNextSubject(reservedSubjects);
		if(subjectOptional.isPresent())
			subjectInstants.addNow();
		return subjectOptional;
	}
	
	
	// INTERNAL
	private double trackingIntervalMinutes()
	{
		double minutes = 5 / perMinute;
		if(minutes < 1)
			minutes = 1;
		return minutes;
	}
	
	private Duration trackingInterval() {return TimeUtil.fromMinutesDecimal(trackingIntervalMinutes());}
	
	private class SubjectInstants
	{
		
		private final Deque<Instant> subjectInstants;
		
		
		// INIT
		public SubjectInstants(int capacity) {this.subjectInstants = new ArrayDeque<>(capacity);}
		
		
		// INTERFACE
		public synchronized void addNow() {subjectInstants.add(Instant.now());}
		
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
		
		
		// INTERNAL
		private void removeOldInstants()
		{
			while(!subjectInstants.isEmpty() && TimeUtil.isOlderThan(subjectInstants.element(), trackingInterval()))
				subjectInstants.remove();
		}
		
	}
	
}
