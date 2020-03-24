package io.domisum.lib.auxiliumlib.datastructures;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import io.domisum.lib.auxiliumlib.util.DurationUtil;
import io.domisum.lib.auxiliumlib.timing.PassiveTimer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@API
public final class LazyKeyCache<KeyT, T>
{

	// SETTINGS
	private final Duration expirationDuration;
	private final boolean randomizeExpirationDuration;
	private final boolean onlyExpireUnused;

	// STATE
	private final Map<KeyT, CacheEntry> cache = new ConcurrentHashMap<>();
	private final PassiveTimer expireCheckDueTimer;


	// INIT
	@API
	public static <KeyT, T> LazyKeyCache<KeyT, T> neverExpires()
	{
		return expiresAfter(Duration.ofSeconds(Long.MAX_VALUE));
	}

	@API
	public static <KeyT, T> LazyKeyCache<KeyT, T> expiresAfter(Duration expirationDuration)
	{
		return new LazyKeyCache<>(expirationDuration, false, false);
	}

	@API
	public static <KeyT, T> LazyKeyCache<KeyT, T> expiresAfterRandomized(Duration expirationDuration)
	{
		return new LazyKeyCache<>(expirationDuration, true, false);
	}


	@API
	public static <KeyT, T> LazyKeyCache<KeyT, T> neverExpiresUnused()
	{
		return unusedExpiresAfter(Duration.ofSeconds(Long.MAX_VALUE));
	}

	@API
	public static <KeyT, T> LazyKeyCache<KeyT, T> unusedExpiresAfter(Duration expirationDuration)
	{
		return new LazyKeyCache<>(expirationDuration, false, true);
	}


	private LazyKeyCache(Duration expirationDuration, boolean randomizeExpirationDuration, boolean onlyExpireUnused)
	{
		this.expirationDuration = expirationDuration;
		this.randomizeExpirationDuration = randomizeExpirationDuration;
		this.onlyExpireUnused = onlyExpireUnused;

		expireCheckDueTimer = PassiveTimer.fromDurationAndStart(expirationDuration);
	}


	// CACHE
	@API
	public void put(KeyT key, T value)
	{
		ifDueRemoveExpiredEntries();

		var newCacheEntry = createCacheEntry(value);
		cache.put(key, newCacheEntry);
	}

	@API
	public void remove(KeyT key)
	{
		cache.remove(key);
	}

	@API
	public Optional<T> get(KeyT key)
	{
		ifDueRemoveExpiredEntries();

		var optionalCacheEntry = Optional.ofNullable(cache.get(key));
		optionalCacheEntry.ifPresent(CacheEntry::markAsUsed);
		return optionalCacheEntry.map(CacheEntry::getValue);
	}

	@API
	public Map<KeyT, CacheEntry> getEntries()
	{
		return new HashMap<>(cache);
	}


	private CacheEntry createCacheEntry(T value)
	{
		var expirationDuration = this.expirationDuration;
		if(randomizeExpirationDuration)
		{
			long expirationDurationMillis = expirationDuration.toMillis();

			long maxDifference = expirationDurationMillis/5; // max 20% offset
			long newExpirationDurationMillis = RandomUtil.distribute(expirationDurationMillis, maxDifference);
			expirationDuration = Duration.ofMillis(newExpirationDurationMillis);
		}

		if(onlyExpireUnused)
			return new CacheEntryRespectUsage(expirationDuration, value);

		return new CacheEntryIgnoreUsage(expirationDuration, value);
	}


	// EXPIRATION
	private void ifDueRemoveExpiredEntries()
	{
		if(!expireCheckDueTimer.isOver())
			return;

		expireCheckDueTimer.resetAndStart();
		removeExpiredEntries();
	}

	private void removeExpiredEntries()
	{
		cache.entrySet().removeIf(e->e.getValue().isExpired());
	}


	// ENTRY
	public abstract class CacheEntry
	{

		public abstract T getValue();

		abstract void markAsUsed();

		abstract boolean isExpired();

	}

	@RequiredArgsConstructor
	private class CacheEntryRespectUsage extends CacheEntry
	{

		private Instant lastUsage = Instant.now();
		private final Duration expirationDuration;
		@Getter
		private final T value;


		// STATUS
		@Override
		public void markAsUsed()
		{
			lastUsage = Instant.now();
		}

		@Override
		public boolean isExpired()
		{
			Duration sinceLastUsage = DurationUtil.toNow(lastUsage);
			return sinceLastUsage.compareTo(expirationDuration) > 0;
		}

	}

	@RequiredArgsConstructor
	private class CacheEntryIgnoreUsage extends CacheEntry
	{

		private final Instant created = Instant.now();
		private final Duration expirationDuration;
		@Getter
		private final T value;


		// STATUS
		@Override
		public void markAsUsed()
		{

		}

		@Override
		public boolean isExpired()
		{
			Duration sinceCreation = DurationUtil.toNow(created);
			return sinceCreation.compareTo(expirationDuration) > 0;
		}

	}

}
