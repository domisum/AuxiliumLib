package de.domisum.lib.auxilium.contracts.tick;

import java.time.Duration;

public interface IntervalTickable extends Tickable
{

	Duration getTickInterval();

}
