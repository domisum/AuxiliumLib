package io.domisum.lib.auxiliumlib.contracts.source.optional;

import io.domisum.lib.auxiliumlib.datastructures.ExpirationSettings;
import io.domisum.lib.auxiliumlib.datastructures.LazyCache;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class OptionalSource_CacheInMemory<K, V>
	implements OptionalSource<K, V>
{
	
	// DEPENDENCIES
	private final OptionalSource<K, V> backingSource;
	
	// CACHE
	private final LazyCache<K, V> cache = LazyCache.of(EXPIRATION_SETTINGS());
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract ExpirationSettings EXPIRATION_SETTINGS();
	
	
	// SOURCE
	@Override
	public Optional<? extends V> get(K key)
	{
		var itemFromCacheOptional = cache.get(key);
		if(itemFromCacheOptional.isPresent())
			return itemFromCacheOptional;
		
		var itemFromBackingSourceOptional = backingSource.get(key);
		cache.put(key, itemFromBackingSourceOptional.orElse(null));
		return itemFromBackingSourceOptional;
	}
	
}
