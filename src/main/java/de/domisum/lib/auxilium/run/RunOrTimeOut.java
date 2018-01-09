package de.domisum.lib.auxilium.run;

import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@RequiredArgsConstructor
public class RunOrTimeOut implements Runnable
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// DEPENDENCIES
	private final Runnable toRun;

	// SETTINGS
	private final Duration timeout;


	// RUN
	@Override public void run()
	{
		Instant startInstant = Instant.now();
		Thread runThread = ThreadUtil.createAndStartThread(toRun, Thread.currentThread().getName()+"-TO");

		while(runThread.isAlive())
		{
			if(Duration.between(startInstant, Instant.now()).compareTo(timeout) > 0)
			{
				String stackTraceString = stackTraceElementsToString(runThread.getStackTrace());
				logger.error("Run timed out, killing thread; current stacktrace: {}", "\n"+stackTraceString);

				ThreadUtil.stop(runThread);
				break;
			}

			ThreadUtil.sleep(1);
		}
	}


	// UTIL
	private static String stackTraceElementsToString(StackTraceElement[] stackTraceElements)
	{
		String beforeLine = "    ";
		return beforeLine+StringUtil.listToString(Arrays.asList(stackTraceElements), "\n"+beforeLine);
	}

}
