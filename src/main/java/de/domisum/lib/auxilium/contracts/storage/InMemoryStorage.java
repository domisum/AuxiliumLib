package de.domisum.lib.auxilium.contracts.storage;

import de.domisum.lib.auxilium.contracts.Keyable;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@API
@RequiredArgsConstructor
public class InMemoryStorage<KeyT, T extends Keyable<KeyT>> implements Storage<KeyT, T>
{

	// DATA
	private final transient Map<KeyT, T> items = new HashMap<>();


	// STORAGE
	@Override
	public void store(T item)
	{
		items.put(item.getKey(), item);
	}

	@Override
	public void remove(KeyT key)
	{
		items.remove(key);
	}

	@Override
	public Optional<T> fetch(KeyT key)
	{
		return Optional.ofNullable(items.get(key));
	}

	@Override
	public Collection<T> fetchAll()
	{
		return Collections.unmodifiableCollection(items.values());
	}

	@Override
	public boolean contains(KeyT key)
	{
		return items.containsKey(key);
	}

}
