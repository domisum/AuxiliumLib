package de.domisum.lib.auxilium.contracts.source.io;

import de.domisum.lib.auxilium.contracts.source.io.ioaction.IoAction;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

public class IoOptional<T>
{

	private final T value;
	private final IOException exception;


	// INIT
	public IoOptional(T value, IOException exception)
	{
		this.value = value;
		this.exception = exception;

		if((value == null) && (exception == null))
			throw new IllegalArgumentException("value and exception can't both be null");

		if((value != null) && (exception != null))
			throw new IllegalArgumentException("value can't be provided while exception is provided as well");
	}

	@API
	public static <T> IoOptional<T> of(T value)
	{
		Validate.notNull(value);
		return new IoOptional<>(value, null);
	}

	@API
	public static <T> IoOptional<T> ofException(IOException exception)
	{
		Validate.notNull(exception);
		return new IoOptional<>(null, exception);
	}

	@API
	public static <T> IoOptional<T> ofAction(IoAction<T> ioAction)
	{
		try
		{
			return of(ioAction.execute());
		}
		catch(IOException e)
		{
			return ofException(e);
		}
	}


	// GETTERS
	public boolean isPresent()
	{
		return value != null;
	}

	public boolean isEmpty()
	{
		return value == null;
	}

	public T get()
	{
		if(!isPresent())
			throw new NoSuchElementException("no value present");

		return value;
	}

	public IOException getException()
	{
		if(exception == null)
			throw new NoSuchElementException("no exception present");

		return exception;
	}

	@API
	public T getOrThrow() throws IOException
	{
		if(isPresent())
			return value;

		throw exception;
	}

	@API
	public T getOrThrowWrapped(String message) throws IOException
	{
		try
		{
			return getOrThrow();
		}
		catch(IOException e)
		{
			throw new IOException(message, e);
		}
	}

	@API
	public T getOrThrowUnchecked()
	{
		if(isPresent())
			return value;

		throw new UncheckedIOException(exception);
	}


	// USAGE
	@API
	public void ifPresent(Consumer<T> consumer)
	{
		if(isPresent())
			consumer.accept(value);
	}


	// CONVERSION
	@API
	public Optional<T> toOptional()
	{
		return Optional.ofNullable(value);
	}

}
