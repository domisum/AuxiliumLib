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
import java.util.function.Function;
import java.util.function.Supplier;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class LazyCache<KeyT, T>
{
	
	// SETTINGS
	@Nullable
	private final ExpirationSettings expirationSettings;
	
	// STATE
	private final transient Map<KeyT, CacheEntry> entries = new ConcurrentHashMap<>();
	private transient Instant lastExpirationCheck = Instant.now();
	
	
	// INIT
	@API
	public static <KeyT, T> LazyCache<KeyT, T> of(@Nullable ExpirationSettings expirationSettings)
	{
		return new LazyCache<>(expirationSettings);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT, T> neverExpire()
	{
		return of(null);
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT, T> expireAfter(Duration expirationDuration)
	{
		return of(ExpirationSettings.after(expirationDuration));
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT, T> expireAfterRandomized(Duration expirationDuration)
	{
		return of(ExpirationSettings.afterRandomized(expirationDuration));
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT, T> expireUnusedAfter(Duration expirationDuration)
	{
		return of(ExpirationSettings.unusedAfter(expirationDuration));
	}
	
	@API
	public static <KeyT, T> LazyCache<KeyT, T> expireUnusedAfterRandomized(Duration expirationDuration)
	{
		return of(ExpirationSettings.unusedAfterRandomized(expirationDuration));
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
		if(key != null)
			entries.remove(key);
	}
	
	@API
	public void clear()
	{
		entries.clear();
	}
	
	@API
	public T getOrPutAndGet(KeyT key, Supplier<T> valueSupplier)
	{
		return getOrPutAndGet(key, k->valueSupplier.get());
	}
	
	@API
	public T getOrPutAndGet(KeyT key, Function<KeyT, T> valueFunction)
	{
		var entry = get(key);
		if(entry.isPresent())
			return entry.get();
		
		var value = valueFunction.apply(key);
		put(key, value);
		return value;
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
	public boolean containsKey(KeyT key)
	{
		ifDueExpire();
		
		var entry = entries.get(key);
		return entry != null;
	}
	
	
	// EXPIRATION
	private void ifDueExpire()
	{
		if(isExpireDue())
			expire();
	}
	
	private synchronized boolean isExpireDue()
	{
		if(expirationSettings == null)
			return false;
		
		boolean isExpireDue = TimeUtil.isOlderThan(lastExpirationCheck, expirationSettings.getExpirationDuration());
		if(isExpireDue)
			lastExpirationCheck = Instant.now();
		return isExpireDue;
	}
	
	private void expire()
	{
		entries.entrySet().removeIf(e -> e.getValue().isExpired());
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
			if(expirationSettings == null)
				return null;
			
			var expirationDuration = expirationSettings.getExpirationDuration();
			if(expirationSettings.shouldRandomizeExpirationDuration())
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
			
			if(expirationSettings.shouldOnlyExpireUnused())
				return TimeUtil.isOlderThan(lastUsed, expirationDuration);
			
			return TimeUtil.isOlderThan(created, expirationDuration);
		}
		
	}
	
}
