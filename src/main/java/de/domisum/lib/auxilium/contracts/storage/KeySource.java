package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.util.java.annotations.API;

public interface KeySource<T, K>
{

	/**
	 * Fetches the item associated with the given key.
	 *
	 * @param key the key of the object to retrieve from storage
	 * @return the T associated with the key, or null if none found
	 */
	@API T fetch(K key);

}
