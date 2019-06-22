package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map.Entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadUtil
{

	private static final Logger LOGGER = LoggerFactory.getLogger("threadUtil");


	// TIMING
	@API
	public static boolean sleep(Duration duration)
	{
		return sleep(duration.toMillis());
	}

	@API
	public static boolean sleep(long ms)
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

	@API
	public static boolean join(Thread thread)
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


	// SYNCHRONIZATION
	@API
	public static boolean wait(Object object)
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

	@API
	public static void notifyAll(Object object)
	{
		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized(object)
		{
			object.notifyAll();
		}
	}


	// THREAD CREATION
	@API
	public static Thread createThread(Runnable runnable, String threadName)
	{
		return createThread(runnable, threadName, false);
	}

	@API
	public static Thread createDaemonThread(Runnable runnable, String threadName)
	{
		return createThread(runnable, threadName, true);
	}

	private static Thread createThread(Runnable runnable, String threadName, boolean daemon)
	{
		Thread thread = new Thread(runnable);
		thread.setName(threadName);
		thread.setDaemon(daemon);
		logUncaughtExceptions(thread);

		return thread;
	}


	@API
	public static Thread createAndStartThread(Runnable runnable, String threadName)
	{
		return createAndStartThread(runnable, threadName, false);
	}

	@API
	public static Thread createAndStartDaemonThread(Runnable runnable, String threadName)
	{
		return createAndStartThread(runnable, threadName, true);
	}

	private static Thread createAndStartThread(Runnable runnable, String threadName, boolean daemon)
	{
		Thread thread = createThread(runnable, threadName, daemon);
		thread.start();

		return thread;
	}


	@API
	public static Thread runDelayed(Runnable run, long ms)
	{
		Runnable delayed = ()->
		{
			sleep(ms);
			run.run();
		};

		return createAndStartThread(delayed, "delayedTask");
	}


	// SHUTDOWN HOOKS
	@API
	public static void registerShutdownHook(Runnable shutdownHook)
	{
		registerShutdownHook(shutdownHook, "shutdownHook");
	}

	@API
	public static void registerShutdownHook(Runnable shutdownHook, String shutdownHookName)
	{
		Thread shutdownHookThread = createThread(shutdownHook, shutdownHookName);
		Runtime.getRuntime().addShutdownHook(shutdownHookThread);
	}


	// EXCEPTIONS
	@API
	public static void logUncaughtExceptions(Thread thread)
	{
		thread.setUncaughtExceptionHandler((t, e)->
		{
			if(e instanceof ThreadDeath)
				return;

			LOGGER.error("uncaught exception in thread {}", t, e);

			if(e instanceof OutOfMemoryError)
			{
				dumpAllThreads();
				System.exit(-1);
			}
		});
	}


	// DEBUGGING
	@API
	public static String getAllThreadsDump()
	{
		StringBuilder threadDump = new StringBuilder();

		for(Entry<Thread, StackTraceElement[]> threadEntry : Thread.getAllStackTraces().entrySet())
		{
			Thread thread = threadEntry.getKey();
			threadDump
					.append("Thread: ")
					.append(thread)
					.append(", id: ")
					.append(thread.getId())
					.append(", daemon: ")
					.append(thread.isDaemon())
					.append("\n");

			for(StackTraceElement stackTraceElement : threadEntry.getValue())
				threadDump.append("    ").append(stackTraceElement.toString()).append("\n");
		}

		return threadDump.toString();
	}

	@API
	public static void dumpAllThreads()
	{
		LOGGER.info("Global thread dump:\n{}", getAllThreadsDump());
	}

}
