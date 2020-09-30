package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class NotApplicableException
	extends RuntimeException
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
