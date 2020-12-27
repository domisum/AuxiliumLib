package io.domisum.lib.auxiliumlib.contracts.iosource.impl;

import io.domisum.lib.auxiliumlib.contracts.iosource.SingleItemIoSource;
import io.domisum.lib.auxiliumlib.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class SingleItemIoSource_CacheInMemory<T>
	implements SingleItemIoSource<T>
{
	
	// CONSTANTS
	private static final String KEY = "item";
	
	// DEPENDENCIES
	private final SingleItemIoSource<T> backingSource;
	
	// CACHE
	private final LazyCache<String, T> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public T get()
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
