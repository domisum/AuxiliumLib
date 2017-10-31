package de.domisum.lib.auxilium.util.java;

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
	protected Ticker(Duration tickInterval)
	{
		this(tickInterval, "ticker");
	}

	protected Ticker(Duration tickInterval, String threadName)
	{
		this.tickInterval = tickInterval;
		this.threadName = threadName;
	}


	// TICK
	public synchronized void start()
	{
		if(this.tickThread != null)
			return;

		this.tickThread = new Thread(()->{
			while(this.tickThreadRunning)
			{
				tick();
				ThreadUtil.sleep(this.tickInterval.toMillis());
			}
		});

		this.tickThread.setName(this.threadName);
		this.tickThreadRunning = true;
		this.tickThread.start();
	}

	public synchronized void stop()
	{
		if(this.tickThread == null)
			return;

		this.tickThreadRunning = false;
		this.tickThread.interrupt();
		if(Thread.currentThread() != this.tickThread)
			ThreadUtil.join(this.tickThread);
		this.tickThread = null;
	}


	protected abstract void tick();

}
