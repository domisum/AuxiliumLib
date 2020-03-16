package io.domisum.lib.auxiliumlib.contracts.source.optional;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

import java.util.Collection;

public interface FiniteOptionalSource<KeyT, T> extends OptionalSource<KeyT, T>
{

	/**
	 * Fetches a collection of all T stored by this storage module. If no items are stored, an empty collection is
	 * returned.
	 *
	 * @return a collection containing all the items
	 */
	@API
	Collection<T> fetchAll();

}
