package de.domisum.lib.auxilium.util.http.specific;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.HttpFetch;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@API
public class HttpFetchRaw extends HttpFetch<byte[]>
{

	// INIT
	public HttpFetchRaw(AbstractURL url)
	{
		super(url);
	}


	// FETCH
	@Override protected Optional<byte[]> convertToSpecific(InputStream inputStream)
	{
		try
		{
			return Optional.ofNullable(IOUtils.toByteArray(inputStream));
		}
		catch(IOException e)
		{
			getOnFail().handle(e);
			return Optional.empty();
		}
	}

}
