package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.time.DurationUtil;
import de.domisum.lib.auxilium.util.time.PassiveTimer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@API
public final class LazyKeyCache<KeyT, T>
{

	// SETTINGS
	private final Duration expirationDuration;
	private final boolean onlyExpireUnused;

	// STATE
	private final Map<KeyT, CacheEntry> cache = new ConcurrentHashMap<>();
	private final PassiveTimer expireCheckDueTimer;


	// INIT
	@API public static <KeyT, T> LazyKeyCache<KeyT, T> neverExpires()
	{
		return expiresAfter(Duration.ofSeconds(Long.MAX_VALUE));
	}

	@API public static <KeyT, T> LazyKeyCache<KeyT, T> expiresAfter(Duration expirationDuration)
	{
		return new LazyKeyCache<>(expirationDuration, false);
	}


	@API public static <KeyT, T> LazyKeyCache<KeyT, T> neverExpiresUnused()
	{
		return unusedExpiresAfter(Duration.ofSeconds(Long.MAX_VALUE));
	}

	@API public static <KeyT, T> LazyKeyCache<KeyT, T> unusedExpiresAfter(Duration expirationDuration)
	{
		return new LazyKeyCache<>(expirationDuration, true);
	}


	private LazyKeyCache(Duration expirationDuration, boolean onlyExpireUnused)
	{
		this.expirationDuration = expirationDuration;
		this.onlyExpireUnused = onlyExpireUnused;

		expireCheckDueTimer = PassiveTimer.fromDurationAndStart(expirationDuration);
	}


	// CACHE
	public void put(KeyT key, T value)
	{
		ifDueRemoveExpiredEntries();

		CacheEntry newCacheEntry = createCacheEntry(value);
		cache.put(key, newCacheEntry);
	}

	private CacheEntry createCacheEntry(T value)
	{
		if(onlyExpireUnused)
			return new CacheEntryRespectUsage(value);

		return new CacheEntryIgnoreUsage(value);
	}

	public Optional<T> get(KeyT key)
	{
		ifDueRemoveExpiredEntries();

		Optional<CacheEntry> optionalCacheEntry = Optional.ofNullable(cache.get(key));
		optionalCacheEntry.ifPresent(CacheEntry::markAsUsed);

		return optionalCacheEntry.map(CacheEntry::getValue);
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
	private abstract class CacheEntry
	{

		abstract T getValue();

		abstract void markAsUsed();

		abstract boolean isExpired();

	}

	@RequiredArgsConstructor
	private class CacheEntryRespectUsage extends CacheEntry
	{

		private Instant lastUsage = Instant.now();
		@Getter private final T value;


		// STATUS
		@Override public void markAsUsed()
		{
			lastUsage = Instant.now();
		}

		@Override public boolean isExpired()
		{
			Duration sinceLastUsage = DurationUtil.toNow(lastUsage);
			return sinceLastUsage.compareTo(expirationDuration) > 0;
		}

	}

	@RequiredArgsConstructor
	private class CacheEntryIgnoreUsage extends CacheEntry
	{

		private final Instant created = Instant.now();
		@Getter private final T value;


		// STATUS
		@Override public void markAsUsed()
		{

		}

		@Override public boolean isExpired()
		{
			Duration sinceCreation = DurationUtil.toNow(created);
			return sinceCreation.compareTo(expirationDuration) > 0;
		}

	}

}
