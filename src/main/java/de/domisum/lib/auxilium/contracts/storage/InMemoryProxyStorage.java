package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.contracts.storage.InMemoryProxyStorage.Keyable;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemoryProxyStorage<KeyT, T extends Keyable<KeyT>> implements Storage<KeyT, T>
{

	// REFERENCES
	private final Storage<KeyT, T> backingStorage;

	// TEMP
	private transient Map<KeyT, T> items;


	// INIT
	@API public void fetchAllToMemory()
	{
		Collection<T> itemsFromBackingstorage = backingStorage.fetchAll();

		items = new ConcurrentHashMap<>();
		for(T item : itemsFromBackingstorage)
		{
			KeyT key = item.getKey();
			if(key == null)
				throw new IllegalStateException("key of item from backingstorage was null: "+item);

			items.put(item.getKey(), item);
		}
	}

	private void checkReady()
	{
		if(items == null)
			throw new IllegalStateException("can't use this proxy before fetching all the items to memory");
	}


	// STORAGE
	@Override public void store(T item)
	{
		checkReady();

		items.put(item.getKey(), item);
		backingStorage.store(item);
	}

	@Override public void remove(KeyT key)
	{
		checkReady();

		items.remove(key);
		backingStorage.remove(key);
	}

	@Override public Optional<T> fetch(KeyT key)
	{
		checkReady();

		return Optional.ofNullable(items.get(key));
	}

	@Override public Collection<T> fetchAll()
	{
		checkReady();

		return Collections.unmodifiableCollection(items.values());
	}

	@Override public boolean contains(KeyT key)
	{
		checkReady();

		return items.containsKey(key);
	}


	// KEYABLE
	public interface Keyable<T>
	{

		T getKey();

	}

}
