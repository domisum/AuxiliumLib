package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.datacontainers.tuple.Pair;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class CatchRetry<T extends Exception>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// INPUT
	private final RunThrows<T> run;
	private final int maxTries;
	private final String warnMessage;
	private final List<Pair<Class<? extends Exception>, Consumer<Exception>>> exceptionHandlers = new ArrayList<>();
	
	
	// INTERFACE
	public void handleExc(Class<? extends Exception> clazz, Consumer<Exception> handler)
	{
		this.exceptionHandlers.add(new Pair<>(clazz, handler));
	}
	
	public void run()
		throws T
	{
		for(int i = 0; i < maxTries; i++)
			try
			{
				run.run();
				return;
			}
			catch(Exception e)
			{
				if(i == maxTries - 1)
					throw e;
				
				var exhOptional = getExceptionHandler(e);
				exhOptional.ifPresent(eh -> eh.accept(e));
				if(exhOptional.isEmpty())
					logger.warn(warnMessage, e);
			}
	}
	
	public interface RunThrows<X extends Exception>
	{
		
		void run()
			throws X;
		
	}
	
	
	// INTERNAL
	private Optional<Consumer<Exception>> getExceptionHandler(Exception e)
	{
		for(var eh : exceptionHandlers)
			if(eh.getA().isAssignableFrom(e.getClass()))
				return Optional.of(eh.getB());
		return Optional.empty();
	}
	
}
