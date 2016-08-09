package de.domisum.auxiliumapi.data.container;

import java.util.HashMap;
import java.util.Map;

public class DataRegister
{

	// DATA
	private Map<String, Object> data = new HashMap<>();


	// -------
	// CONSTRUCTOR
	// -------
	public DataRegister()
	{

	}


	// -------
	// GETTERS
	// -------
	public Object get(String key)
	{
		Object value = this.data.get(key);
		if(value == null)
			throw new DataRegisterException("There is no object mapped to the key '"+key+"'");

		return value;
	}

	public boolean doesValueExist(String key)
	{
		return this.data.get(key) != null;
	}


	// -------
	// SETTERS
	// -------
	public DataRegister set(String key, Object object)
	{
		this.data.put(key, object);

		return this;
	}


	// -------
	// DATA REGISTER EXCEPTION
	// -------
	public class DataRegisterException extends RuntimeException
	{

		// CONSTANTS
		private static final long serialVersionUID = 1L;


		// -------
		// CONSTRUCTOR
		// -------
		public DataRegisterException()
		{

		}

		public DataRegisterException(String message)
		{
			super(message);
		}

	}

}
