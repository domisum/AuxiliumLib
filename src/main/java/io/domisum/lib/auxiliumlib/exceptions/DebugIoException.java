package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;

import java.io.IOException;

@API
public class DebugIoException
	extends IOException
{
	
	@Getter
	private final String debugText;
	
	
	@API
	public DebugIoException(String message, String debugText)
	{
		super(message);
		this.debugText = debugText;
	}
	
	@API
	public DebugIoException(String message, Throwable cause, String debugText)
	{
		super(message, cause);
		this.debugText = debugText;
	}
	
}
