package io.domisum.lib.auxiliumlib.contracts.iosource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

@API
public interface IoSource<KeyT, T>
{
	
	@API
	T get(KeyT key)
		throws IOException;
	
	@API
	default IoOptional<T> getOptional(KeyT key)
	{
		return IoOptional.ofAction(()->get(key));
	}
	
	@API
	default T getOrThrowUncheckedException(KeyT key)
	{
		return getOptional(key).getOrThrowUnchecked();
	}
	
}
