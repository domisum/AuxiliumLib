package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.display.DurationDisplay;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.time.DurationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@API
public abstract class Ticker
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final Duration TIMEOUT_DEFAULT = null;

	// SETTINGS
	private final Duration tickInterval;
	private final String threadName;

	@Getter
	@Setter
	private Duration timeout = TIMEOUT_DEFAULT;

	// STATUS
	@Getter(AccessLevel.PROTECTED)
	private Thread tickThread;
	private boolean tickThreadRunning;
	private Instant lastTickStart;
	private Thread watchdogThread;


	// INIT
	@API
	protected Ticker(Duration tickInterval)
	{
		this(tickInterval, "ticker");
	}

	@API
	protected Ticker(Duration tickInterval, String threadName)
	{
		this.tickInterval = tickInterval;
		this.threadName = threadName;
	}


	// GETTERS
	@API
	public synchronized boolean isRunning()
	{
		return tickThreadRunning;
	}


	// CONTROL
	@API
	public synchronized void start()
	{
		if(tickThread != null)
			return;
		logger.info("Starting ticker {}...", getTickerName());

		tickThreadRunning = true;
		tickThread = ThreadUtil.createAndStartThread(this::run, threadName);
		watchdogThread = ThreadUtil.createAndStartDaemonThread(this::watchdogRun, "watchDog-"+threadName);
	}

	@API
	public synchronized void requestAndWaitForStop()
	{
		logger.info("Stopping ticker {}...", getTickerName());
		requestStop();
		waitForStop();
		logger.info("Stopped ticker {}", getTickerName());
	}

	@API
	public synchronized void requestStop()
	{
		if(tickThread == null)
			return;
		logger.info("Requesting stop of ticker {}...", getTickerName());

		tickThreadRunning = false;
	}

	@API
	public synchronized void waitForStop()
	{
		if(tickThread == null)
			return;
		if(tickThreadRunning)
			throw new IllegalStateException("can't wait for stop of ticker if request stop hasn't been called");

		logger.info("Waiting for stop of ticker {}...", getTickerName());

		if(Thread.currentThread() != tickThread)
			ThreadUtil.join(tickThread);
		tickThread = null;
		lastTickStart = null;

		watchdogThread.interrupt();
		watchdogThread = null;

		logger.info("Stopped ticker {}", getTickerName());
	}


	// WATCHDOG
	private void watchdogRun()
	{
		while(!Thread.interrupted())
		{
			watchdogTick();
			ThreadUtil.sleep(Duration.ofMillis(100));
		}
	}

	private synchronized void watchdogTick()
	{
		// get local references to avoid impact of changes in variables during run of method
		Instant lastTickStart = this.lastTickStart;
		Duration timeout = this.timeout;

		if(lastTickStart == null)
			return;
		if(timeout == null)
			return;

		if(DurationUtil.isOlderThan(lastTickStart, timeout))
			timeout(timeout);
	}

	private synchronized void timeout(Duration timeout)
	{
		logger.error(
				"Ticker {} timed out (after {}). Current stacktrace:\n{}",
				getTickerName(),
				DurationDisplay.of(timeout),
				ThreadUtil.getThreadToString(tickThread)
		);

		boolean startAgain = tickThreadRunning;

		tickThreadRunning = false;
		tickThread.interrupt();
		ThreadUtil.tryStop(tickThread);
		tickThread = null;
		lastTickStart = null;

		watchdogThread.interrupt();
		watchdogThread = null;

		if(startAgain)
			start();
	}


	// TICK
	private void run()
	{
		while(tickThreadRunning)
		{
			lastTickStart = Instant.now();
			tickCaught();
			lastTickStart = null;

			if(tickThreadRunning)
				ThreadUtil.sleep(tickInterval);
		}
	}

	private void tickCaught()
	{
		try
		{
			tick();
		}
		catch(RuntimeException e)
		{
			logger.error("Exception occured during tick", e);
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
