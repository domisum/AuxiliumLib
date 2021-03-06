package io.domisum.lib.auxiliumlib.thread;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadWatchdog
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	private static final Duration THREAD_CHECK_INTERVAL = Duration.ofSeconds(1);
	
	// INSTANCE
	private static ThreadWatchdog instance = null;
	
	// WATCHDOG
	private final Map<Thread, List<Runnable>> watchedThreadsOnTerminationActions = new HashMap<>();
	private Thread watchdogThread;
	
	
	// SINGLETON
	@API
	public static synchronized void registerOnTerminationAction(Thread thread, Runnable run)
	{
		getInstance().register(thread, run);
	}
	
	@API
	public static synchronized void unregisterOnTerminationActions(Thread thread)
	{
		getInstance().unregister(thread);
	}
	
	private static synchronized ThreadWatchdog getInstance()
	{
		if(instance == null)
			instance = new ThreadWatchdog();
		
		return instance;
	}
	
	
	// REGISTRATION
	private void register(Thread thread, Runnable run)
	{
		if(!watchedThreadsOnTerminationActions.containsKey(thread))
			watchedThreadsOnTerminationActions.put(thread, new ArrayList<>());
		
		watchedThreadsOnTerminationActions.get(thread).add(run);
		
		if(shouldStartWatchdogThread())
			startWatchdogThread();
	}
	
	
	private void unregister(Thread thread)
	{
		watchedThreadsOnTerminationActions.remove(thread);
		
		if(shouldStopWatchdogThread())
			stopWatchdogThread();
	}
	
	
	// WATCHING
	private void startWatchdogThread()
	{
		logger.info("Starting thread watchdog");
		watchdogThread = ThreadUtil.createAndStartDaemonThread(this::run, "threadWatchdog");
	}
	
	private void stopWatchdogThread()
	{
		logger.info("Stopping thread watchdog");
		
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
		for(var thread : new HashSet<>(watchedThreadsOnTerminationActions.keySet()))
			if(!thread.isAlive())
				onWatchedThreadDied(thread);
	}
	
	private void onWatchedThreadDied(Thread thread)
	{
		logger.info("Watched thread died: {}", thread);
		
		var threadOnTerminationActions = watchedThreadsOnTerminationActions.get(thread);
		watchedThreadsOnTerminationActions.remove(thread);
		
		for(var runnable : threadOnTerminationActions)
			ThreadUtil.createAndStartThread(runnable, "shutdownAction-"+thread.getName());
	}
	
	
	// CONDITION UTIL
	private boolean shouldStartWatchdogThread()
	{
		return watchdogThread == null;
	}
	
	private boolean shouldStopWatchdogThread()
	{
		return watchedThreadsOnTerminationActions.isEmpty() && (watchdogThread != null);
	}
	
}
