package de.domisum.lib.auxilium.contracts.source.implementations.web;

import de.domisum.lib.auxilium.contracts.Converter;
import de.domisum.lib.auxilium.contracts.source.Source;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.PHR;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebDirectorySource<KeyT, T> implements Source<KeyT, T>
{

	private final AbstractURL webDirectory;
	private final Converter<KeyT, String> keyToInDirectoryPathConverter;


	// SOURCE
	@Override public Optional<T> fetch(KeyT key)
	{
		return fetch(getUrl(key));
	}

	@Override public T fetchOrException(KeyT key)
	{
		Optional<T> optional = fetch(key);
		if(!optional.isPresent())
			throw new NoSuchElementException(PHR.r("could not fetch key {}: ({})", key, getUrl(key)));

		return optional.get();
	}


	protected abstract Optional<T> fetch(AbstractURL url);


	// UTIL
	private AbstractURL getUrl(KeyT key)
	{
		String inDirectoryPath = keyToInDirectoryPathConverter.convert(key);
		return new AbstractURL(webDirectory, inDirectoryPath);
	}

}