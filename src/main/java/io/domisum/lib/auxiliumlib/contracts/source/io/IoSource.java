package io.domisum.lib.auxiliumlib.contracts.source.io;

import io.domisum.lib.auxiliumlib.run.RetryUntilSuccessfulIOAction;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;

import java.io.IOException;

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
	default String getFetchFailMessage(KeyT key)
	{
		return "failed to fetch "+key;
	}

}
