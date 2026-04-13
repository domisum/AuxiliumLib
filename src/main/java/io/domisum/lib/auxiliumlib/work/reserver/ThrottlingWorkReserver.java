package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.time.ratelimit.RateLimiter;
import io.domisum.lib.auxiliumlib.time.ratelimit.TrickleRateLimiter;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class ThrottlingWorkReserver<T>
	extends WorkReserver<T>
{
	
	private final WorkReserver<T> backingWorkReserver;
	private final RateLimiter rateLimiter;
	
	
	// INIT
	public static <T> ThrottlingWorkReserver<T> perMinute(WorkReserver<T> backingWorkReserver, double perMinute)
	{return new ThrottlingWorkReserver<>(backingWorkReserver, perMinute);}
	
	public static <T> ThrottlingWorkReserver<T> perDuration(WorkReserver<T> backingWorkReserver, double count, Duration timeframe)
	{return new ThrottlingWorkReserver<>(backingWorkReserver, count / TimeUtil.getMinutesDecimal(timeframe));}
	
	protected ThrottlingWorkReserver(WorkReserver<T> backingWorkReserver, double perMinute)
	{this(backingWorkReserver, Instant::now, perMinute);}
	
	protected ThrottlingWorkReserver(WorkReserver<T> backingWorkReserver, Supplier<Instant> clock, double perMinute)
	{
		this.backingWorkReserver = backingWorkReserver;
		this.rateLimiter = new TrickleRateLimiter(perMinute, Duration.ofMinutes(1), perMinute / 6, clock);
	}
	
	
	// INTERFACE: internal
	@Override
	protected Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		if(rateLimiter.isBlocking())
			return Optional.empty();
		
		var subjectOptional = backingWorkReserver.getNextSubject(reservedSubjects);
		if(subjectOptional.isPresent())
			rateLimiter.blockUntilAcquire();
		return subjectOptional;
	}
	
}
