package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MultiTickerStopperCompleter
{

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTickerStopperCompleter.class);


	@API
	public static void stopAndWaitForCompletion(Collection<? extends Ticker> tickers)
	{
		LOGGER.info("Stopping and waiting for completion for {} tickers simultaneously...", tickers.size());

		Set<Thread> waitThreads = new HashSet<>();
		for(var ticker : tickers)
		{
			Runnable run = ticker::stopAndWaitForCompletion;
			var thread = ThreadUtil.createAndStartThread(run, ticker.getName());
			waitThreads.add(thread);
		}

		for(Thread waitThread : waitThreads)
			ThreadUtil.join(waitThread);

		LOGGER.info("All tickers were stopped and completed");
	}

}
