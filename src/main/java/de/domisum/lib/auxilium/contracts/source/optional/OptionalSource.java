package de.domisum.lib.auxilium.contracts.source.optional;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface OptionalSource<KeyT, T>
{

	/**
	 * Fetches the item associated with the given key.
	 *
	 * @param key the key of the object to retrieve from storage
	 * @return the Optional.of(T) associated with the key or Optional.empty() if there is no T associated with key
	 */
	@API
	Optional<T> fetch(KeyT key);

	/**
	 * Checks if this source contains a {@code T} with the key equal to the supplied key.
	 *
	 * @param key the id of the item to check against
	 * @return whether this source contains an item with the supplied key
	 */
	@API
	default boolean contains(KeyT key)
	{
		return fetch(key).isPresent();
	}

	@API
	default T fetchOrException(KeyT key)
	{
		return fetch(key).orElseThrow(()->new NoSuchElementException("source does not contain element with key: "+key));
	}

}
