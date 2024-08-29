package io.domisum.lib.auxiliumlib.contracts.source.safe.impl;

import io.domisum.lib.auxiliumlib.contracts.source.safe.SafeSource;
import io.domisum.lib.auxiliumlib.time.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.time.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public abstract class SafeSource_CacheInMemory<K, V>
	implements SafeSource<K, V>
{
	
	// DEPENDENCIES
	private final SafeSource<K, V> backingSource;
	
	// CACHE
	private final LazyCache<K, V> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public V get(K key)
	{
		var itemFromCacheOptional = cache.get(key);
		if(itemFromCacheOptional.isPresent())
			return itemFromCacheOptional.get();
		
		var itemFromBackingSource = backingSource.get(key);
		cache.put(key, itemFromBackingSource);
		return itemFromBackingSource;
	}
	
}
