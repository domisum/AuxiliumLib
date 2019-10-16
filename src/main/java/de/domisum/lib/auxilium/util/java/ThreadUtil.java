package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.display.DurationDisplay;
import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadUtil
{

	private static final Logger LOGGER = LoggerFactory.getLogger("threadUtil");
	private static boolean threadDumped = false;


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


	@SuppressWarnings("deprecation")
	@API
	public static void tryDestroy(Thread thread)
	{
		try
		{
			thread.destroy();
		}
		catch(NoSuchMethodError ignored)
		{

		}
	}


	// SHUTDOWN
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

	@API
	public static void scheduleEmergencyExit(Duration delay)
	{
		LOGGER.info("Scheduling emergency exit to run in {}", DurationDisplay.of(delay));

		createAndStartDaemonThread(()->
		{
			sleep(delay);

			LOGGER.error("Shutdown did not complete after {}, forcing exit", DurationDisplay.of(delay));
			System.exit(-1);
		}, "emergencyDelayedExit");
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
				if(!threadDumped)
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
		Set<Long> dumpedThreadsIds = new HashSet<>();

		for(Entry<Thread, StackTraceElement[]> threadEntry : Thread.getAllStackTraces().entrySet())
		{
			Thread thread = threadEntry.getKey();
			if(dumpedThreadsIds.contains(thread.getId()))
				continue;

			threadDump.append(getThreadToString(thread)).append("\n");
			dumpedThreadsIds.add(thread.getId());
		}

		return threadDump.toString();
	}

	@API
	public static String getThreadToString(Thread thread)
	{
		StringBuilder threadStackTrace = new StringBuilder();
		threadStackTrace
				.append(thread)
				.append(", id: ")
				.append(thread.getId())
				.append(", daemon: ")
				.append(thread.isDaemon())
				.append("\n");

		List<String> lines = new ArrayList<>();
		for(StackTraceElement stackTraceElement : thread.getStackTrace())
			lines.add("    "+stackTraceElement.toString());
		threadStackTrace.append(StringUtil.listToString(lines, "\n"));
		if(lines.isEmpty())
			threadStackTrace.append("(no stack trace)");

		return threadStackTrace.toString();
	}

	@API
	public static void dumpAllThreads()
	{
		LOGGER.info("Global thread dump:\n{}", getAllThreadsDump());
	}

}
