package io.domisum.lib.auxiliumlib.datacontainers;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@API
public class AbstractURL
{
	
	private final String url;
	
	
	// INIT
	@API
	public AbstractURL(String url)
	{
		String beingCleanedUrl = url;
		while(beingCleanedUrl.endsWith("/"))
			beingCleanedUrl = beingCleanedUrl.substring(0, beingCleanedUrl.length()-1);
		
		this.url = beingCleanedUrl;
	}
	
	@API
	public AbstractURL(AbstractURL base, String extension)
	{
		this(base.combineWith(extension));
	}
	
	private String combineWith(String extension)
	{
		if(!extension.startsWith("/"))
			extension = "/"+extension;
		return url+extension;
	}
	
	
	// OBJECT
	@Override
	public String toString()
	{
		return url;
	}
	
	
	// CONVERSION
	@API
	public URL toNet()
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
	
	
	@API
	public static String escapeUrlParameterString(String urlString)
	{
		return URLEncoder.encode(urlString, StandardCharsets.UTF_8);
	}
	
}
