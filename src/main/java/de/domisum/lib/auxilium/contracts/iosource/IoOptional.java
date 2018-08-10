package de.domisum.lib.auxilium.contracts.iosource;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoOptional<T>
{

	private final T value;
	private final IOException exception;


	// INIT
	@API public static <T> IoOptional<T> of(T value)
	{
		Validate.notNull(value);
		return new IoOptional<>(value, null);
	}

	@API public static <T> IoOptional<T> ofException(IOException exception)
	{
		return new IoOptional<>(null, exception);
	}


	// GETTERS
	public boolean isPresent()
	{
		return value != null;
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

	@API public T getOrThrow() throws IOException
	{
		if(isPresent())
			return value;

		throw exception;
	}

	@API public T getOrThrowUnchecked()
	{
		if(isPresent())
			return value;

		throw new UncheckedIOException(exception);
	}


	// USAGE
	@API public void ifPresent(Consumer<T> consumer)
	{
		if(isPresent())
			consumer.accept(value);
	}

}
