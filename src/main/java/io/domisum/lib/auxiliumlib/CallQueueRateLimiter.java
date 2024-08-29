package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class CallQueueRateLimiter
	extends RateLimiter
{
	
	// CONFIGURATION
	private final Duration timeframe;
	private final int maxCalls;
	
	// STATUS
	private final Queue<Instant> calls = new LinkedBlockingQueue<>();
	
	
	// INIT
	@API
	public CallQueueRateLimiter(Duration timeframe, int maxCalls)
	{
		this(timeframe, maxCalls, false);
	}
	
	@API
	public static CallQueueRateLimiter spreadStart(Duration timeframe, int maxCalls)
	{
		return new CallQueueRateLimiter(timeframe, maxCalls, true);
	}
	
	private CallQueueRateLimiter(Duration timeframe, int maxCalls, boolean spreadStart)
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
	@Override
	public boolean isAvailable()
	{
		clean();
		return calls.size() < maxCalls;
	}
	
	@Override
	public synchronized boolean tryAcquire()
	{
		boolean available = isAvailable();
		if(available)
			calls.add(Instant.now());
		return available;
	}
	
	private synchronized void clean()
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
