package de.domisum.lib.auxilium.mattp.util;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.MattpRequestEnvoy;
import de.domisum.lib.auxilium.mattp.decorator.RetryingMattpRequestEnvoy;
import de.domisum.lib.auxilium.mattp.request.MattpRequest;
import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
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
		return get(url, new MattpStringReader());
	}

	// BYTE ARRAY
	@API public static Optional<byte[]> getByteArray(AbstractURL url)
	{
		return get(url, new MattpByteArrayReader());
	}

	// IMAGE
	@API public static Optional<BufferedImage> getImage(AbstractURL url)
	{
		return get(url, new MattpImageReader());
	}

	// GENERIC
	@API public static <T> Optional<T> get(AbstractURL url, MattpResponseBodyReader<T> responseBodyReader)
	{
		MattpRequest request = MattpRequest.get(url);
		MattpRequestEnvoy<T> envoy = new RetryingMattpRequestEnvoy<>(request, responseBodyReader);

		return envoy.send().getContent();
	}

}
