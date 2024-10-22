package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.IoConsumer;
import io.domisum.lib.auxiliumlib.util.ExceptionUtil;
import io.domisum.lib.auxiliumlib.work.WorkResult;
import org.apache.commons.io.function.IOFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@API
public abstract class WorkReserver<T>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// STATUS
	protected final Set<T> reservedSubjects = new HashSet<>();
	
	
	// UTIL
	@API
	public WorkResult workIo(IOFunction<T, WorkResult> workAction, BiConsumer<T, IOException> onIoException)
	{
		var workOptional = getWorkOptional();
		if(workOptional.isEmpty())
			return WorkResult.successfulWithoutEffort();
		var work = workOptional.get();
		var subject = work.getSubject();
		
		try(work)
		{
			var result = workAction.apply(subject);
			if(result.isSuccess())
				work.successful();
			return result;
		}
		catch(IOException e)
		{
			onIoException.accept(subject, e);
			return WorkResult.workFailed();
		}
	}
	
	
	/**
	 * Performs IO work using the provided workAction and logs a warning message if an IOException occurs.
	 *
	 * @param workAction   The function that performs the IO work
	 * @param errorMessage The error message to log if an IOException occurs,
	 *                     optionally including one '{}' placeholder for the subject
	 * @return Whether effort was made while performing the IO work
	 */
	@API
	public WorkResult workIoWarn(IOFunction<T, WorkResult> workAction, String errorMessage)
	{
		return workIo(workAction, (s, e) ->
			{
				var args = new ArrayList<>();
				if(errorMessage.contains("{}"))
					args.add(s);
				args.add(ExceptionUtil.getSynopsis(e));
				
				logger.warn(errorMessage + ": {}", args.toArray());
			}
		);
	}
	
	/**
	 * Performs IO work using the provided workAction and logs a warning message if an IOException occurs.
	 *
	 * @param workAction   The function that performs the IO work
	 * @param errorMessage The error message to log if an IOException occurs, optionally with one '{}' placeholder
	 * @return Whether effort was made while performing the IO work
	 */
	@API
	public WorkResult workIoWarn(IoConsumer<T> workAction, String errorMessage)
	{
		return workIo(s ->
		{
			workAction.accept(s);
			return WorkResult.didSomeWork();
		}, (s, e) -> warn(errorMessage, s, e));
	}
	
	private void warn(String errorMessage, T subject, Exception exception)
	{
		if(errorMessage.contains("{}"))
			//noinspection StringConcatenationArgumentToLogCall
			logger.warn(errorMessage + ": {}", subject, ExceptionUtil.getSynopsis(exception));
		else
			logger.warn("{}: {}", errorMessage, ExceptionUtil.getSynopsis(exception));
	}
	
	
	@API
	public WorkResult work(Function<T, WorkResult> workAction)
	{
		return workIo(workAction::apply, null);
	}
	
	@API
	public WorkResult work(Consumer<T> workAction)
	{
		return work(s ->
		{
			workAction.accept(s);
			return WorkResult.didSomeWork();
		});
	}
	
	
	// INTERFACE
	@API
	public synchronized Optional<ReservedWork<T>> getWorkOptional()
	{
		var workSubjectOptional = getNextSubject(reservedSubjects);
		if(workSubjectOptional.isEmpty())
			return Optional.empty();
		var workSubject = workSubjectOptional.get();
		
		reservedSubjects.add(workSubject);
		var reservedWork = ReservedWork.ofOnCloseOnSuccessfulOnFail(
			workSubject, this::onCloseInternal, this::onSuccess, this::onFail);
		return Optional.of(reservedWork);
	}
	
	
	// INTERNAL
	private void onCloseInternal(ReservedWork<T> work)
	{
		this.onClose(work);
		reservedSubjects.remove(work.getSubject());
	}
	
	
	// OVERRIDE THIS
	@API
	protected abstract Optional<T> getNextSubject(Collection<T> reservedSubjects);
	
	@API
	protected void onClose(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	@API
	protected void onSuccess(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	@API
	protected void onFail(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
}
