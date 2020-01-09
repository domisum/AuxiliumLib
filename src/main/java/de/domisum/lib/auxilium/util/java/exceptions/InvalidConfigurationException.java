package de.domisum.lib.auxilium.util.java.exceptions;

import de.domisum.lib.auxilium.util.java.annotations.API;

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
