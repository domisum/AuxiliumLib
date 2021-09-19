package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class ProgrammingError
	extends AssertionError
{
	
	// CONSTANTS
	private static final String DEFAULT_MESSAGE = "This should never happen. If it does happen, the programmer made a mistake";
	
	
	// INIT
	@API
	public ProgrammingError()
	{
		super(DEFAULT_MESSAGE);
	}
	
	@API
	public ProgrammingError(String message)
	{
		super(message);
	}
	
	@API
	public ProgrammingError(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	@API
	public ProgrammingError(Throwable cause)
	{
		super(DEFAULT_MESSAGE, cause);
	}
	
}
