package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.IOExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpFetchUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;


	// STRING
	@API public static Optional<String> fetchString(AbstractURL url)
	{
		return fetchString(url, DEFAULT_STRING_ENCODING, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(AbstractURL url, Charset encoding)
	{
		return fetchString(url, encoding, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(AbstractURL url, IOExceptionHandler onFail)
	{
		return fetchString(url, DEFAULT_STRING_ENCODING, onFail);
	}

	@API public static Optional<String> fetchString(AbstractURL url, Charset encoding, IOExceptionHandler onFail)
	{
		try
		{
			return Optional.of(IOUtils.toString(url.toNet(), encoding));
		}
		catch(java.io.IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}


	@API public static String fetchStringOrException(AbstractURL url)
	{
		return fetchStringOrException(url, DEFAULT_STRING_ENCODING);
	}

	@API public static String fetchStringOrException(AbstractURL url, Charset encoding)
	{
		return IOExceptionHandler.getOrException(onFail->fetchString(url, encoding, onFail));
	}


	// RAW BYTES
	@API public static Optional<byte[]> fetchRaw(AbstractURL url)
	{
		return fetchRaw(url, IOExceptionHandler.noAction());
	}

	@API public static Optional<byte[]> fetchRaw(AbstractURL url, IOExceptionHandler onFail)
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
		return IOExceptionHandler.getOrException(onFail->fetchRaw(url, onFail));
	}


	// IMAGE
	@API public static Optional<BufferedImage> fetchImage(AbstractURL url)
	{
		return fetchImage(url, IOExceptionHandler.noAction());
	}

	@API public static Optional<BufferedImage> fetchImage(AbstractURL url, IOExceptionHandler onFail)
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
		return IOExceptionHandler.getOrException(onFail->fetchImage(url, onFail));
	}

}
