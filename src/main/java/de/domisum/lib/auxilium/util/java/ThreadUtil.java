package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;

public class ThreadUtil
{

	@API public static boolean sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
			return true;
		}
		catch(InterruptedException e)
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
		catch(InterruptedException e)
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
		catch(InterruptedException e)
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


	@API public static Thread runAsync(Runnable run, String description)
	{
		Thread thread = new Thread(run);
		thread.setName("asyncTask-"+description);
		thread.start();

		return thread;
	}

	@API public static Thread runDelayed(Runnable run, long ms)
	{
		Runnable delay = ()->
		{
			sleep(ms);
			run.run();
		};

		Thread thread = new Thread(delay);
		thread.setName("delayedTask");
		thread.start();

		return thread;
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

}
