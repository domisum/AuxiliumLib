package de.domisum.lib.auxilium.util.time;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.MathUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class ProfilerStopWatch
{

	// PROPERTIES
	private final String name;

	// TEMP
	@Getter private transient long startNano;
	private transient long endNano = -1;


	// OBJECT
	@Override public String toString()
	{
		return "[name="+name+";active="+isActive()+";elapsedMs="+MathUtil.round(getElapsedMilli(), 3)+"]";
	}


	// GETTERS
	@API public boolean isActive()
	{
		return endNano == -1;
	}

	@API public long getElapsedNano()
	{
		return (endNano == -1 ? System.nanoTime() : endNano)-startNano;
	}

	@API public double getElapsedMilli()
	{
		return getElapsedNano()/1000d/1000d;
	}


	// START/STOP
	@API public void start()
	{
		startNano = System.nanoTime();
	}

	@API public void stop()
	{
		if(endNano != -1)
			throw new IllegalStateException("The stopwatch has already been stopped");

		endNano = System.nanoTime();
	}

}
