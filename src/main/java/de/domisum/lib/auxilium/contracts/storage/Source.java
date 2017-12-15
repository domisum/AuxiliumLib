package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Collection;

public interface Source<T, K>
{

	/**
	 * Fetches the item associated with the given key. Iff the storage does not contain an item with the
	 * given key (iff {@link #contains(K)}</code> returns false), null is returned.
	 *
	 * @param key the key of the object to retrieve from storage
	 * @return the T associated with the key, or null if none found
	 */
	@API T fetch(K key);

	/**
	 * Fetches a collection of all T stored by this storage module. If no items are stored, an empty collection is
	 * returned.
	 *
	 * @return a collection containing all the items
	 */
	@API Collection<T> fetchAll();

	/**
	 * Checks if this storage module contains a <code>T</code> with the key equal to the supplied key.
	 *
	 * @param key the id of the item to check against
	 * @return whether this storage module contains an item with the supplied key
	 */
	@API boolean contains(K key);

}
