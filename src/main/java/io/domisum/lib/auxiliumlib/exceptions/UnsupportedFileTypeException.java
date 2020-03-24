package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class UnsupportedFileTypeException
		extends RuntimeException
{

	// INIT
	@API
	public UnsupportedFileTypeException()
	{

	}

	@API
	public UnsupportedFileTypeException(String s)
	{
		super(s);
	}

	@API
	public UnsupportedFileTypeException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

}
