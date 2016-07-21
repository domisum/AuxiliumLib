package de.domisum.auxiliumapi.util.java;

import org.bukkit.Bukkit;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class ThreadUtil
{

	public static boolean sleep(long ms)
	{
		return sleep(ms, 0);
	}

	public static boolean sleepNs(long ns)
	{
		long ms = ns / (1000 * 1000);
		long nsOnly = ns % (1000 * 1000);

		return sleep(ms, nsOnly);
	}

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


	public static boolean wait(Object object)
	{
		try
		{
			synchronized (object)
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

	public static void notifyAll(Object object)
	{
		synchronized (object)
		{
			object.notifyAll();
		}
	}


	public static Thread runDelayed(Runnable run, long ms)
	{
		Runnable delay = () ->
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
	public static void runSync(Runnable run)
	{
		Bukkit.getScheduler().runTask(AuxiliumAPI.getPlugin(), run);
	}

}
