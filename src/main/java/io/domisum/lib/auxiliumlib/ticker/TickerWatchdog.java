package io.domisum.lib.auxiliumlib.ticker;

import com.google.common.collect.Sets;
import io.domisum.lib.auxiliumlib.ticker.Ticker.Ticking;
import io.domisum.lib.auxiliumlib.ticker.Ticker.TickingStatus;
import io.domisum.lib.auxiliumlib.util.java.thread.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class TickerWatchdog
{
	
	// CONSTANTS
	private static final Duration INTERVAL = Duration.ofSeconds(1);
	
	// STATUS
	private static final Set<Ticking> watchedTickings = Sets.newConcurrentHashSet();
	private static Thread thread = null;
	
	
	// CONTROL
	static void watch(Ticking ticking)
	{
		watchedTickings.add(ticking);
		ensureThreadRunning();
	}
	
	private static void ensureThreadRunning()
	{
		if(thread != null)
			return;
		
		thread = ThreadUtil.createAndStartDaemonThread(TickerWatchdog::run, "ticker-watchdog");
	}
	
	
	private static void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			tick();
			ThreadUtil.sleep(INTERVAL);
		}
	}
	
	private static void tick()
	{
		watchedTickings.removeIf(t->t.getStatus() == TickingStatus.DEAD);
		
		for(var ticking : watchedTickings)
			ticking.watchdogTick();
	}
	
}
