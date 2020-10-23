package io.domisum.lib.auxiliumlib.contracts.source;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface Source<KeyT, T>
{
	
	/**
	 * Fetches the item associated with the given key.
	 *
	 * @param key the key of the object to retrieve from storage
	 * @return the Optional.of(T) associated with the key or Optional.empty() if there is no T associated with key
	 */
	@API
	Optional<T> get(KeyT key);
	
	/**
	 * Checks if this source contains a {@code T} with the key equal to the supplied key.
	 *
	 * @param key the id of the item to check against
	 * @return whether this source contains an item with the supplied key
	 */
	@API
	default boolean contains(KeyT key)
	{
		return get(key).isPresent();
	}
	
	@API
	default T getOrError(KeyT key)
	{
		return get(key).orElseThrow(()->new NoSuchElementException("source does not contain element with key: "+key));
	}
	
}
