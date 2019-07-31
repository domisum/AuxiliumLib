package de.domisum.lib.auxilium.async;

import de.domisum.lib.auxilium.contracts.source.io.ExecutorServices;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ExecutorServiceUser
{

	default int getThreadPoolSize()
	{
		return 20;
	}

	default <T> SimpleFuture<T> submit(Callable<T> callable)
	{
		ExecutorService executorService = ExecutorServices.getExecutorService(this);
		Future<T> future = executorService.submit(callable);
		return new SimpleFutureFutureWrapper<>(future);
	}

}
