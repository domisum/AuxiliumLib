package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface KeySource<KeyT, T>
{

	/**
	 * Fetches the item associated with the given key.
	 *
	 * @param key the key of the object to retrieve from storage
	 * @return the T associated with the key
	 */
	@API Optional<T> fetch(KeyT key);


	@API default T fetchOrException(KeyT key)
	{
		return fetch(key).orElseThrow(()->new NoSuchElementException("source does not contain element with key: "+key));
	}

}
