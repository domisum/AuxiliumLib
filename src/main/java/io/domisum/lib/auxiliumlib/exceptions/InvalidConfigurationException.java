package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class InvalidConfigurationException
		extends Exception
{
	
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
	
}
