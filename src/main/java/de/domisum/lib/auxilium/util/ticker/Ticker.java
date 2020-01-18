package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.display.DurationDisplay;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.time.DurationUtil;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

@API
public abstract class Ticker
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final Duration TIMEOUT_DEFAULT = null;

	// SETTINGS
	private final String name;
	private final Duration interval;
	@Nullable
	private final Duration timeout;

	// STATUS
	private Thread tickThread;
	private Thread watchdogThread;

	private Status status = Status.READY;
	private Instant lastTickStart;


	// INIT
	@API
	protected Ticker(String name, Duration interval, @Nullable Duration timeout)
	{
		Validate.notNull(name, "name can't be null");
		Validate.notNull(interval, "interval can't be null");
		Validate.isTrue(interval.compareTo(Duration.ZERO) > 0, "interval has to be greater than zero");

		this.name = name;
		this.interval = interval;
		this.timeout = timeout;
	}

	@API
	protected Ticker(String name, Duration interval)
	{
		this(name, interval, TIMEOUT_DEFAULT);
	}


	// GETTERS
	@API
	public synchronized boolean isRunning()
	{
		return status == Status.RUNNING;
	}

	@API
	public Thread getTickThread()
	{
		if(status != Status.RUNNING)
			throw new IllegalStateException("can't get tick thread while not running");

		return tickThread;
	}


	// CONTROL
	@API
	public synchronized void start()
	{
		if(status != Status.READY)
			throw new IllegalStateException("Can't start ticker with status "+status);

		logger.info("Starting ticker {}...", name);
		status = Status.RUNNING;
		startThreads();
	}

	private void startThreads()
	{
		tickThread = ThreadUtil.createAndStartThread(this::run, name);
		watchdogThread = ThreadUtil.createAndStartDaemonThread(this::watchdogRun, "watchDog-"+name);
	}


	@API
	public synchronized void stopAndWaitForCompletion()
	{
		stop(true);
	}

	@API
	public synchronized void stopAndIgnoreCompletion()
	{
		stop(false);
	}

	protected synchronized void stop(boolean waitForCompletion)
	{
		if(status == Status.STOPPED)
			return;
		if(status != Status.RUNNING)
			throw new IllegalStateException("Can't stop ticker with status "+status);

		logger.info("Stopping ticker {} (Waiting for completion: {})...", name, waitForCompletion);
		status = Status.STOPPED;
		watchdogThread.interrupt();

		if(waitForCompletion && (Thread.currentThread() != tickThread))
		{
			ThreadUtil.join(tickThread);
			logger.info("Ticker {} completed", name);
		}
	}


	// TICK
	private void run()
	{
		while(status == Status.RUNNING)
		{
			lastTickStart = Instant.now();
			tickCaught();
			lastTickStart = null;

			if(status == Status.RUNNING)
				ThreadUtil.sleep(interval);
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


	// WATCHDOG
	private void watchdogRun()
	{
		while(!Thread.interrupted())
		{
			watchdogTick();
			ThreadUtil.sleep(Duration.ofSeconds(1));
		}
	}

	private void watchdogTick()
	{
		if(timeout == null)
			return;

		// get local reference to avoid impact of changes in variable during run of method
		Instant lastTickStart = this.lastTickStart;
		if(lastTickStart == null)
			return;

		if(DurationUtil.isOlderThan(lastTickStart, timeout))
			timeout(timeout);
	}

	private void timeout(Duration timeout)
	{
		logger.error(
				"Ticker {} timed out (after {}). Current stacktrace:\n{}",
				name,
				DurationDisplay.of(timeout),
				ThreadUtil.getThreadToString(tickThread)
		);

		tickThread.interrupt();
		ThreadUtil.tryKill(tickThread);
		watchdogThread.interrupt();

		lastTickStart = null;

		if(status == Status.RUNNING)
		{
			logger.info("Starting ticker {} back up...", name);
			startThreads();
		}
	}


	// STATUS
	private enum Status
	{

		READY,
		RUNNING,
		STOPPED

	}

}
