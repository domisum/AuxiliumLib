package de.domisum.lib.auxilium.contracts.source.implementations;

import de.domisum.lib.auxilium.contracts.Converter;
import de.domisum.lib.auxilium.contracts.source.Source;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebDirectorySource<KeyT, T> implements Source<KeyT, T>
{

	private final AbstractURL webDirectory;
	private final Converter<KeyT, String> keyToInDirectoryPathConverter;


	// SOURCE
	@Override public Optional<T> fetch(KeyT key)
	{
		String inDirectoryPath = keyToInDirectoryPathConverter.convert(key);
		AbstractURL url = new AbstractURL(webDirectory, inDirectoryPath);

		return fetch(url);
	}

	protected abstract Optional<T> fetch(AbstractURL url);

}
