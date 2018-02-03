package de.domisum.lib.auxilium.mattp.util;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.MattpRequestEnvoy;
import de.domisum.lib.auxilium.mattp.decorator.RetryingMattpRequestEnvoy;
import de.domisum.lib.auxilium.mattp.request.MattpRequest;
import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.mattp.response.RequestResponse;
import de.domisum.lib.auxilium.mattp.response.readers.MattpByteArrayReader;
import de.domisum.lib.auxilium.mattp.response.readers.MattpImageReader;
import de.domisum.lib.auxilium.mattp.response.readers.MattpStringReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MattpGetUtil
{

	// STRING
	@API public static Optional<String> getString(AbstractURL url)
	{
		return getStringResponse(url).getContent();
	}

	@API public static RequestResponse<String> getStringResponse(AbstractURL url)
	{
		return getResponse(url, new MattpStringReader());
	}


	// BYTE ARRAY
	@API public static Optional<byte[]> getByteArray(AbstractURL url)
	{
		return getByteArrayResponse(url).getContent();
	}

	@API public static RequestResponse<byte[]> getByteArrayResponse(AbstractURL url)
	{
		return getResponse(url, new MattpByteArrayReader());
	}


	// IMAGE
	@API public static Optional<BufferedImage> getImage(AbstractURL url)
	{
		return getImageResponse(url).getContent();
	}

	@API public static RequestResponse<BufferedImage> getImageResponse(AbstractURL url)
	{
		return getResponse(url, new MattpImageReader());
	}


	// GENERIC
	@API public static <T> RequestResponse<T> getResponse(AbstractURL url, MattpResponseBodyReader<T> responseBodyReader)
	{
		MattpRequest request = MattpRequest.get(url);
		MattpRequestEnvoy<T> envoy = new RetryingMattpRequestEnvoy<>(request, responseBodyReader);

		return envoy.send();
	}

	@API public static <T> Optional<T> get(AbstractURL url, MattpResponseBodyReader<T> responseBodyReader)
	{
		return getResponse(url, responseBodyReader).getContent();
	}

}
