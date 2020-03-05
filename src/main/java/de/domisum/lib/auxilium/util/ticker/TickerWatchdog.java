package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.ticker.Ticker.Ticking;
import de.domisum.lib.auxilium.util.ticker.Ticker.TickingStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TickerWatchdog
{

	// CONSTANTS
	private static final Duration INTERVAL = Duration.ofSeconds(1);

	// STATUS
	private static final Set<Ticking> watchedTickings = new HashSet<>();
	private static Thread thread = null;


	// CONTROL
	public static synchronized void watch(Ticking ticking)
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

	private static synchronized void tick()
	{
		watchedTickings.removeIf(t->t.getStatus() == TickingStatus.DEAD);

		for(var ticking : watchedTickings)
			ticking.watchdogTick();

		if(watchedTickings.isEmpty())
		{
			thread.interrupt();
			thread = null;
		}
	}

}
