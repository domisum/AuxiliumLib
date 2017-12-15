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
		return "[name="+this.name+";active="+isActive()+";elapsedMs="+MathUtil.round(getElapsedMilli(), 3)+"]";
	}


	// GETTERS
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

}
