package de.domisum.lib.auxilium.util.java;

public interface ExceptionHandler<T extends Exception>
{

	// INIT
	static <T extends Exception> ExceptionHandler<T> noAction()
	{
		return e->
		{
			// do nothing
		};
	}


	// HANDLE
	void handle(T e);

}
