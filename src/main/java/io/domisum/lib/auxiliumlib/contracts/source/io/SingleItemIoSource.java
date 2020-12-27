package io.domisum.lib.auxiliumlib.contracts.source.io;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface SingleItemIoSource<V>
{
	
	@API
	V get()
		throws IOException;
	
	@API
	default IoOptional<V> getOptional()
	{
		return IoOptional.ofAction(this::get);
	}
	
	@API
	default V getOrThrowUncheckedException()
	{
		return getOptional().getOrThrowUnchecked();
	}
	
}
