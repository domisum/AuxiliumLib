package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpFetchString implements HttpFetchSpecific<String>
{

	// CONSTANTS
	private static final Charset STRING_CHARSET = StandardCharsets.UTF_8;


	// FETCH
	@Override public Optional<String> fetch(InputStream inputStream, ExceptionHandler<Exception> onFail)
	{
		try
		{
			return Optional.ofNullable(IOUtils.toString(inputStream, STRING_CHARSET));
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}

}
