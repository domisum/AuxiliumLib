package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.HashMap;
import java.util.Map;

@APIUsage
public class DataRegister
{

	// DATA
	private Map<String, Object> data = new HashMap<>();


	// GETTERS
	@APIUsage public Object get(String key)
	{
		Object value = this.data.get(key);
		if(value == null)
			throw new DataRegisterException("There is no object mapped to the key '"+key+"'");

		return value;
	}

	@APIUsage public boolean doesValueExist(String key)
	{
		return this.data.get(key) != null;
	}


	// SETTERS
	@APIUsage public DataRegister set(String key, Object object)
	{
		this.data.put(key, object);

		return this;
	}


	// DATA REGISTER EXCEPTION
	private class DataRegisterException extends RuntimeException
	{

		// INIT
		private DataRegisterException(String message)
		{
			super(message);
		}

	}

}
