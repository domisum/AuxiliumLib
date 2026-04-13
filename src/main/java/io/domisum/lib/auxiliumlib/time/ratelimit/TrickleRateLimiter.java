package io.domisum.lib.auxiliumlib.time.ratelimit;

import com.google.common.annotations.VisibleForTesting;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

public class TrickleRateLimiter
	extends RateLimiter
{
	
	// CONFIGURATION
	private double perSecond;
	private final double maxAccumulation;
	
	// STATE
	private final Supplier<Instant> clock;
	private Instant balanceTime;
	private double balance;
	
	
	// INIT
	private TrickleRateLimiter(double perSecond, double maxAccumulation, Supplier<Instant> clock)
	{
		this.perSecond = perSecond;
		this.maxAccumulation = MathUtil.clampLower(2, maxAccumulation);
		this.clock = clock == null ? Instant::now : clock;
		this.balanceTime = this.clock.get();
		this.balance = -perSecond * 5; // slow start
	}
	
	@API
	public TrickleRateLimiter(double count, Duration timeframe, double maxAccumulation)
	{this(count / TimeUtil.getSecondsDecimal(timeframe), maxAccumulation, null);}
	
	@VisibleForTesting
	public TrickleRateLimiter(double count, Duration timeframe, double maxAccumulation, Supplier<Instant> clock)
	{this(count / TimeUtil.getSecondsDecimal(timeframe), maxAccumulation, clock);}
	
	
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
	
	@API
	public synchronized void setPerMinute(double perMinute)
	{
		updateBalance();
		perSecond = perMinute / 60;
	}
	
	@API
	public double getPerMinute() {return perSecond * 60;}
	
	
	// INTERNAL
	private synchronized void updateBalance()
	{
		var now = clock.get();
		long sinceMs = Duration.between(balanceTime, now).toMillis();
		balanceTime = now;
		
		balance += sinceMs / 1000d * perSecond;
		balance = MathUtil.clampUpper(balance, maxAccumulation);
	}
	
}
