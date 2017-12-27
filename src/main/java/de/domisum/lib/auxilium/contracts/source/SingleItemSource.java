package de.domisum.lib.auxilium.contracts.source;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface SingleItemSource<T>
{

	@API Optional<T> fetch();


	@API default T fetchOrException()
	{
		return fetch().orElseThrow(()->new NoSuchElementException("single item source is empty"));
	}

}
