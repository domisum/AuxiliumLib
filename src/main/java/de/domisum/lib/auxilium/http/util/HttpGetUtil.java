package de.domisum.lib.auxilium.http.util;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.http.HttpRequestEnvoy;
import de.domisum.lib.auxilium.http.decorator.RetryingHttpRequestEnvoy;
import de.domisum.lib.auxilium.http.request.HttpRequest;
import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.http.response.readers.HttpByteArrayReader;
import de.domisum.lib.auxilium.http.response.readers.HttpImageReader;
import de.domisum.lib.auxilium.http.response.readers.HttpStringReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpGetUtil
{

	// STRING
	@API public static Optional<String> getString(AbstractURL url)
	{
		return get(url, new HttpStringReader());
	}

	// BYTE ARRAY
	@API public static Optional<byte[]> getByteArray(AbstractURL url)
	{
		return get(url, new HttpByteArrayReader());
	}

	// IMAGE
	@API public static Optional<BufferedImage> getImage(AbstractURL url)
	{
		return get(url, new HttpImageReader());
	}

	// GENERIC
	@API public static <T> Optional<T> get(AbstractURL url, HttpResponseBodyReader<T> responseBodyReader)
	{
		HttpRequest request = HttpRequest.get(url);
		HttpRequestEnvoy<T> envoy = new RetryingHttpRequestEnvoy<>(request, responseBodyReader);

		return envoy.send().getContent();
	}

}
