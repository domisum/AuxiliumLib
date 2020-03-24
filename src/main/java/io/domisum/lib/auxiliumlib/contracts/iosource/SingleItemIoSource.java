package io.domisum.lib.auxiliumlib.contracts.iosource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

@API
public interface SingleItemIoSource<T>
{
	
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
