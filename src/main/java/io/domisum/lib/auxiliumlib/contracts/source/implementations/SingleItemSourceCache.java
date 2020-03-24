package io.domisum.lib.auxiliumlib.contracts.source.implementations;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.source.SingleItemSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SingleItemSourceCache<T>
		implements SingleItemSource<T>
{
	
	// REFERENCES
	private final Duration invalidationInterval;
	private final SingleItemSource<T> backingSource;
	
	// CACHE
	private T cachedItem;
	private Instant lastFetchFromBackingSource;
	
	
	// INIT
	@API
	public static <T> SingleItemSourceCache<T> neverInvalidate(SingleItemSource<T> backingSource)
	{
		return new SingleItemSourceCache<>(null, backingSource);
	}
	
	@API
	public static <T> SingleItemSourceCache<T> invalidateEvery(Duration invalidationInterval, SingleItemSource<T> backingSource)
	{
		return new SingleItemSourceCache<>(invalidationInterval, backingSource);
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
		var fromBackingSource = backingSource.fetch();
		if(fromBackingSource.isEmpty())
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
		
		var oldestAllowedFetchInstant = Instant.now().minus(invalidationInterval);
		return (cachedItem != null) && lastFetchFromBackingSource.isBefore(oldestAllowedFetchInstant);
	}
	
}
