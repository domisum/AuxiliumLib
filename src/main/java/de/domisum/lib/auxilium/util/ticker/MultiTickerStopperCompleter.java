package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultiTickerStopperCompleter
{

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTickerStopperCompleter.class);


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
		LOGGER.info("Stopping and waiting for completion for {} tickers simultaneously...", tickers.size());

		var waitThreads = new HashSet<Thread>();
		for(var ticker : tickers)
		{
			Runnable run = hard ? ticker::stopHard : ticker::stopSoft;
			var thread = ThreadUtil.createAndStartThread(run, ticker.getName()+"-stop");
			waitThreads.add(thread);
		}

		for(var waitThread : waitThreads)
			ThreadUtil.join(waitThread);

		LOGGER.info("All tickers were stopped and completed");
	}

}
