package de.domisum.lib.auxilium.contracts.iosource;

import de.domisum.lib.auxilium.run.RetryUntilSuccessfulIOAction;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;

public interface IoSource<KeyT, T>
{

	@API T fetch(KeyT key) throws IOException;

	@API default IoOptional<T> fetchOptional(KeyT key)
	{
		try
		{
			return IoOptional.of(fetch(key));
		}
		catch(IOException e)
		{
			return IoOptional.ofException(e);
		}
	}

	@API default T fetchOrThrowUncheckedException(KeyT key)
	{
		return fetchOptional(key).getOrThrowUnchecked();
	}

	@API default T fetchRetryUntilSuccessful(KeyT key)
	{
		return new RetryUntilSuccessfulIOAction<>(()->fetch(key), getFetchFailMessage(key)).execute();
	}

	@API default String getFetchFailMessage(KeyT key)
	{
		return "failed to fetch "+key;
	}

}
