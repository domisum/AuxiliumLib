package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.util.ExceptionUtil;
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
	private final List<ExceptionHandling<?>> exceptionHandlers = new ArrayList<>();
	
	
	// INTERFACE: EXCEPTIONS
	public <X extends Exception> void handleExc(Class<X> clazz, Consumer<X> handler)
	{
		this.exceptionHandlers.add(new ExceptionHandling<>(clazz, handler, false));
	}
	
	public <X extends Exception> void logAndHandleExc(Class<X> clazz, Consumer<X> handler)
	{
		this.exceptionHandlers.add(new ExceptionHandling<>(clazz, handler, true));
	}
	
	
	// INTERFACE: RUN
	
	/**
	 * @return the number of attempts that led to success. [1 ; maxTries]
	 */
	public int run()
		throws T
	{
		for(int i = 0; i < maxTries; i++)
			try
			{
				run.run();
				return i + 1;
			}
			catch(Exception e)
			{
				var ehlOptional = getExceptionHandling(e);
				ehlOptional.ifPresent(ehl -> ehl.handler().accept(e));
				
				boolean wasLastTry = i == maxTries - 1;
				if(wasLastTry)
					throw e;
				
				if(ehlOptional.isEmpty() || ehlOptional.get().log())
					logger.warn("{} | {}", warnMessage, ExceptionUtil.getSynopsis(e));
			}
		
		throw new ProgrammingError("This should never be reached");
	}
	
	public interface RunThrows<X extends Exception>
	{
		
		void run()
			throws X;
		
	}
	
	
	// INTERNAL
	private <X extends Exception> Optional<ExceptionHandling<X>> getExceptionHandling(X e)
	{
		for(var eh : exceptionHandlers)
			if(eh.clazz() == null || eh.clazz().isAssignableFrom(e.getClass()))
				// noinspection unchecked
				return Optional.of((ExceptionHandling<X>) eh);
		return Optional.empty();
	}
	
	private record ExceptionHandling<R extends Exception>(
		Class<R> clazz,
		Consumer<R> handler,
		boolean log
	) {}
	
}
