package io.domisum.lib.auxiliumlib.contracts.source.optional.implementations.web;

import io.domisum.lib.auxiliumlib.contracts.source.optional.SingleItemOptionalSource;
import io.domisum.lib.auxiliumlib.datacontainers.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebSingleItemOptionalSource<T> implements SingleItemOptionalSource<T>
{

	protected final AbstractURL url;


	@Override
	public T fetchOrException()
	{
		Optional<T> optional = fetch();
		if(!optional.isPresent())
			throw new NoSuchElementException("could not fetch "+url);

		return optional.get();
	}

}
