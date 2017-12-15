package de.domisum.lib.auxilium.contracts.storage;

/**
 * Interface describing the contract of a storage module. The purpose of the module is to store objects of the type
 * <code>T</code> and be able to retrieve the instances later on. This contract does not guarantee persistence,
 * which could cause loss of stored data on shutdown. To check if this is the case, check the actual implementation of the
 * Storage module.
 * <p>
 * A storage module implementing this interface can only hold one item per id.
 *
 * @param <T> the type of item to be stored
 */
public interface Storage<T> extends Source<T>
{

	/**
	 * Stores a <code>T</code> in this module in order to fetch it later on. If the storage module already contains
	 * another item with the same id, the old item is discarded and the new item stored in its place.
	 *
	 * @param item the item to store
	 */
	void store(T item);

	/**
	 * Removes the item with the supplied id from the storage module.
	 *
	 * @param id the id of the item to remove
	 */
	void remove(String id);

}
