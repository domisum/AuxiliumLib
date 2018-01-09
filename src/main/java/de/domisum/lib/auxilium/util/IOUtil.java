package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOUtil
{

	@API public static byte[] toByteArray(InputStream inputStream)
	{
		try
		{
			return IOUtils.toByteArray(inputStream);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
