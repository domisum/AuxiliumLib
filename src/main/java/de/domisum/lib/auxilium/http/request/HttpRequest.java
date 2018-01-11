package de.domisum.lib.auxilium.http.request;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class HttpRequest
{

	@Getter private final AbstractURL url;
	@Getter private final HttpMethod httpMethod;

	private final List<HttpHeader> headers = new ArrayList<>();
	@Getter @Setter private HttpRequestBody body; // TODO check if body is allowed with method


	// INIT
	@API public static HttpRequest get(AbstractURL url)
	{
		return new HttpRequest(url, HttpMethod.GET);
	}

	@API public void addHeader(String key, String value)
	{
		addHeader(new HttpHeader(key, value));
	}

	@API public void addHeader(HttpHeader header)
	{
		headers.add(header);
	}


	// GETTERS
	public List<HttpHeader> getHeaders()
	{
		return Collections.unmodifiableList(headers);
	}

}
