package io.domisum.lib.auxiliumlib.contracts.source.io;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

@API
public interface IoSource<K, V>
{
	
	@API
	V get(K key)
		throws IOException;
	
	@API
	default IoOptional<V> getOptional(K key)
	{
		return IoOptional.ofAction(()->get(key));
	}
	
	@API
	default V getOrThrowUncheckedException(K key)
	{
		return getOptional(key).getOrThrowUnchecked();
	}
	
}
