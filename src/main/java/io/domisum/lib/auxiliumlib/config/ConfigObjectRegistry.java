package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@API
public class ConfigObjectRegistry<T extends ConfigObject>
{
	
	// REGISTRY
	private final Map<String, T> configObjects;
	
	
	// INIT
	ConfigObjectRegistry(Map<String, T> configObjectsById)
	{
		configObjects = Map.copyOf(configObjectsById);
	}
	
	
	// REGISTRY
	@API
	public T get(String id)
	{
		T configObject = configObjects.get(id);
		if(configObject == null)
			throw new IllegalArgumentException("this registry doesn't contain an entry for id '"+id+"'");
		return configObject;
	}
	
	@API
	public Optional<T> getOptional(String id)
	{
		T configObject = configObjects.get(id);
		return Optional.ofNullable(configObject);
	}
	
	@API
	public boolean contains(String id)
	{
		return configObjects.containsKey(id);
	}
	
	@API
	public Collection<T> getAll()
	{
		return new ArrayList<>(configObjects.values());
	}
	
}
