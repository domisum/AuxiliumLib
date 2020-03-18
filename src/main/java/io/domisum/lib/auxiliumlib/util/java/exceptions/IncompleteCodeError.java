package io.domisum.lib.auxiliumlib.util.java.exceptions;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
@SuppressWarnings("ClassWithTooManyConstructors")
public class IncompleteCodeError extends AssertionError
{

	// INIT
	@API
	public IncompleteCodeError()
	{

	}

	@API
	public IncompleteCodeError(String message)
	{
		super(message);
	}

	@API
	public IncompleteCodeError(String message, Throwable cause)
	{
		super(message, cause);
	}

	@API
	public IncompleteCodeError(Throwable cause)
	{
		super(cause);
	}

}