package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class ShouldNeverHappenError
		extends AssertionError
{
	
	@API
	public ShouldNeverHappenError()
	{
	
	}
	
	@API
	public ShouldNeverHappenError(String message)
	{
		super(message);
	}
	
	@API
	public ShouldNeverHappenError(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	@API
	public ShouldNeverHappenError(Throwable cause)
	{
		super(cause);
	}
	
}
