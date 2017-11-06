package de.domisum.lib.auxilium.util.ticker;

import de.domisum.lib.auxilium.util.java.ThreadUtil;

import java.time.Duration;

public abstract class Ticker
{

	// SETTINGS
	private Duration tickInterval;
	private String threadName;

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
		if(this.tickThread != null)
			return;

		this.tickThread = new Thread(this::run);

		this.tickThread.setName(this.threadName);
		this.tickThreadRunning = true;
		this.tickThread.start();
	}

	public synchronized void stop()
	{
		requestStop();
		waitForStop();
	}

	public synchronized void requestStop()
	{
		if(this.tickThread == null)
			return;

		this.tickThreadRunning = false;
		this.tickThread.interrupt();
	}

	public synchronized void waitForStop()
	{
		if(this.tickThread == null)
			return;

		if(Thread.currentThread() != this.tickThread)
			ThreadUtil.join(this.tickThread);
		this.tickThread = null;
	}


	private void run()
	{
		while(this.tickThreadRunning)
		{
			try
			{
				tick();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			if(this.tickThreadRunning)
				ThreadUtil.sleep(this.tickInterval.toMillis());
		}
	}

	protected abstract void tick();

}
