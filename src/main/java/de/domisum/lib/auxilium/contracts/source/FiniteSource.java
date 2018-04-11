package de.domisum.lib.auxilium.contracts.source;

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

}
