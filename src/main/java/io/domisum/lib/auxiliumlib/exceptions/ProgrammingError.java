package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class ProgrammingError
	extends AssertionError
{
	
	@API
	public ProgrammingError()
	{
	
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
		super(cause);
	}
	
}
