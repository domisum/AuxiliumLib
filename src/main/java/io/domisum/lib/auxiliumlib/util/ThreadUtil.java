package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;

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
	
	
	// SYNCHRONIZATION
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
		var thread = new Thread(runnable);
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
		var thread = createThread(runnable, threadName, daemon);
		thread.start();
		return thread;
	}
	
	
	// KILL THREAD
	@API
	@SuppressWarnings({"deprecation", "ErrorNotRethrown"})
	public static void tryKill(Thread thread)
	{
		try
		{
			thread.stop();
		}
		catch(NoSuchMethodError ignored)
		{
		
		}
	}
	
	
	// SHUTDOWN
	@API
	public static void registerShutdownHook(Runnable shutdownHook, String shutdownHookName)
	{
		Thread shutdownHookThread = createThread(shutdownHook, shutdownHookName);
		Runtime.getRuntime().addShutdownHook(shutdownHookThread);
	}
	
	@API
	public static void scheduleEmergencyExit(Duration delay)
	{
		scheduleEmergencyExit(delay, ()->
		{
			// nothing
		});
	}
	
	
	@API
	public static void scheduleEmergencyExit(Duration delay, Runnable run)
	{
		LOGGER.info("Scheduling emergency exit to run in {}", DurationDisplay.of(delay));
		
		createAndStartDaemonThread(()->
		{
			sleep(delay);
			
			LOGGER.error("Shutdown did not complete after {}, forcing exit", DurationDisplay.of(delay));
			run.run();
			LOGGER.error("Thread dump: {}", getThreadDump());
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
			
			LOGGER.error("Uncaught exception in thread {}", t, e);
			
			if(e instanceof Error)
			{
				LOGGER.error("Encountered error, shutting down...");
				System.exit(-1);
			}
		});
	}
	
	
	// DEBUGGING
	@API
	public static String getThreadDump()
	{
		var threadsAsString = new HashSet<String>();
		for(var thread : Thread.getAllStackTraces().keySet())
			threadsAsString.add(displayThread(thread));
		
		return StringListUtil.list(threadsAsString, "\n\n");
	}
	
	@API
	public static String displayThread(Thread thread)
	{
		var threadToString = new StringBuilder();
		threadToString
			.append(thread)
			.append(", id: ")
			.append(thread.getId())
			.append(", daemon: ")
			.append(thread.isDaemon())
			.append("\n");
		
		var stackTraceLines = new ArrayList<String>();
		for(var stackTraceElement : thread.getStackTrace())
			stackTraceLines.add("    "+stackTraceElement.toString());
		
		if(stackTraceLines.isEmpty())
			threadToString.append("(no stack trace)");
		else
			threadToString.append(StringListUtil.list(stackTraceLines, "\n"));
		
		return threadToString.toString();
	}
	
}
