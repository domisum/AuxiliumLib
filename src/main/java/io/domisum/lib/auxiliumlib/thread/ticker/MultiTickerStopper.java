package io.domisum.lib.auxiliumlib.thread.ticker;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultiTickerStopper
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTickerStopper.class);
	
	
	// SETTINGS
	@Setter private static Duration dumpAfter = null;
	
	
	@API
	public static void stopSoft(Collection<? extends Ticker> tickers)
	{
		stop(tickers, false);
	}
	
	@API
	public static void stopHard(Collection<? extends Ticker> tickers)
	{
		stop(tickers, true);
	}
	
	private static void stop(Collection<? extends Ticker> tickers, boolean hard)
	{
		LOGGER.info("Stopping and waiting for completion of {} tickers simultaneously...", tickers.size());
		var stopThreads = startStopThreads(tickers, hard);
		
		var waitStart = Instant.now();
		var lastProgressDisplay = Instant.now();
		boolean dumped = false;
		while(stopThreads.size() > 0)
		{
			ThreadUtil.sleep(Duration.ofSeconds(1));
			stopThreads.keySet().removeIf(t -> !t.isAlive());
			
			if(TimeUtil.isOlderThan(lastProgressDisplay, Duration.ofSeconds(10)))
			{
				lastProgressDisplay = Instant.now();
				var waitDurationDisplay = DurationDisplay.of(TimeUtil.since(waitStart));
				LOGGER.info("Tickers remaining: {}/{} after {}", stopThreads.size(), tickers.size(), waitDurationDisplay);
			}
			
			if(!dumped && dumpAfter != null && TimeUtil.isOlderThan(waitStart, dumpAfter))
			{
				for(var ticker : stopThreads.values())
					ticker.dumpStackTrace("Ticker hasn't stopped yet. Dumping stack trace:\n{}");
				dumped = true;
			}
		}
		LOGGER.info("...All tickers were stopped and completed");
	}
	
	private static Map<Thread, Ticker> startStopThreads(Collection<? extends Ticker> tickers, boolean hard)
	{
		var waitThreads = new HashMap<Thread, Ticker>();
		for(var ticker : tickers)
		{
			Runnable run = hard ?
				ticker::stopHard :
				ticker::stopSoft;
			var thread = ThreadUtil.createAndStartThread(run, ticker.getName() + "-stop");
			waitThreads.put(thread, ticker);
		}
		return waitThreads;
	}
	
}
