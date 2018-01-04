package de.domisum.lib.auxilium;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RunOnce implements Runnable
{

	// DEPENDENCIES
	private final Runnable toRun;

	// STATUS
	private transient volatile boolean hasRun = false;
	private final transient Object hasRunLock = new Object();


	// RUN
	@Override public void run()
	{
		synchronized(hasRunLock)
		{
			if(hasRun)
				return;
			hasRun = true;
		}

		toRun.run();
	}

}
