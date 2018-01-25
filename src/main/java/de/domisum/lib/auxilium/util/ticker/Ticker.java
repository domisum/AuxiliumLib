package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.run.RunNotifyOnTimeout;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

@API
public abstract class Ticker
{

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// CONSTANTS
	private static final Duration TIMEOUT_DEFAULT = Duration.ofMinutes(10);


	// SETTINGS
	private final Duration tickInterval;
	private final String threadName;

	@Getter @Setter private Duration timeout = TIMEOUT_DEFAULT;

	// STATUS
	@Getter(AccessLevel.PROTECTED) private Thread tickThread;
	private boolean tickThreadRunning;


	// INIT
	@API protected Ticker(Duration tickInterval)
	{
		this(tickInterval, "ticker");
	}

	@API protected Ticker(Duration tickInterval, String threadName)
	{
		this.tickInterval = tickInterval;
		this.threadName = threadName;
	}


	// TICK
	@API public synchronized void start()
	{
		if(tickThread != null)
			return;
		logger.info("Starting ticker {}...", getTickerName());

		tickThreadRunning = true;
		tickThread = ThreadUtil.createAndStartThread(this::run, threadName);

		logger.info("Starting ticker {} done", getTickerName());
	}

	@API public synchronized void requestAndWaitForStop()
	{
		logger.info("Stopping ticker {}...", getTickerName());

		requestStop();
		waitForStop();

		logger.info("Stopped ticker {}", getTickerName());
	}

	@API public synchronized void requestStop()
	{
		if(tickThread == null)
			return;
		logger.info("Requesting stop of ticker {}...", getTickerName());

		tickThreadRunning = false;
		tickThread.interrupt();
	}

	@API public synchronized void waitForStop()
	{
		if(tickThread == null)
			return;
		if(tickThreadRunning)
			throw new IllegalStateException("can't wait for stop of ticker if request stop hasn't been called");

		logger.info("Waiting for stop of ticker {}...", getTickerName());

		if(Thread.currentThread() != tickThread)
			ThreadUtil.join(tickThread);
		tickThread = null;
		logger.info("Stopped ticker {}", getTickerName());
	}


	private void run()
	{
		while(tickThreadRunning)
		{
			if(timeout != null)
				tickWithTimeout();
			else
				tickCaught();

			if(tickThreadRunning)
				ThreadUtil.sleep(tickInterval.toMillis());
		}
	}

	private void tickWithTimeout()
	{
		new RunNotifyOnTimeout(this::tickCaught, timeout).run();
	}

	private void tickCaught()
	{
		try
		{
			tick();
		}
		catch(RuntimeException e)
		{
			logger.error("Exception occured during tick\n", e);
		}
	}

	protected abstract void tick();


	// UTIL
	private String getTickerName()
	{
		if(!Objects.equals(threadName, "ticker"))
			return threadName;

		return getClass().getSimpleName();
	}

}
