package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.http.specific.HttpFetchString;
import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpFetchUtil
{

	// STRING
	@API public static Optional<String> fetchString(AbstractURL url)
	{
		return fetchString(url, ExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(AbstractURL url, ExceptionHandler<IOException> onFail)
	{
		HttpFetch<String> httpFetchString = new HttpFetchString(url).onFail(onFail).maxNumberOfTries(3);

		return httpFetchString.fetch();
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
		try(InputStream inputStream = url.toNet().openStream())
		{
			return Optional.of(IOUtils.toByteArray(inputStream));
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
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
		try
		{
			return Optional.ofNullable(ImageIO.read(url.toNet()));
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}


	@API public static BufferedImage fetchImageOrException(AbstractURL url)
	{
		return ExceptionHandler.getOrIOException(onFail->fetchImage(url, onFail));
	}

}
