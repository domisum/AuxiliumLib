package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public abstract class Ticker
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final Duration tickInterval;
	private final String threadName;

	// STATUS
	private Thread tickThread;
	private boolean tickThreadRunning;


	// INIT
	public Ticker(Duration tickInterval)
	{
		this(tickInterval, "ticker");
	}

	public Ticker(Duration tickInterval, String threadName)
	{
		this.tickInterval = tickInterval;
		this.threadName = threadName;
	}


	// TICK
	public synchronized void start()
	{
		if(tickThread != null)
			return;

		tickThread = new Thread(this::run);

		tickThread.setName(threadName);
		tickThreadRunning = true;
		tickThread.start();
	}

	public synchronized void stop()
	{
		requestStop();
		waitForStop();
	}

	public synchronized void requestStop()
	{
		if(tickThread == null)
			return;

		tickThreadRunning = false;
		tickThread.interrupt();
	}

	public synchronized void waitForStop()
	{
		if(tickThread == null)
			return;

		if(Thread.currentThread() != tickThread)
			ThreadUtil.join(tickThread);
		tickThread = null;
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
