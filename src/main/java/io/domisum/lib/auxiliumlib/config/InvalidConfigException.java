package io.domisum.lib.auxiliumlib.config;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Collection;
import java.util.Map;

@API
public class InvalidConfigException
		extends Exception
{
	
	// INIT
	@API
	public InvalidConfigException(String message)
	{
		super(message);
	}
	
	@API
	public InvalidConfigException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	// VALIDATION
	@API
	@SuppressWarnings("BooleanParameter")
	public static void validateIsTrue(boolean expression, String failMessage)
			throws InvalidConfigException
	{
		if(!expression)
			throw new InvalidConfigException(failMessage);
	}
	
	@API
	public static void validateIsSet(Object object, String objectName)
			throws InvalidConfigException
	{
		if(object == null)
			throw new InvalidConfigException(objectName+" has to be set (was null)");
	}
	
	@API
	public static void validateNotBlank(String value, String stringName)
			throws InvalidConfigException
	{
		validateIsSet(value, stringName);
		if(value.isBlank())
			throw new InvalidConfigException(stringName+" can't be blank");
	}
	
	@API
	public static void validateNotEmpty(Collection<?> collection, String collectionName)
			throws InvalidConfigException
	{
		validateIsSet(collection, collectionName);
		if(collection.isEmpty())
			throw new InvalidConfigException(collectionName+" can't be empty");
	}
	
	@API
	public static <T> void validateContainsKey(Map<T,?> map, T key, String mapName)
			throws InvalidConfigException
	{
		validateIsSet(map, mapName);
		if(!map.containsKey(key))
			throw new InvalidConfigException(mapName+" has to contain key "+key+", but didn't");
	}
	
	
	// SPECIFIC VALIDATION
	@API
	public static void validatePort(Integer port, String portName)
			throws InvalidConfigException
	{
		final int MAX_PORT_VALUE = 65535;
		
		validateIsSet(port, portName);
		if(port < 1 || port > MAX_PORT_VALUE)
			throw new InvalidConfigException("port "+portName+" out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}
	
}
