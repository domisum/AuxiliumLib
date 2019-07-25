package de.domisum.lib.auxilium.contracts.source.io;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoSourceAsyncThreadPools
{

	private static final Map<Class<?>, ExecutorService> EXECUTOR_SERVICES = new HashMap<>();

	public static ExecutorService getExecutorService(Class<?> clazz, int size)
	{
		if(!EXECUTOR_SERVICES.containsKey(clazz))
			EXECUTOR_SERVICES.put(clazz, Executors.newFixedThreadPool(size, runnable->
			{
				Thread thread = new Thread(runnable);
				thread.setDaemon(true);
				ThreadUtil.logUncaughtExceptions(thread);
				return thread;
			}));

		ExecutorService executorService = EXECUTOR_SERVICES.get(clazz);
		return executorService;
	}

}
