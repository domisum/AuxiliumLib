package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;

import java.io.IOException;

@API
@Getter
public class DebugIoException
	extends IOException
{
	
	private final String debugTextName;
	private final String debugText;
	
	
	@API
	public DebugIoException(String message, String debugTextName, String debugText)
	{
		super(message);
		this.debugTextName = debugTextName;
		this.debugText = debugText;
	}
	
	@API
	public DebugIoException(String message, Throwable cause, String debugTextName, String debugText)
	{
		super(message, cause);
		this.debugTextName = debugTextName;
		this.debugText = debugText;
	}
	
}
