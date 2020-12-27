package io.domisum.lib.auxiliumlib.contracts.iosource.impl;

import io.domisum.lib.auxiliumlib.contracts.iosource.IoSource;
import io.domisum.lib.auxiliumlib.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class IoSource_CacheInMemory<KeyT, T>
	implements IoSource<KeyT, T>
{
	
	// DEPENDENCIES
	private final IoSource<KeyT, T> backingSource;
	
	// CACHE
	private final LazyCache<KeyT, T> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public T get(KeyT key)
		throws IOException
	{
		var itemFromCacheOptional = cache.get(key);
		if(itemFromCacheOptional.isPresent())
			return itemFromCacheOptional.get();
		
		var itemFromBackingSource = backingSource.get(key);
		cache.put(key, itemFromBackingSource);
		return itemFromBackingSource;
	}
	
}
