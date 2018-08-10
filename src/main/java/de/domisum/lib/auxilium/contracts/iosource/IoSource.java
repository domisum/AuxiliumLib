package de.domisum.lib.auxilium.contracts.iosource;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface IoSource<KeyT, T>
{

	@API T fetch(KeyT key) throws IOException;

	@API default T fetchOrUncaughtException(KeyT key)
	{
		try
		{
			return fetch(key);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
