package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@API
public class DataRegister
{

	// DATA
	private final Map<String, Object> data = new HashMap<>();


	// GETTERS
	@API public Object get(String key)
	{
		Object value = data.get(key);
		if(value == null)
			throw new NoSuchElementException("There is no object mapped to the key '"+key+"'");

		return value;
	}

	@API public boolean doesValueExist(String key)
	{
		return data.get(key) != null;
	}


	// SETTERS
	@API public DataRegister set(String key, Object object)
	{
		data.put(key, object);
		return this;
	}

}
