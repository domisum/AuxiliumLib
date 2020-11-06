package io.domisum.lib.auxiliumlib.work;

import io.domisum.lib.auxiliumlib.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

public abstract class EndlessWorkDistributor<T>
	extends WorkDistributor<T>
{
	
	// STATE
	private Instant lastUnsuccessfulRefill = Instant.EPOCH;
	
	
	// CONSTANT METHODS
	protected abstract int TARGET_QUEUE_LENGTH();
	
	protected abstract Duration EMPTY_REFILL_COOLDOWN_DURATION();
	
	
	// REFILL
	@Override
	protected boolean shouldRefill()
	{
		if(getQueueSize() >= TARGET_QUEUE_LENGTH())
			return false;
		
		if(TimeUtil.isYoungerThan(lastUnsuccessfulRefill, EMPTY_REFILL_COOLDOWN_DURATION()))
			return false;
		
		return true;
	}
	
	@Override
	protected final Collection<T> getMoreWork()
	{
		var moreWork = getMoreEndlessWork();
		
		if(moreWork.isEmpty())
			lastUnsuccessfulRefill = Instant.now();
		
		return moreWork;
	}
	
	protected abstract Collection<T> getMoreEndlessWork();
	
}
