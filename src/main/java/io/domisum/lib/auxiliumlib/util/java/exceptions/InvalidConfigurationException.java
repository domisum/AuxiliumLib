package io.domisum.lib.auxiliumlib.util.java.exceptions;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
public class InvalidConfigurationException extends RuntimeException
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
