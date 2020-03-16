package io.domisum.lib.auxiliumlib.util.java.exceptions;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

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
