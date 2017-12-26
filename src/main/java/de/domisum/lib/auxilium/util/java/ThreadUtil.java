package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadUtil
{

	private static final Logger LOGGER = LoggerFactory.getLogger("threadUtil");


	@API public static boolean sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
			return true;
		}
		catch(InterruptedException ignored)
		{
			Thread.currentThread().interrupt();
			return false;
		}
	}

	@API public static boolean join(Thread thread)
	{
		try
		{
			thread.join();
			return true;
		}
		catch(InterruptedException ignored)
		{
			Thread.currentThread().interrupt();
			return false;
		}
	}


	@API public static boolean wait(Object object)
	{
		try
		{
			//noinspection SynchronizationOnLocalVariableOrMethodParameter
			synchronized(object)
			{
				object.wait();
			}
			return true;
		}
		catch(InterruptedException ignored)
		{
			return false;
		}
	}

	@API public static void notifyAll(Object object)
	{
		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized(object)
		{
			object.notifyAll();
		}
	}


	@API public static Thread createAndStartThread(Runnable runnable, String threadName)
	{
		Thread thread = new Thread(runnable);
		thread.setName(threadName);
		logUncaughtExceptions(thread);

		thread.start();
		return thread;
	}

	@API public static Thread runDelayed(Runnable run, long ms)
	{
		Runnable delayed = ()->
		{
			sleep(ms);
			run.run();
		};

		return createAndStartThread(delayed, "delayedTask");
	}


	@API public static void addShutdownHook(Runnable shutdownHook)
	{
		addShutdownHook(shutdownHook, "shutdownHook");
	}

	@API public static void addShutdownHook(Runnable shutdownHook, String shutdownHookName)
	{
		Thread shutdownHookThread = new Thread(shutdownHook);
		shutdownHookThread.setName(shutdownHookName);

		Runtime.getRuntime().addShutdownHook(shutdownHookThread);
	}


	@API public static void logUncaughtExceptions(Thread thread)
	{
		thread.setUncaughtExceptionHandler((t, e)->LOGGER.error("uncaught exception in thread "+t, e));
	}

}
