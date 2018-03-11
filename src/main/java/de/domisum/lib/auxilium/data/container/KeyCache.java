package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.time.DurationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyCache<KeyT, T>
{

	private final Duration expirationDuration;
	private final Map<KeyT, CacheEntry> cache = new ConcurrentHashMap<>();


	// INIT
	@API public static <KeyT, T> KeyCache<KeyT, T> neverExpiresUnused()
	{
		return unusedExpiresAfter(Duration.ofSeconds(Long.MAX_VALUE));
	}

	@API public static <KeyT, T> KeyCache<KeyT, T> unusedExpiresAfter(Duration expirationDuration)
	{
		return new KeyCache<>(expirationDuration);
	}


	// CACHE
	public void store(KeyT key, T value)
	{
		removeExpiredEntries();

		CacheEntry newCacheEntry = new CacheEntry(value);
		cache.put(key, newCacheEntry);
	}

	public Optional<T> get(KeyT key)
	{
		removeExpiredEntries();

		Optional<CacheEntry> optionalCacheEntry = Optional.ofNullable(cache.get(key));
		optionalCacheEntry.ifPresent(CacheEntry::markAsUsed);

		return optionalCacheEntry.map(CacheEntry::getElement);
	}


	// EXPIRATION
	private void removeExpiredEntries()
	{
		cache.entrySet().removeIf(e->e.getValue().isExpired());
	}


	// ENTRY
	@RequiredArgsConstructor
	private class CacheEntry
	{

		private Instant lastUsage = Instant.now();
		@Getter private final T element;


		// STATUS
		private void markAsUsed()
		{
			lastUsage = Instant.now();
		}

		private boolean isExpired()
		{
			Duration sinceLastUsage = DurationUtil.toNow(lastUsage);
			return sinceLastUsage.compareTo(expirationDuration) > 0;
		}

	}

}
