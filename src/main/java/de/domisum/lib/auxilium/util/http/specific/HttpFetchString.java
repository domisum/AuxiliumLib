package de.domisum.lib.auxilium.util.http.specific;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.HttpFetch;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@API
public class HttpFetchString extends HttpFetch<String>
{

	// CONSTANTS
	private static final Charset STRING_CHARSET = StandardCharsets.UTF_8;


	// INIT
	public HttpFetchString(AbstractURL url)
	{
		super(url);
	}


	// FETCH
	@Override protected Optional<String> convertToSpecific(InputStream inputStream)
	{
		try
		{
			return Optional.ofNullable(IOUtils.toString(inputStream, STRING_CHARSET));
		}
		catch(IOException e)
		{
			getOnFail().handle(e);
			return Optional.empty();
		}
	}

}
