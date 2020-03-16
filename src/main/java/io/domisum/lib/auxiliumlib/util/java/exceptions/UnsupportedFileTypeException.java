package io.domisum.lib.auxiliumlib.util.java.exceptions;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
public class UnsupportedFileTypeException extends RuntimeException
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
