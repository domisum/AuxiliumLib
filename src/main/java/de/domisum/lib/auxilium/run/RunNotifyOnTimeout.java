package de.domisum.lib.auxilium.run;

import de.domisum.lib.auxilium.display.DurationDisplay;
import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@RequiredArgsConstructor
public class RunNotifyOnTimeout implements Runnable
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// DEPENDENCIES
	private final Runnable toRun;

	// SETTINGS
	private final Duration timeout;


	// RUN
	@Override public void run()
	{
		Thread threadToWatch = Thread.currentThread();
		Thread watchThread = ThreadUtil.createAndStartThread(()->watchThreadRun(threadToWatch), threadToWatch.getName()+"-TO");

		try
		{
			toRun.run();
		}
		finally
		{
			watchThread.interrupt();
		}
	}

	private void watchThreadRun(Thread threadToWatch)
	{
		Instant startInstant = Instant.now();

		while(!Thread.currentThread().isInterrupted())
		{
			if(Duration.between(startInstant, Instant.now()).compareTo(timeout) > 0)
			{
				String stackTraceString = stackTraceElementsToString(threadToWatch.getStackTrace());

				logger.error("Run timed out ({}); current stacktrace: \n{}", DurationDisplay.of(timeout), stackTraceString);
				break;
			}

			ThreadUtil.sleep(10);
		}
	}


	// UTIL
	private static String stackTraceElementsToString(StackTraceElement[] stackTraceElements)
	{
		String beforeLine = "    ";
		return beforeLine+StringUtil.listToString(Arrays.asList(stackTraceElements), "\n"+beforeLine);
	}

}
