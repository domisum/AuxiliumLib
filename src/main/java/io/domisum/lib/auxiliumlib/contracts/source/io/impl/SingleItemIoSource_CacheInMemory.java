package io.domisum.lib.auxiliumlib.contracts.source.io.impl;

import io.domisum.lib.auxiliumlib.contracts.source.io.SingleItemIoSource;
import io.domisum.lib.auxiliumlib.time.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.time.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class SingleItemIoSource_CacheInMemory<V>
	implements SingleItemIoSource<V>
{
	
	// CONSTANTS
	private static final String KEY = "item";
	
	// DEPENDENCIES
	private final SingleItemIoSource<V> backingSource;
	
	// CACHE
	private final LazyCache<String, V> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public synchronized V get()
		throws IOException
	{
		var itemFromCacheOptional = cache.get(KEY);
		if(itemFromCacheOptional.isPresent())
			return itemFromCacheOptional.get();
		
		var itemFromBackingSource = backingSource.get();
		cache.put(KEY, itemFromBackingSource);
		return itemFromBackingSource;
	}
	
}
