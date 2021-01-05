package io.domisum.lib.auxiliumlib.contracts.source.optional;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface OptionalSource<K, V>
{
	
	@API
	Optional<V> get(K key);
	
	@API
	default boolean contains(K key)
	{
		return get(key).isPresent();
	}
	
	@API
	default V getOrThrow(K key)
		throws IOException
	{
		return get(key).orElseThrow(()->new NoSuchElementException("No element for key: "+key));
	}
	
}
