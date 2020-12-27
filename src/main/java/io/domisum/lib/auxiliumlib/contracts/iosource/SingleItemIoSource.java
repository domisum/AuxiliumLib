package io.domisum.lib.auxiliumlib.contracts.iosource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface SingleItemIoSource<T>
{
	
	@API
	T get()
		throws IOException;
	
	@API
	default IoOptional<T> getOptional()
	{
		return IoOptional.ofAction(this::get);
	}
	
	@API
	default T getOrThrowUncheckedException()
	{
		return getOptional().getOrThrowUnchecked();
	}
	
}
