package de.domisum.lib.auxilium.util.java;

public interface ExceptionHandler<T extends Exception>
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
	void handle(T e);

}
