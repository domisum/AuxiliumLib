package io.domisum.lib.auxiliumlib.contracts.tick;

import java.time.Duration;

public interface IntervalTickable
		extends Tickable
{

	Duration getTickInterval();

}
