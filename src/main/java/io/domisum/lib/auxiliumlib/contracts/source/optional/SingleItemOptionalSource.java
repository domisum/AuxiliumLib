package io.domisum.lib.auxiliumlib.contracts.source.optional;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface SingleItemOptionalSource<T>
{

	@API
	Optional<T> fetch();


	@API
	default T fetchOrException()
	{
		return fetch().orElseThrow(()->new NoSuchElementException("single item source is empty"));
	}

}
