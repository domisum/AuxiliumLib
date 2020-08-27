package io.domisum.lib.auxiliumlib.datastructures;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LazyCache<KeyT, T>
{
	
	// SETTINGS
	@Nullable
	private final Duration expirationDuration;
	private final boolean randomizeExpirationDuration;
	private final boolean onlyExpireUnused;
	
	// STATE
	private final transient Map<KeyT,CacheEntry> entries = new ConcurrentHashMap<>();
	private transient Instant lastExpirationCheck = Instant.now();
	
	
	// INIT
	@API
	public static <KeyT, T> LazyCache<KeyT,T> neverExpire()
	{
		return new LazyCache<>(null, false, false);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT,T> expireAfter(Duration expirationDuration)
	{
		return new LazyCache<>(expirationDuration, false, false);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT,T> expireAfterRandomized(Duration expirationDuration)
	{
		return new LazyCache<>(expirationDuration, true, false);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT,T> expireUnusedAfter(Duration expirationDuration)
	{
		return new LazyCache<>(expirationDuration, false, true);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT,T> expireUnusedAfterRandomized(Duration expirationDuration)
	{
		return new LazyCache<>(expirationDuration, true, true);
	}
	
	
	// CACHE
	@API
	public void put(KeyT key, T value)
	{
		ifDueExpire();
		entries.put(key, new CacheEntry(value));
	}
	
	@API
	public void remove(KeyT key)
	{
		ifDueExpire();
		entries.remove(key);
	}
	
	@API
	public Optional<T> get(KeyT key)
	{
		ifDueExpire();
		
		var entry = entries.get(key);
		if(entry == null)
			return Optional.empty();
		if(entry.isExpired()) // don't remove expired entry because of race conditions, will be removed on next expire anyway
			return Optional.empty();
		
		entry.markAsUsed();
		return Optional.of(entry.getValue());
	}
	
	@API
	public void clear()
	{
		entries.clear();
	}
	
	
	// EXPIRATION
	private void ifDueExpire()
	{
		if(isExpireDue())
			expire();
	}
	
	private synchronized boolean isExpireDue()
	{
		if(expirationDuration == null)
			return false;
		
		boolean isExpirationDue = TimeUtil.isOlderThan(lastExpirationCheck, expirationDuration);
		if(isExpirationDue)
			lastExpirationCheck = Instant.now();
		return isExpirationDue;
	}
	
	private void expire()
	{
		entries.entrySet().removeIf(e->e.getValue().isExpired());
	}
	
	
	// ENTRY
	@RequiredArgsConstructor
	private class CacheEntry
	{
		
		// BASE ATTRIBUTES
		@Getter
		private final T value;
		private final Duration expirationDuration = determineExpirationDuration();
		
		// STATUS
		private final Instant created = Instant.now();
		private Instant lastUsed = created;
		
		
		// INIT
		private Duration determineExpirationDuration()
		{
			var expirationDuration = LazyCache.this.expirationDuration;
			if(expirationDuration != null && randomizeExpirationDuration)
			{
				final double maxOffsetRel = 0.2;
				expirationDuration = RandomUtil.distributeRel(expirationDuration, maxOffsetRel);
			}
			
			return expirationDuration;
		}
		
		
		// EXPIRATION
		private void markAsUsed()
		{
			lastUsed = Instant.now();
		}
		
		private boolean isExpired()
		{
			if(expirationDuration == null)
				return false;
			
			if(onlyExpireUnused)
				return TimeUtil.isOlderThan(lastUsed, expirationDuration);
			
			return TimeUtil.isOlderThan(created, expirationDuration);
		}
		
	}
	
}
