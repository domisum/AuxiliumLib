package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@API
public class AbstractURL
{

	private final String url;


	// INIT
	@API public AbstractURL(String url)
	{
		String cleanedUrl = url;
		while(cleanedUrl.endsWith("/"))
			cleanedUrl = cleanedUrl.substring(0, cleanedUrl.length()-1);

		this.url = cleanedUrl;
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


	// UTIL
	public static String escapeParameterValue(String parameterValue)
	{
		try
		{
			return URLEncoder.encode(parameterValue, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
