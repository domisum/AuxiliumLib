package io.domisum.lib.auxiliumlib.time.ratelimit;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;

import java.time.Duration;

public class TrickleRateLimiter
	extends RateLimiter
{
	
	// CONFIGURATION
	private double perSecond;
	private final double maxAccumulation;
	
	// STATE
	private long balanceTimeEpochMs = System.currentTimeMillis();
	private double balance;
	
	
	// INIT
	private TrickleRateLimiter(double perSecond, double maxAccumulation)
	{
		this.perSecond = perSecond;
		this.maxAccumulation = MathUtil.clampLower(2, maxAccumulation);
		this.balance = -perSecond * 5; // slow start
	}
	
	@API
	public TrickleRateLimiter(double count, Duration timeframe, double maxAccumulation)
	{
		this(count * 1000d / timeframe.toMillis(), maxAccumulation);
	}
	
	
	// INTERFACE: ABSTRACT
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
	
	
	// INTERFACE
	@API
	public synchronized void setPerMinute(double perMinute)
	{
		updateBalance();
		perSecond = perMinute / 60;
	}
	
	@API
	public double getPerMinute()
	{
		return perSecond * 60;
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
