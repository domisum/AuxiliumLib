package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Collection;
import java.util.Map;

@API
public class ConfigException
		extends Exception
{
	
	// INIT
	@API
	public ConfigException(String message)
	{
		super(message);
	}
	
	@API
	public ConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	// VALIDATION
	@API
	@SuppressWarnings("BooleanParameter")
	public static void validateIsTrue(boolean expression, String failMessage)
			throws ConfigException
	{
		if(!expression)
			throw new ConfigException(failMessage);
	}
	
	@API
	public static void validateIsSet(Object object, String objectName)
			throws ConfigException
	{
		if(object == null)
			throw new ConfigException(objectName+" has to be set (was null)");
	}
	
	@API
	public static void validateNotBlank(String value, String stringName)
			throws ConfigException
	{
		validateIsSet(value, stringName);
		if(value.isBlank())
			throw new ConfigException(stringName+" can't be blank");
	}
	
	@API
	public static void validateNotEmpty(Collection<?> collection, String collectionName)
			throws ConfigException
	{
		validateIsSet(collection, collectionName);
		if(collection.isEmpty())
			throw new ConfigException(collectionName+" can't be empty");
	}
	
	@API
	public static <T> void validateContainsKey(Map<T,?> map, T key, String mapName)
			throws ConfigException
	{
		validateIsSet(map, mapName);
		if(!map.containsKey(key))
			throw new ConfigException(mapName+" has to contain key "+key+", but didn't");
	}
	
	
	// SPECIFIC VALIDATION
	@API
	public static void validatePort(Integer port, String portName)
			throws ConfigException
	{
		final int MAX_PORT_VALUE = 65535;
		
		validateIsSet(port, portName);
		if(port < 1 || port > MAX_PORT_VALUE)
			throw new ConfigException("port "+portName+" out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}
	
}
