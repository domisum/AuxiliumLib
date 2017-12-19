package de.domisum.lib.auxilium.contracts.storage;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public abstract class InMemoryProxyKeyStorage<T extends InMemoryProxyKeyStorage.Keyable<K>, K> implements KeyStorage<T, K>
{

	// REFERENCES
	private final KeyStorage<T, K> backingStorage;

	// TEMP
	private transient Map<K, T> items;


	// INIT
	public void fetchAllToMemory()
	{
		Collection<T> matchesFromBackingStorage = backingStorage.fetchAll();

		items = new ConcurrentHashMap<>();
		for(T m : matchesFromBackingStorage)
			items.put(m.getKey(), m);
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

	@Override public void remove(K key)
	{
		checkReady();

		items.remove(key);
		backingStorage.remove(key);
	}

	@Override public Optional<T> fetch(K key)
	{
		checkReady();

		return Optional.ofNullable(items.get(key));
	}

	@Override public Collection<T> fetchAll()
	{
		checkReady();

		return Collections.unmodifiableCollection(items.values());
	}

	@Override public boolean contains(K key)
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
