package de.domisum.lib.auxilium.util.http.specific;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.HttpFetch;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@API
public class HttpFetchToFile extends HttpFetch<File>
{

	// SETTINGS
	private final File downloadTo;


	// INIT
	public HttpFetchToFile(AbstractURL url, File downloadTo)
	{
		super(url);
		this.downloadTo = downloadTo;
	}


	// FETCH
	@Override protected Optional<File> fetch(InputStream inputStream)
	{
		try
		{
			FileUtils.copyInputStreamToFile(inputStream, downloadTo);
			return Optional.of(downloadTo); // if this is reached, the download was successful
		}
		catch(IOException e)
		{
			getOnFail().handle(e);
			return Optional.empty();
		}
	}

}
