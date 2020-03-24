package io.domisum.lib.auxiliumlib.timing;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PassiveTimer
{

	// SETTINGS
	private final Duration duration;

	// TEMP
	private Instant startTime = null;


	// INIT
	@API
	public static PassiveTimer fromDuration(Duration duration)
	{
		return new PassiveTimer(duration);
	}

	@API
	public static PassiveTimer fromDurationAndStart(Duration duration)
	{
		PassiveTimer timer = fromDuration(duration);
		timer.start();

		return timer;
	}


	// TIMER
	@API
	public synchronized void start()
	{
		if(startTime != null)
			throw new IllegalStateException("can't start timer that has already been started, use #reset() first");

		startTime = Instant.now();
	}

	@API
	public synchronized void reset()
	{
		startTime = null;
	}

	@API
	public synchronized void resetAndStart()
	{
		reset();
		start();
	}


	// STATUS
	@API
	public synchronized boolean isReady()
	{
		return startTime == null;
	}

	@API
	public synchronized boolean isOver()
	{
		if(startTime == null)
			throw new IllegalStateException("can't check if timer is over if it hasn't been started yet");

		Duration timeSinceStart = getTimeSinceStart();
		return timeSinceStart.compareTo(duration) >= 0;
	}


	// UTIL
	private Duration getTimeSinceStart()
	{
		return Duration.between(startTime, Instant.now());
	}

}
