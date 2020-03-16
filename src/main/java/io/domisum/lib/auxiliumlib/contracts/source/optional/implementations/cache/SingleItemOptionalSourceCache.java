package io.domisum.lib.auxiliumlib.contracts.source.optional.implementations.cache;

import io.domisum.lib.auxiliumlib.contracts.source.optional.SingleItemOptionalSource;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SingleItemOptionalSourceCache<T> implements SingleItemOptionalSource<T>
{

	// REFERENCES
	private final Duration invalidationInterval;
	private final SingleItemOptionalSource<T> backingSource;

	// CACHE
	private T cachedItem;
	private Instant lastFetchFromBackingSource;


	// INIT
	@API
	public static <T> SingleItemOptionalSourceCache<T> neverInvalidate(SingleItemOptionalSource<T> backingSource)
	{
		return new SingleItemOptionalSourceCache<>(null, backingSource);
	}

	@API
	public static <T> SingleItemOptionalSourceCache<T> invalidateEvery(
			Duration invalidationInterval,
			SingleItemOptionalSource<T> backingSource)
	{
		return new SingleItemOptionalSourceCache<>(invalidationInterval, backingSource);
	}


	// SOURCE
	@Override
	public synchronized Optional<T> fetch()
	{
		if(shouldInvalidateCache())
			invalidateCache();

		if(cachedItem == null)
			fetchFromBackingSource();

		return Optional.ofNullable(cachedItem);
	}


	// CACHE
	private void fetchFromBackingSource()
	{
		Optional<T> fromBackingSource = backingSource.fetch();
		if(!fromBackingSource.isPresent())
			return;

		cachedItem = fromBackingSource.get();
		lastFetchFromBackingSource = Instant.now();
	}

	private void invalidateCache()
	{
		cachedItem = null;
	}


	// CONDITION UTIL
	private boolean shouldInvalidateCache()
	{
		if(invalidationInterval == null)
			return false;

		Instant oldestAllowedFetchInstant = Instant.now().minus(invalidationInterval);
		return (cachedItem != null) && lastFetchFromBackingSource.isBefore(oldestAllowedFetchInstant);
	}

}
