package de.domisum.lib.auxilium.contracts.storage;

import java.util.Collection;

public interface Source<T>
{

	/**
	 * Fetches the item associated with the given id. Iff the storage does not contain an item with the
	 * given id (iff {@link #contains(String)}</code> returns false), null is returned.
	 *
	 * @param id the id of the object to retrieve from storage
	 * @return the T associated with the id, or null if none found
	 */
	T fetch(String id);

	/**
	 * Fetches a collection of all T stored by this storage module. If no items are stored, an empty collection is
	 * returned.
	 *
	 * @return a collection containing
	 */
	Collection<T> fetchAll();

	/**
	 * Checks if this storage module contains a <code>T</code> with the id equal to the supplied id.
	 *
	 * @param id the id of the item to check against
	 * @return whether this storage module contains an item with the supplied id
	 */
	boolean contains(String id);

}
