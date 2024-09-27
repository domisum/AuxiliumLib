package io.domisum.lib.auxiliumlib.time.ratelimit;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RateLimiter
{
	
	private final ReentrantLock acquireLock = new ReentrantLock(true);
	private final AtomicInteger waitingCount = new AtomicInteger(0);
	
	
	@API
	public abstract boolean isAvailable();
	
	@API
	public boolean isBlocking() {return !isAvailable();}
	
	
	@API
	public abstract boolean tryAcquire();
	
	@API
	public void blockUntilAcquire()
	{
		try
		{
			waitingCount.incrementAndGet();
			acquireLock.lock();
			while(true)
			{
				if(tryAcquire())
					return;
				ThreadUtil.sleep(Duration.ofMillis(10));
			}
		}
		finally
		{
			acquireLock.unlock();
			waitingCount.decrementAndGet();
		}
	}
	
	@API
	public int getWaitingCount()
	{
		return waitingCount.get();
	}
	
}
