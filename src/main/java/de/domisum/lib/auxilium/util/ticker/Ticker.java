package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@API
public abstract class Ticker
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final Duration tickInterval;
	private final String threadName;

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
		logger.info("Starting {}...", getClass().getSimpleName());

		tickThreadRunning = true;
		tickThread = ThreadUtil.createAndStartThread(this::run, threadName);

		logger.info("Starting {} complete", getClass().getSimpleName());
	}

	@API public synchronized void stop()
	{
		logger.info("Stopping {}...", getClass().getSimpleName());

		requestStop();
		waitForStop();

		logger.info("Stopping {} complete", getClass().getSimpleName());
	}

	@API public synchronized void requestStop()
	{
		if(tickThread == null)
			return;
		logger.info("Requesting stop of {}...", getClass().getSimpleName());

		tickThreadRunning = false;
		tickThread.interrupt();
	}

	@API public synchronized void waitForStop()
	{
		if(tickThread == null)
			return;
		logger.info("Waiting for stop of {}...", getClass().getSimpleName());

		if(Thread.currentThread() != tickThread)
			ThreadUtil.join(tickThread);
		tickThread = null;
		logger.info("Waiting for stop of {} complete", getClass().getSimpleName());
	}


	private void run()
	{
		while(tickThreadRunning)
		{
			try
			{
				tick();
			}
			catch(RuntimeException e)
			{
				logger.error("Exception occured during tick", e);
			}

			if(tickThreadRunning)
				ThreadUtil.sleep(tickInterval.toMillis());
		}
	}

	protected abstract void tick();

}
