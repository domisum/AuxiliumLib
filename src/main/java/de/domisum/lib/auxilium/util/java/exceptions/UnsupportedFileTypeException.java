package de.domisum.lib.auxilium.util.java.exceptions;

import de.domisum.lib.auxilium.util.java.annotations.API;

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
