package io.domisum.lib.auxiliumlib.datastructures;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class TimedMultiLock<KeyT>
{
	
	// SETTINGS
	@Getter
	@Nullable
	private final Duration defaultDuration;
	
	// STATUS
	private final Map<KeyT, Instant> lockedUntilMap = new HashMap<>();
	
	
	// INIT
	public TimedMultiLock()
	{
		defaultDuration = null;
	}
	
	
	// LOCK
	@API
	public synchronized void lock(KeyT key, Duration duration)
	{
		lockedUntilMap.put(key, Instant.now().plus(duration));
	}
	
	@API
	public synchronized void lock(KeyT key)
	{
		if(defaultDuration == null)
			throw new IllegalStateException("Can't use this method when no defaultDuration was given in constructor");
		
		lock(key, defaultDuration);
	}
	
	@API
	public synchronized void unlock(KeyT key)
	{
		lockedUntilMap.remove(key);
	}
	
	
	// STATUS
	@API
	public synchronized boolean isLocked(KeyT key)
	{
		var lockedUntil = lockedUntilMap.get(key);
		if(lockedUntil == null)
			return false;
		
		if(TimeUtil.isInFuture(lockedUntil))
			return true;
		else
		{
			lockedUntilMap.remove(key);
			return false;
		}
	}
	
	@API
	public synchronized Optional<Duration> getRemainingLockDuration(KeyT key)
	{
		var lockedUntil = lockedUntilMap.get(key);
		if(lockedUntil == null)
			return Optional.empty();
		
		var remainingLockDuration = TimeUtil.until(lockedUntil);
		if(remainingLockDuration.isZero() || remainingLockDuration.isNegative())
		{
			lockedUntilMap.remove(key);
			return Optional.empty();
		}
		
		return Optional.of(remainingLockDuration);
	}
	
	@API
	public synchronized Optional<Instant> getNextLockReleaseInstant()
	{
		lockedUntilMap.entrySet().removeIf(e->TimeUtil.isInPast(e.getValue()));
		return lockedUntilMap.values().stream().min(Comparator.naturalOrder());
	}
	
}
