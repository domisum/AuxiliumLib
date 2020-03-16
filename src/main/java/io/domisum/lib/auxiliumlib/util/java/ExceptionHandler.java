package io.domisum.lib.auxiliumlib.util.java;

import io.domisum.lib.auxiliumlib.contracts.Converter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Consumer;

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


	static void executeOrIOException(Consumer<ExceptionHandler<IOException>> toExecute)
	{
		IOException[] exception = new IOException[1];

		toExecute.accept(e->exception[0] = e);

		if(exception[0] != null)
			throw new UncheckedIOException(exception[0]);
	}

	static <T> T getOrIOException(Converter<ExceptionHandler<IOException>, Optional<T>> toGet)
	{
		IOException[] exception = new IOException[1];

		Optional<T> value = toGet.convert(e->exception[0] = e);
		if(value.isPresent())
			return value.get();

		throw new UncheckedIOException(exception[0]);
	}

}
