package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Collection;

public interface FiniteSource<KeyT, T> extends Source<KeyT, T>
{

	/**
	 * Fetches a collection of all T stored by this storage module. If no items are stored, an empty collection is
	 * returned.
	 *
	 * @return a collection containing all the items
	 */
	@API Collection<T> fetchAll();

	/**
	 * Checks if this storage module contains a {@code T} with the key equal to the supplied key.
	 *
	 * @param key the id of the item to check against
	 * @return whether this storage module contains an item with the supplied key
	 */
	@API boolean contains(KeyT key);

}
