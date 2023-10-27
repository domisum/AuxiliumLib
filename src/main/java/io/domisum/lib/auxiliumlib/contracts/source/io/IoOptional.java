package io.domisum.lib.auxiliumlib.contracts.source.io;

import io.domisum.lib.auxiliumlib.annotations.API;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@API
public class IoOptional<V>
{
	
	// ATTRIBUTES
	private final V value;
	private final IOException exception;
	
	
	// INIT
	@API
	public IoOptional(V value, IOException exception)
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
		catch(UncheckedIOException e)
		{
			return ofException(e.getCause());
		}
	}
	
	
	// GETTERS
	@API
	public boolean isPresent()
	{
		return value != null;
	}
	
	@API
	public boolean isEmpty()
	{
		return value == null;
	}
	
	@API
	public V get()
	{
		if(!isPresent())
			throw new NoSuchElementException("no value present");
		return value;
	}
	
	@API
	public IOException getException()
	{
		if(exception == null)
			throw new NoSuchElementException("no exception present");
		return exception;
	}
	
	@API
	public V getOrThrow()
		throws IOException
	{
		if(isPresent())
			return value;
		throw exception;
	}
	
	@API
	public V getOrThrowWrapped(String message)
		throws IOException
	{
		return getOrThrowWrapped(e->new IOException(message, e));
	}
	
	@API
	public <E extends Throwable> V getOrThrowWrapped(Function<IOException, E> wrap)
		throws E
	{
		try
		{
			return getOrThrow();
		}
		catch(IOException e)
		{
			throw wrap.apply(e);
		}
	}
	
	@API
	public V getOrThrowUnchecked()
	{
		if(isPresent())
			return value;
		throw new UncheckedIOException(exception);
	}
	
	@API
	public V orElse(V defaultValue)
	{
		if(isPresent())
			return get();
		else
			return defaultValue;
	}
	
	
	// FUNCTIONAL
	@API
	public void ifPresent(Consumer<V> consumer)
	{
		if(isPresent())
			consumer.accept(value);
	}
	
	
	// CONVERSION
	@API
	public Optional<V> toOptional()
	{
		return Optional.ofNullable(value);
	}
	
}
