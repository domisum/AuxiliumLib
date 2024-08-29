package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.work.reserver.ReservedWork;
import io.domisum.lib.auxiliumlib.work.reserver.WorkReserver;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public abstract class BatchCooldownWorkReserver<T>
	extends WorkReserver<T>
{
	
	// STATE
	private final Queue<T> batchQueue = new LinkedList<>();
	private Instant nextBatchLockedUntil = Instant.MIN;
	private final Queue<T> failedQueue = new LinkedList<>();
	
	
	// CONSTANT METHODS
	protected Duration BATCH_COOLDOWN()
	{
		return Duration.ofMinutes(5);
	}
	
	protected boolean RETRY_FAILED_SUBJECTS()
	{
		return true;
	}
	
	
	// IMPLEMENTATION
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		if(batchQueue.isEmpty())
			if(TimeUtil.isInPast(nextBatchLockedUntil))
			{
				nextBatchLockedUntil = Instant.now().plus(BATCH_COOLDOWN());
				failedQueue.clear();
				for(T s : getNextBatch())
					if(!reservedSubjects.contains(s))
						batchQueue.add(s);
			}
		
		return Optional.ofNullable(
			batchQueue.poll()).or(() -> Optional.ofNullable(
			failedQueue.poll()));
	}
	
	@Override
	protected void onFail(ReservedWork<T> work)
	{
		if(RETRY_FAILED_SUBJECTS())
			failedQueue.add(work.getSubject());
	}
	
	
	// OVERRIDE THIS
	protected abstract Collection<T> getNextBatch();
	
}
