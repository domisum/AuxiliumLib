package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.specific.HttpFetchImage;
import de.domisum.lib.auxilium.util.http.specific.HttpFetchRaw;
import de.domisum.lib.auxilium.util.http.specific.HttpFetchString;
import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpFetchUtil
{

	// CONSTANTS
	private static final int NUMBER_OF_FETCH_TRIES = 3;


	// STRING
	@API public static Optional<String> fetchString(AbstractURL url)
	{
		return fetchString(url, ExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(AbstractURL url, ExceptionHandler<IOException> onFail)
	{
		HttpFetch<String> fetchString = new HttpFetchString(url).onFail(onFail).numberOfTries(NUMBER_OF_FETCH_TRIES);
		return fetchString.fetch();
	}

	@API public static String fetchStringOrException(AbstractURL url)
	{
		return ExceptionHandler.getOrIOException(onFail->fetchString(url, onFail));
	}


	// RAW BYTES
	@API public static Optional<byte[]> fetchRaw(AbstractURL url)
	{
		return fetchRaw(url, ExceptionHandler.noAction());
	}

	@API public static Optional<byte[]> fetchRaw(AbstractURL url, ExceptionHandler<IOException> onFail)
	{
		HttpFetch<byte[]> fetchRaw = new HttpFetchRaw(url).onFail(onFail).numberOfTries(NUMBER_OF_FETCH_TRIES);
		return fetchRaw.fetch();
	}


	@API public static byte[] fetchRawOrException(AbstractURL url)
	{
		return ExceptionHandler.getOrIOException(onFail->fetchRaw(url, onFail));
	}


	// IMAGE
	@API public static Optional<BufferedImage> fetchImage(AbstractURL url)
	{
		return fetchImage(url, ExceptionHandler.noAction());
	}

	@API public static Optional<BufferedImage> fetchImage(AbstractURL url, ExceptionHandler<IOException> onFail)
	{
		HttpFetch<BufferedImage> fetchImage = new HttpFetchImage(url).onFail(onFail).numberOfTries(NUMBER_OF_FETCH_TRIES);
		return fetchImage.fetch();
	}


	@API public static BufferedImage fetchImageOrException(AbstractURL url)
	{
		return ExceptionHandler.getOrIOException(onFail->fetchImage(url, onFail));
	}

}
