package de.domisum.lib.auxilium.util.java.exceptions;

import de.domisum.lib.auxilium.util.java.annotations.API;

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
