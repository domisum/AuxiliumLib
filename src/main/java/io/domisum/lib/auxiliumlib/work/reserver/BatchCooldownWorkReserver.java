package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.work.WorkReserver;

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
	private final Queue<T> queue = new LinkedList<>();
	private Instant nextBatchLockedUntil = Instant.MIN;
	
	
	// CONSTANT METHODS
	protected Duration BATCH_COOLDOWN()
	{
		return Duration.ofMinutes(5);
	}
	
	
	// IMPLEMENTATION
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		if(queue.isEmpty())
			if(TimeUtil.isInPast(nextBatchLockedUntil))
			{
				nextBatchLockedUntil = Instant.now().plus(BATCH_COOLDOWN());
				for(T s : getNextBatch())
					if(!reservedSubjects.contains(s))
						queue.add(s);
			}
		
		return Optional.ofNullable(queue.poll());
	}
	
	
	// OVERRIDE THIS
	protected abstract Collection<T> getNextBatch();
	
}
