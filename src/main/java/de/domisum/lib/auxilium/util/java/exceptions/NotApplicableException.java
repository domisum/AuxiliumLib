package de.domisum.lib.auxilium.util.java.exceptions;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public class NotApplicableException extends RuntimeException
{

	// INIT
	@API
	public NotApplicableException()
	{

	}

	@API
	public NotApplicableException(String message)
	{
		super(message);
	}

}
