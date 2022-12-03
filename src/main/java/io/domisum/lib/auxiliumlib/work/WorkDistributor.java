package io.domisum.lib.auxiliumlib.work;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.IoConsumer;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import org.apache.commons.io.function.IOFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@API
public abstract class WorkDistributor<T>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// STATUS
	private final WorkQueue workQueue = new WorkQueue();
	private final Lock refillLock = new ReentrantLock();
	private final Set<T> reservedWorkSubjects = new HashSet<>();
	private Instant blockedUntil = Instant.MIN;
	
	
	// CONSTANT METHODS
	protected Duration IO_EXCEPTION_BLOCK_DURATION()
	{
		return Duration.ofSeconds(30);
	}
	
	
	// GET
	@API
	public Optional<ReservedWork<T>> getWorkOptional()
	{
		if(TimeUtil.isInFuture(blockedUntil))
			return Optional.empty();
		
		if(refillLock.tryLock())
			try
			{
				if(shouldRefill())
					refill();
			}
			finally
			{
				refillLock.unlock();
			}
		
		var workSubjectOptional = workQueue.poll();
		if(workSubjectOptional.isEmpty())
			return Optional.empty();
		var workSubject = workSubjectOptional.get();
		
		reservedWorkSubjects.add(workSubject);
		var reservedWork = ReservedWork.ofOnSuccessfulOnClose(workSubject, this::onSuccess, this::onClose);
		return Optional.of(reservedWork);
	}
	
	@API
	public boolean isEmpty()
	{
		if(reservedWorkSubjects.size() > 0)
			return false;
		if(workQueue.size() > 0)
			return false;
		
		return true;
	}
	
	
	// WORK
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
			if(IO_EXCEPTION_BLOCK_DURATION() != null)
				blockedUntil = Instant.now().plus(IO_EXCEPTION_BLOCK_DURATION());
			return Effort.SOME;
		}
	}
	
	@API
	public Effort workIoWarn(IOFunction<T, WorkResult> workAction, String errorMessage)
	{
		return workIo(workAction, (s, e)->logger.warn(errorMessage, s, e));
	}
	
	@API
	public Effort workIoWarn(IoConsumer<T> workAction, String errorMessage)
	{
		return workIo(s->
		{
			workAction.accept(s);
			return WorkResult.worked();
		}, (s, e)->logger.warn(errorMessage, s, e));
	}
	
	
	@API
	public Effort work(Function<T, WorkResult> workAction)
	{
		return workIo(workAction::apply, null);
	}
	
	@API
	public Effort work(Consumer<T> workAction)
	{
		return work(s->
		{
			workAction.accept(s);
			return WorkResult.worked();
		});
	}
	
	
	// RESERVED WORK
	protected void onSuccess(ReservedWork<T> work)
	{
		// nothing in base impl
	}
	
	protected void onClose(ReservedWork<T> work)
	{
		reservedWorkSubjects.remove(work.getSubject());
	}
	
	
	// REFILL
	protected abstract boolean shouldRefill();
	
	protected void refill()
	{
		var moreWork = getMoreWork();
		for(T w : moreWork)
		{
			if(reservedWorkSubjects.contains(w))
				continue;
			
			workQueue.insertIfNotContained(w);
		}
	}
	
	protected abstract Collection<T> getMoreWork();
	
	
	// QUEUE
	protected int getQueueSize()
	{
		return workQueue.size();
	}
	
	private class WorkQueue
	{
		
		private final Queue<T> queue = new LinkedList<>();
		private final Set<T> set = new HashSet<>();
		
		
		// QUEUE
		public synchronized void insertIfNotContained(T work)
		{
			if(set.contains(work))
				return;
			
			queue.add(work);
			set.add(work);
		}
		
		public synchronized Optional<T> poll()
		{
			var work = queue.poll();
			if(work != null)
				set.remove(work);
			
			return Optional.ofNullable(work);
		}
		
		
		// GETTERS
		public int size()
		{
			return set.size();
		}
		
	}
	
}
