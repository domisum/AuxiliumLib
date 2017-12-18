package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.IOExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@API
public final class HttpFetchUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;


	// STRING
	@API public static Optional<String> fetchString(String url)
	{
		return fetchString(url, DEFAULT_STRING_ENCODING, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(String url, Charset encoding)
	{
		return fetchString(url, encoding, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(String url, IOExceptionHandler onFail)
	{
		return fetchString(url, DEFAULT_STRING_ENCODING, onFail);
	}

	@API public static Optional<String> fetchString(String url, Charset encoding, IOExceptionHandler onFail)
	{
		try
		{
			return Optional.of(IOUtils.toString(new URL(url), encoding));
		}
		catch(java.io.IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}


	// RAW BYTES
	@API public static Optional<byte[]> fetchRaw(String url)
	{
		return fetchRaw(url, IOExceptionHandler.noAction());
	}

	@API public static Optional<byte[]> fetchRaw(String url, IOExceptionHandler onFail)
	{
		try(InputStream inputStream = new AbstractURL(url).toNet().openStream())
		{
			return Optional.of(IOUtils.toByteArray(inputStream));
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}

}
