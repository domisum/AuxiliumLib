package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.IoConsumer;
import io.domisum.lib.auxiliumlib.util.ExceptionUtil;
import io.domisum.lib.auxiliumlib.work.Effort;
import io.domisum.lib.auxiliumlib.work.WorkResult;
import org.apache.commons.io.function.IOFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
	public Effort workIo(IOFunction<T, WorkResult> workAction, BiConsumer<T, IOException> onIoException)
	{
		var workOptional = getWorkOptional();
		if(workOptional.isEmpty())
			return Effort.NONE;
		var work = workOptional.get();
		var subject = work.getSubject();
		
		try(work)
		{
			var result = workAction.apply(subject);
			if(result.isSuccessful())
				work.successful();
			return result.getEffort();
		}
		catch(IOException e)
		{
			onIoException.accept(subject, e);
			return Effort.SOME;
		}
	}
	
	
	/**
	 * Performs IO work using the provided workAction and logs a warning message if an IOException occurs.
	 *
	 * @param workAction The function that performs the IO work
	 * @param errorMessage The error message to log if an IOException occurs, including one '{}' placeholder
	 * @return Whether effort was made while performing the IO work
	 */
	@API
	public Effort workIoWarn(IOFunction<T, WorkResult> workAction, String errorMessage)
	{
		return workIo(workAction, (s, e) -> logger.warn(errorMessage + ": {}", s, ExceptionUtil.getSynopsis(e)));
	}
	
	/**
	 * Performs IO work using the provided workAction and logs a warning message if an IOException occurs.
	 *
	 * @param workAction The function that performs the IO work
	 * @param errorMessage The error message to log if an IOException occurs, including one '{}' placeholder
	 * @return Whether effort was made while performing the IO work
	 */
	@API
	public Effort workIoWarn(IoConsumer<T> workAction, String errorMessage)
	{
		return workIo(s ->
		{
			workAction.accept(s);
			return WorkResult.worked();
		}, (s, e) -> logger.warn(errorMessage + ": {}", s, ExceptionUtil.getSynopsis(e)));
	}
	
	
	@API
	public Effort work(Function<T, WorkResult> workAction)
	{
		return workIo(workAction::apply, null);
	}
	
	@API
	public Effort work(Consumer<T> workAction)
	{
		return work(s ->
		{
			workAction.accept(s);
			return WorkResult.worked();
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
	protected abstract Optional<T> getNextSubject(Collection<T> reservedSubjects);
	
	protected void onClose(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	protected void onSuccess(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	protected void onFail(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
}
