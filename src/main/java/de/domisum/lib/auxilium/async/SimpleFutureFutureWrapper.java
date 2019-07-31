package de.domisum.lib.auxilium.async;

import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class SimpleFutureFutureWrapper<T> implements SimpleFuture<T>
{

	private final Future<T> future;


	// SIMPLE FUTURE
	@Override
	public T get()
	{
		try
		{
			return future.get();
		}
		catch(InterruptedException e)
		{
			throw new ShouldNeverHappenError(e);
		}
		catch(ExecutionException e)
		{
			if(e.getCause() instanceof RuntimeException)
				throw (RuntimeException) e.getCause();
			else
				throw new ShouldNeverHappenError(e);
		}
	}

	@Override
	public boolean isDone()
	{
		return future.isDone();
	}

}
