package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class IncompleteCodeError
		extends AssertionError
{
	
	@API
	public IncompleteCodeError()
	{
	
	}
	
	@API
	public IncompleteCodeError(String message)
	{
		super(message);
	}
	
}
