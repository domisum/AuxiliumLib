package de.domisum.lib.auxilium.util.java;

import java.io.IOException;

public interface IOExceptionHandler extends ExceptionHandler<IOException>
{

	// INIT
	static IOExceptionHandler noAction()
	{
		return (e)->
		{
			// do nothing
		};
	}


	// HANDLE
	@Override void handle(IOException e);

}
