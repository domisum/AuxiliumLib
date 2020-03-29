package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

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
	public static void validateNotNull(Object object, String objectName)
			throws InvalidConfigurationException
	{
		if(object == null)
			throw new InvalidConfigurationException(objectName+" can't be null");
	}
	
}
