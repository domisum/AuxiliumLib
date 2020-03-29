package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Map;

@API
public class InvalidConfigurationException
		extends Exception
{
	
	// INIT
	@API
	public InvalidConfigurationException(String message)
	{
		super(message);
	}
	
	@API
	public InvalidConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	// VALIDATION
	@API
	@SuppressWarnings("BooleanParameter")
	public static void validateIsTrue(boolean expression, String failMessage)
			throws InvalidConfigurationException
	{
		if(!expression)
			throw new InvalidConfigurationException(failMessage);
	}
	
	@API
	public static void validateIsSet(Object object, String objectName)
			throws InvalidConfigurationException
	{
		if(object == null)
			throw new InvalidConfigurationException(objectName+" has to be set (was null)");
	}
	
	@API
	public static <T> void validateContainsKey(Map<T,?> map, T key, String mapName)
			throws InvalidConfigurationException
	{
		if(!map.containsKey(key))
			throw new InvalidConfigurationException(mapName+" has to contain key "+key+", but didn't");
	}
	
	
	// SPECIFIC VALIDATION
	@API
	public static void validatePort(int port, String portName)
			throws InvalidConfigurationException
	{
		final int MAX_PORT_VALUE = 65535;
		
		if(port < 1 || port > MAX_PORT_VALUE)
			throw new InvalidConfigurationException("port "+portName+" out of range [1-"+MAX_PORT_VALUE+"]: "+port);
	}
	
}
