package io.domisum.lib.auxiliumlib.contracts.iosource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

@API
public interface IoSource<KeyT, T>
{
	
	// SOURCE
	@API
	T fetch(KeyT key)
			throws IOException;
	
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
	
}
