package io.domisum.lib.auxiliumlib.contracts.source.implementations.web;

import io.domisum.lib.auxiliumlib.contracts.source.SingleItemSource;
import io.domisum.lib.auxiliumlib.datacontainers.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public abstract class WebSingleItemSource<T>
		implements SingleItemSource<T>
{

	protected final AbstractURL url;


	@Override
	public T fetchOrError()
	{
		var optional = fetch();
		if(optional.isEmpty())
			throw new NoSuchElementException("could not fetch "+url);

		return optional.get();
	}

}
