package io.domisum.lib.auxiliumlib.contracts.source;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface SingleItemSource<T>
{

	@API
	Optional<T> fetch();

	@API
	default T fetchOrError()
	{
		return fetch().orElseThrow(()->new NoSuchElementException("single item source is empty"));
	}

}
