package de.domisum.lib.auxilium.contracts.source.implementations;

import de.domisum.lib.auxilium.contracts.source.Source;
import de.domisum.lib.auxilium.data.container.AbstractURL;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class WebDirectorySource<T> implements Source<String, T>
{

	private final AbstractURL webDirectory;


	// SOURCE
	@Override public Optional<T> fetch(String subPath)
	{
		AbstractURL url = new AbstractURL(webDirectory, subPath);
		return fetch(url);
	}

	protected abstract Optional<T> fetch(AbstractURL url);

}
