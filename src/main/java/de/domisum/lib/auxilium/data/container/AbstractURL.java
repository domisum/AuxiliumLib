package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

@API
public class AbstractURL
{

	private final String url;


	// INIT
	@API public AbstractURL(String url)
	{
		while(url.endsWith("/"))
			url = url.substring(0, url.length()-1);

		this.url = url;
	}

	@API public AbstractURL(AbstractURL base, String extension)
	{
		this(base.combineWith(extension));
	}

	private String combineWith(String extension)
	{
		String processedExtension = extension;
		if(!processedExtension.startsWith("/"))
			processedExtension = "/"+processedExtension;

		return url+processedExtension;
	}


	// OBJECT
	@Override public String toString()
	{
		return url;
	}


	// CONVERSION
	@API public URL toNet()
	{
		try
		{
			return new URL(url);
		}
		catch(MalformedURLException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
