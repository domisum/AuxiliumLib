package de.domisum.lib.auxilium.util.java;

import de.domisum.lib.auxilium.util.java.annotations.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@API
public class ThreadWatchdog
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final Duration THREAD_CHECK_INTERVAL = Duration.ofSeconds(1);

	// WATCHDOG
	private final Map<Thread, List<Runnable>> watchedThreadsOnTerminationActions = new HashMap<>();
	private Thread watchdogThread;


	// REGISTRATION
	@API public synchronized void register(Thread thread, Runnable run)
	{
		if(!watchedThreadsOnTerminationActions.containsKey(thread))
			watchedThreadsOnTerminationActions.put(thread, new ArrayList<>());

		watchedThreadsOnTerminationActions.get(thread).add(run);

		if(shouldStartWatchdogThread())
			startWatchdogThread();
	}


	@API public synchronized void unregister(Thread thread)
	{
		watchedThreadsOnTerminationActions.remove(thread);

		if(shouldStopWatchdogThread())
			stopWatchdogThread();
	}


	// WATCHING
	private void startWatchdogThread()
	{
		logger.info("Starting watchdog thread...");

		watchdogThread = ThreadUtil.createAndStartThread(this::run, "threadWatchdog");
	}

	private void stopWatchdogThread()
	{
		logger.info("Stopping watchdog thread...");

		watchdogThread.interrupt();
		ThreadUtil.join(watchdogThread);
		watchdogThread = null;
	}

	private void run()
	{
		while(!Thread.interrupted())
		{
			tick();
			ThreadUtil.sleep(THREAD_CHECK_INTERVAL.toMillis());
		}
	}

	private synchronized void tick()
	{
		for(Thread thread : new HashSet<>(watchedThreadsOnTerminationActions.keySet()))
			if(!thread.isAlive())
				onWatchedThreadDied(thread);
	}

	private void onWatchedThreadDied(Thread thread)
	{
		logger.info("Watched thread died: {}", thread);

		List<Runnable> threadOnTerminationActions = watchedThreadsOnTerminationActions.get(thread);
		watchedThreadsOnTerminationActions.remove(thread);

		for(Runnable runnable : threadOnTerminationActions)
			runnable.run();
	}


	// CONDITION UTIL
	private boolean shouldStartWatchdogThread()
	{
		return watchdogThread == null;
	}

	private boolean shouldStopWatchdogThread()
	{
		return watchedThreadsOnTerminationActions.isEmpty();
	}

}
