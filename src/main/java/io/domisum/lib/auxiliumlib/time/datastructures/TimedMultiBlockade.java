package io.domisum.lib.auxiliumlib.time.datastructures;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.util.StringReportUtil;
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
public class TimedMultiBlockade<KeyT>
{
	
	// SETTINGS
	@Getter
	@Nullable
	private final Duration defaultDuration;
	
	// STATUS
	private final Map<KeyT, Instant> blockedUntilMap = new HashMap<>();
	
	
	// INIT
	public TimedMultiBlockade()
	{
		defaultDuration = null;
	}
	
	private Duration getDefaultDurationOrThrow()
	{
		if(defaultDuration == null)
			throw new IllegalStateException("Can't use this method when no defaultDuration was given in constructor");
		return defaultDuration;
	}
	
	@Override
	public String toString()
	{
		removeExpired();
		String inner = StringReportUtil.report(blockedUntilMap, TimeUtil::displaySecAndRelative);
		if(!inner.isBlank())
			inner = "\n" + inner + "\n";
		return PHR.r("{}(blockedUntil=[{}])", getClass().getSimpleName(), inner);
	}
	
	
	// LOCK
	@API
	public synchronized boolean tryBlock(KeyT key, Duration duration)
	{
		if(isBlocked(key))
			return false;
		
		block(key, duration);
		return true;
	}
	
	@API
	public synchronized boolean tryBlock(KeyT key)
	{
		return tryBlock(key, getDefaultDurationOrThrow());
	}
	
	@API
	public synchronized void block(KeyT key, Duration duration)
	{
		blockedUntilMap.put(key, Instant.now().plus(duration));
	}
	
	@API
	public synchronized void block(KeyT key)
	{
		block(key, getDefaultDurationOrThrow());
	}
	
	@API
	public synchronized void unblock(KeyT key)
	{
		blockedUntilMap.remove(key);
	}
	
	
	// STATUS
	@API
	public synchronized boolean isBlocked(KeyT key)
	{
		var lockedUntil = blockedUntilMap.get(key);
		if(lockedUntil == null)
			return false;
		
		if(TimeUtil.isInFuture(lockedUntil))
			return true;
		else
		{
			blockedUntilMap.remove(key);
			return false;
		}
	}
	
	@API
	public synchronized Optional<Duration> getRemainingBlockDuration(KeyT key)
	{
		var lockedUntil = blockedUntilMap.get(key);
		if(lockedUntil == null)
			return Optional.empty();
		
		var remainingLockDuration = TimeUtil.until(lockedUntil);
		if(remainingLockDuration.isZero() || remainingLockDuration.isNegative())
		{
			blockedUntilMap.remove(key);
			return Optional.empty();
		}
		
		return Optional.of(remainingLockDuration);
	}
	
	@API
	public synchronized Optional<Instant> getNextBlockReleaseInstant()
	{
		removeExpired();
		return blockedUntilMap.values().stream().min(Comparator.naturalOrder());
	}
	
	
	// INTERNAL
	private synchronized void removeExpired()
	{
		blockedUntilMap.entrySet().removeIf(e -> TimeUtil.isInPast(e.getValue()));
	}
	
}
