package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.MathUtil;

@API
public class ProfilerStopWatch
{

	// PROPERTIES
	private String name;
	private long startNano;
	private long endNano = -1;


	// INIT
	@API public ProfilerStopWatch(String name)
	{
		this.name = name;
	}

	@Override public String toString()
	{
		return "[name="+this.name+";active="+isActive()+";elapsedMs="+MathUtil.round(getElapsedMilli(), 3)+"]";
	}


	// START/STOP
	@API public void start()
	{
		this.startNano = System.nanoTime();
	}

	@API public void stop()
	{
		if(this.endNano != -1)
			throw new IllegalStateException("The stopwatch has already been stopped");

		this.endNano = System.nanoTime();
	}


	// GETTERS
	@API public long getStartNano()
	{
		return this.startNano;
	}

	@API public boolean isActive()
	{
		return this.endNano == -1;
	}

	@API public long getElapsedNano()
	{
		return (this.endNano == -1 ? System.nanoTime() : this.endNano)-this.startNano;
	}

	@API public double getElapsedMilli()
	{
		return getElapsedNano()/1000d/1000d;
	}

}
