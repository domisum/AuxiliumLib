package de.domisum.lib.auxilium.util.java.debug;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;

@APIUsage
public class ProfilerStopWatch
{

	// PROPERTIES
	private String name;
	private long startNano;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public ProfilerStopWatch(String name)
	{
		this.name = name;
		this.startNano = System.nanoTime();
	}

	public String toString()
	{
		return "[name="+this.name+";elapsedMs="+MathUtil.round(getElapsedMilli(), 5)+"]";
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public long getStartNano()
	{
		return this.startNano;
	}

	@APIUsage
	public long getElapsedNano()
	{
		return System.nanoTime()-this.startNano;
	}

	@APIUsage
	public double getElapsedMilli()
	{
		return getElapsedNano()/1000d/1000d;
	}

}
