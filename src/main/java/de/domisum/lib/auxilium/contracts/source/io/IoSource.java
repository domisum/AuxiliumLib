package de.domisum.lib.auxilium.contracts.source.io;

import de.domisum.lib.auxilium.run.RetryUntilSuccessfulIOAction;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@API
public interface IoSource<KeyT, T>
{

	// SOURCE
	@API
	T fetch(KeyT key) throws IOException;

	@API
	default IoOptional<T> fetchOptional(KeyT key)
	{
		return IoOptional.ofAction(()->fetch(key));
	}

	@API
	default T fetchOrThrowUncheckedException(KeyT key)
	{
		return fetchOptional(key).getOrThrowUnchecked();
	}

	@API
	default T fetchRetryUntilSuccessful(KeyT key)
	{
		return new RetryUntilSuccessfulIOAction<>(()->fetch(key), getFetchFailMessage(key)).execute();
	}

	@API
	default Future<IoOptional<T>> fetchOptionalAsync(KeyT key)
	{
		ExecutorService executorService = IoSourceAsyncThreadPools.getExecutorService(getClass(), getAsycnThreadPoolSize());
		return executorService.submit(()->fetchOptional(key));
	}


	@API
	default String getFetchFailMessage(KeyT key)
	{
		return "failed to fetch "+key;
	}

	@API
	default int getAsycnThreadPoolSize()
	{
		return 10;
	}

}
