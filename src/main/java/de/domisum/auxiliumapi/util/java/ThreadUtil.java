package de.domisum.auxiliumapi.util.java;

import de.domisum.auxiliumapi.AuxiliumAPI;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.Bukkit;

public class ThreadUtil
{

	@APIUsage
	public static boolean sleep(long ms)
	{
		return sleep(ms, 0);
	}

	@APIUsage
	public static boolean sleepNs(long ns)
	{
		long ms = ns/(1000*1000);
		long nsOnly = ns%(1000*1000);

		return sleep(ms, nsOnly);
	}

	@APIUsage
	public static boolean sleep(long ms, long ns)
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


	@APIUsage
	public static boolean join(Thread thread)
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


	@APIUsage
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
		catch(InterruptedException e)
		{
			return false;
		}
	}

	@APIUsage
	public static void notifyAll(Object object)
	{
		//noinspection SynchronizationOnLocalVariableOrMethodParameter
		synchronized(object)
		{
			object.notifyAll();
		}
	}

	@APIUsage
	public static Thread runDelayed(Runnable run, long ms)
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


	// BUKKIT
	@APIUsage
	public static void runSync(Runnable run)
	{
		Bukkit.getScheduler().runTask(AuxiliumAPI.getPlugin(), run);
	}

}
