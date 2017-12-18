package de.domisum.lib.auxilium.util.java;

public interface ExceptionHandler
{

	// INIT
	static ExceptionHandler noAction()
	{
		return (e)->
		{
			// do nothing
		};
	}


	// HANDLE
	void handle(Exception e);

}
