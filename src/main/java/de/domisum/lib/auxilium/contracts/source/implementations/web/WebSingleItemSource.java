package de.domisum.lib.auxilium.contracts.source.implementations.web;

import de.domisum.lib.auxilium.contracts.source.SingleItemSource;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebSingleItemSource<T> implements SingleItemSource<T>
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
