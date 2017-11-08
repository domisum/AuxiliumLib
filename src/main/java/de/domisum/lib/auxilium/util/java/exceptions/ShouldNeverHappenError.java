package de.domisum.lib.auxilium.util.java.exceptions;

public class ShouldNeverHappenError extends AssertionError
{

	// INIT
	public ShouldNeverHappenError()
	{

	}

	public ShouldNeverHappenError(String message)
	{
		super(message);
	}

	public ShouldNeverHappenError(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ShouldNeverHappenError(Throwable cause)
	{
		super(cause);
	}

}
