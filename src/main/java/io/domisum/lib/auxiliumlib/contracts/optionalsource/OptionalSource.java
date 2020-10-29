package io.domisum.lib.auxiliumlib.contracts.optionalsource;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Optional;

public interface OptionalSource<KeyT, T>
{
	
	@API
	Optional<T> get(KeyT key);
	
	@API
	default boolean contains(KeyT key)
	{
		return get(key).isPresent();
	}
	
}
