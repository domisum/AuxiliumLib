package de.domisum.lib.auxilium.contracts.iosource;

import de.domisum.lib.auxilium.run.RetryUntilSuccessfulIOAction;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;

@API
public interface SingleItemIoSource<T>
{

	@API T fetch() throws IOException;

	@API default IoOptional<T> fetchOptional()
	{
		return IoOptional.ofAction(this::fetch);
	}

	@API default T fetchOrThrowUncheckedException()
	{
		return fetchOptional().getOrThrowUnchecked();
	}

	@API default T fetchRetryUntilSuccessful()
	{
		return new RetryUntilSuccessfulIOAction<>(this::fetch, getFetchFailMessage()).execute();
	}

	@API default String getFetchFailMessage()
	{
		return "failed to fetch ";
	}

}
