package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;

public class ThreadUtil
{

	@API public static boolean sleep(long ms)
	{
		return sleep(ms, 0);
	}

	@API public static boolean sleepNs(long ns)
	{
		long ms = ns/(1000*1000);
		long nsOnly = ns%(1000*1000);

		return sleep(ms, nsOnly);
	}

	@API public static boolean sleep(long ms, long ns)
	{
		try
		{
			Thread.sleep(ms, (int) ns);
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
		Runnable delay = ()->{
			sleep(ms);
			run.run();
		};

		Thread thread = new Thread(delay);
		thread.setName("delayedTask");
		thread.start();

		return thread;
	}

}
