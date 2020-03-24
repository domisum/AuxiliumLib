package io.domisum.lib.auxiliumlib.contracts.source.implementations;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.Keyable;
import io.domisum.lib.auxiliumlib.contracts.source.FiniteSource;
import io.domisum.lib.auxiliumlib.contracts.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemoryProxySource<KeyT, T extends Keyable<KeyT>>
		implements FiniteSource<KeyT,T>
{
	
	// REFERENCES
	protected final Storage<KeyT,T> backingSource;
	
	// TEMP
	protected transient Map<KeyT,T> items;
	
	
	// INIT
	@API
	public void fetchAllToMemory()
	{
		var itemsFromBackingstorage = backingSource.fetchAll();
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
	@Override
	public Optional<T> fetch(KeyT key)
	{
		Validate.notNull(key, "key can't be null");
		checkReady();
		return Optional.ofNullable(items.get(key));
	}
	
	@Override
	public Collection<T> fetchAll()
	{
		checkReady();
		return Collections.unmodifiableCollection(items.values());
	}
	
	@Override
	public boolean contains(KeyT key)
	{
		checkReady();
		return items.containsKey(key);
	}
	
}
