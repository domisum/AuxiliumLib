package de.domisum.lib.auxilium.util.java.exceptions;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

@APIUsage
public class InvalidConfigurationException extends RuntimeException
{

	@APIUsage public InvalidConfigurationException(String message)
	{
		super(message);
	}

	@APIUsage public InvalidConfigurationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
