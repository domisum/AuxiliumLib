package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;

import java.time.Duration;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RateLimiter
{
	
	private final ReentrantLock acquireLock = new ReentrantLock(true);
	
	
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
		}
	}
	
}
