package io.domisum.lib.auxiliumlib.contracts.iosource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface VoidIoSource<T>
{
	
	// SOURCE
	@API
	T fetch()
			throws IOException;
	
	@API
	default IoOptional<T> fetchOptional()
	{
		return IoOptional.ofAction(this::fetch);
	}
	
	@API
	default T fetchOrThrowUncheckedException()
	{
		return fetchOptional().getOrThrowUnchecked();
	}
	
}
