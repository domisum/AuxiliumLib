package io.domisum.lib.auxiliumlib.contracts.source.io.impl;

import io.domisum.lib.auxiliumlib.contracts.source.io.IoSource;
import io.domisum.lib.auxiliumlib.time.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.time.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class IoSource_CacheInMemory<K, V>
	implements IoSource<K, V>
{
	
	// DEPENDENCIES
	private final IoSource<K, V> backingSource;
	
	// CACHE
	private final LazyCache<K, V> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public synchronized V get(K key)
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
