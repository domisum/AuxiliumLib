package io.domisum.lib.auxiliumlib.time;

import io.domisum.lib.auxiliumlib.contracts.IoSupplier;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.exceptions.ProgrammingError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class RunStopwatch
{
	
	public static <T> Result<T> run(Supplier<T> run)
	{
		try
		{
			return runIo((IoSupplier<T>) run::get);
		}
		catch(IOException e)
		{
			throw new ProgrammingError("Should never be thrown");
		}
	}
	
	public static <T> Result<T> runIo(IoSupplier<T> run)
		throws IOException
	{
		var start = Instant.now();
		var returnValue = run.get();
		var end = Instant.now();
		var duration = java.time.Duration.between(start, end);
		
		return new Result<>(returnValue, duration);
	}
	
	
	@RequiredArgsConstructor
	public static class Result<T>
	{
		
		private final T returnValue;
		@Getter private final Duration duration;
		
		
		// GETTERS
		public T get()
		{
			return returnValue;
		}
		
		public DurationDisplay displayDuration()
		{
			return DurationDisplay.of(duration);
		}
		
	}
	
}
