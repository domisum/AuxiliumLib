package de.domisum.lib.auxilium.contracts.source.io;

import de.domisum.lib.auxilium.async.ExecutorServiceUser;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServices
{

	private static final Map<Class<?>, ExecutorService> EXECUTOR_SERVICES = new HashMap<>();

	public static ExecutorService getExecutorService(ExecutorServiceUser executorServiceUser)
	{
		Class<? extends ExecutorServiceUser> clazz = executorServiceUser.getClass();

		if(!EXECUTOR_SERVICES.containsKey(clazz))
		{
			int threadPoolSize = executorServiceUser.getThreadPoolSize();
			ThreadFactory threadFactory = ExecutorServices::createThread;

			EXECUTOR_SERVICES.put(clazz, Executors.newFixedThreadPool(threadPoolSize, threadFactory));
		}

		ExecutorService executorService = EXECUTOR_SERVICES.get(clazz);
		return executorService;
	}

	private static Thread createThread(Runnable runnable)
	{
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		ThreadUtil.logUncaughtExceptions(thread);

		return thread;
	}

}
