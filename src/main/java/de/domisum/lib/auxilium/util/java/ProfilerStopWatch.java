package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;

@APIUsage
public class ProfilerStopWatch
{

	// PROPERTIES
	private String name;
	private long startNano;
	private long endNano = -1;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage public ProfilerStopWatch(String name)
	{
		this.name = name;
	}

	@Override public String toString()
	{
		return "[name="+this.name+";active="+isActive()+";elapsedMs="+MathUtil.round(getElapsedMilli(), 3)+"]";
	}


	// -------
	// START/STOP
	// -------
	@APIUsage public void start()
	{
		this.startNano = System.nanoTime();
	}

	@APIUsage public void stop()
	{
		if(this.endNano != -1)
			throw new IllegalStateException("The stopwatch has already been stopped");

		this.endNano = System.nanoTime();
	}


	// -------
	// GETTERS
	// -------
	@APIUsage public long getStartNano()
	{
		return this.startNano;
	}

	@APIUsage public boolean isActive()
	{
		return this.endNano == -1;
	}

	@APIUsage public long getElapsedNano()
	{
		return (this.endNano == -1 ? System.nanoTime() : this.endNano)-this.startNano;
	}

	@APIUsage public double getElapsedMilli()
	{
		return getElapsedNano()/1000d/1000d;
	}

}
