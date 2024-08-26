package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import io.domisum.lib.auxiliumlib.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter
{
	
	// CONFIGURATION
	private final Duration timeframe;
	private final int maxCalls;
	
	// STATUS
	private final Queue<Instant> calls = new LinkedBlockingQueue<>();
	private final ReentrantLock acquireLock = new ReentrantLock(true);
	
	
	// INIT
	@API
	public RateLimiter(Duration timeframe, int maxCalls)
	{
		this(timeframe, maxCalls, false);
	}
	
	@API
	public static RateLimiter spreadStart(Duration timeframe, int maxCalls)
	{
		return new RateLimiter(timeframe, maxCalls, true);
	}
	
	private RateLimiter(Duration timeframe, int maxCalls, boolean spreadStart)
	{
		this.timeframe = timeframe;
		this.maxCalls = maxCalls;
		
		// it is possible that rate limit is hit from previous run before this startup,
		// so err on safe side and block at start
		for(int i = 0; i < maxCalls; i++)
			calls.add(Instant.now());
		
		var interval = timeframe.dividedBy(maxCalls);
		if(spreadStart)
			for(int i = 0; i < maxCalls; i++)
				calls.add(Instant.now().plus(interval.multipliedBy(i)));
	}
	
	
	// RATE LIMIT
	@API
	public synchronized boolean available()
	{
		clean();
		return calls.size() < maxCalls;
	}
	
	@API
	public synchronized boolean tryAcquire()
	{
		boolean available = available();
		if(available)
			calls.add(Instant.now());
		return available;
	}
	
	@API
	public Duration blockUntilAcquire()
	{
		try
		{
			var waitStart = Instant.now();
			acquireLock.lock();
			while(true)
			{
				if(tryAcquire())
					return TimeUtil.since(waitStart);
				ThreadUtil.sleep(Duration.ofMillis(10));
			}
		}
		finally
		{
			acquireLock.unlock();
		}
	}
	
	private void clean()
	{
		while(true)
		{
			var first = calls.peek();
			if(first != null && TimeUtil.isOlderThan(first, timeframe))
				calls.remove();
			else
				break;
		}
	}
	
}
