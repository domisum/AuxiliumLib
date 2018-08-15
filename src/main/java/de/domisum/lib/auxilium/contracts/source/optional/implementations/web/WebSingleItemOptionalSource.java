package de.domisum.lib.auxilium.contracts.source.optional.implementations.web;

import de.domisum.lib.auxilium.contracts.source.optional.SingleItemOptionalSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebSingleItemOptionalSource<T> implements SingleItemOptionalSource<T>
{

	protected final AbstractURL url;


	@Override public T fetchOrException()
	{
		Optional<T> optional = fetch();
		if(!optional.isPresent())
			throw new NoSuchElementException("could not fetch "+url);

		return optional.get();
	}

}
