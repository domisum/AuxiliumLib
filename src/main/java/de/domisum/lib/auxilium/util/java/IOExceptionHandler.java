package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.contracts.Converter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Consumer;

public interface IOExceptionHandler extends ExceptionHandler<IOException>
{

	// INIT
	static IOExceptionHandler noAction()
	{
		return e->
		{
			// do nothing
		};
	}


	// HANDLE
	@Override void handle(IOException e);


	static void executeOrException(Consumer<IOExceptionHandler> toExecute)
	{
		IOException[] exception = new IOException[1];

		toExecute.accept(e->exception[0] = e);

		if(exception[0] != null)
			throw new UncheckedIOException(exception[0]);
	}

	static <T> T getOrException(Converter<IOExceptionHandler, Optional<T>> toGet)
	{
		IOException[] exception = new IOException[1];

		Optional<T> value = toGet.convert(e->exception[0] = e);
		if(value.isPresent())
			return value.get();

		throw new UncheckedIOException(exception[0]);
	}

}
