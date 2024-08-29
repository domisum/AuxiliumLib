package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TrickleRateLimiter
	extends RateLimiter
{
	
	// CONFIGURATION
	private final double perSecond;
	private final double maxAccumulation;
	
	// STATE
	private long balanceTimeEpochMs = System.currentTimeMillis();
	private double balance = 0;
	
	
	// INIT
	@API
	public static TrickleRateLimiter perSecondAndAccLimit(double perSecond, double maxAccumulation)
	{
		return new TrickleRateLimiter(perSecond, maxAccumulation);
	}
	
	@API
	public static TrickleRateLimiter perMinute(double perMinute)
	{
		return new TrickleRateLimiter(perMinute / 60, perMinute / 2);
	}
	
	
	// INTERFACE
	@Override
	public boolean isAvailable()
	{
		updateBalance();
		return balance >= 1;
	}
	
	@Override
	public synchronized boolean tryAcquire()
	{
		if(isBlocking())
			return false;
		balance -= 1;
		return true;
	}
	
	public synchronized Duration forceAcquireAndReturnDelay()
	{
		updateBalance();
		
		balance -= 1;
		if(balance >= 0)
			return Duration.ZERO;
		long untilBalanceZeroMs = Math.round(balance / perSecond * -1000);
		return Duration.ofMillis(untilBalanceZeroMs);
	}
	
	
	// INTERNAL
	private synchronized void updateBalance()
	{
		long nowMs = System.currentTimeMillis();
		long sinceMs = nowMs - balanceTimeEpochMs;
		
		balanceTimeEpochMs = nowMs;
		balance += sinceMs / 1000d * perSecond;
		balance = MathUtil.clampUpper(balance, maxAccumulation);
	}
	
}
