package de.domisum.lib.auxilium.mattp;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.authproviders.NoAuthProvider;
import de.domisum.lib.auxilium.mattp.request.MattpRequest;
import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.mattp.response.RequestResponse;
import de.domisum.lib.auxilium.mattp.response.readers.MattpStringReader;
import de.domisum.lib.auxilium.mattp.response.responses.ConnectionError;
import de.domisum.lib.auxilium.mattp.response.responses.RequestFailure;
import de.domisum.lib.auxilium.mattp.response.responses.RequestSuccess;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@API
@RequiredArgsConstructor
public class MattpRequestEnvoy<T>
{

	// BASE SETTINGS
	private final MattpRequest request;
	private final MattpResponseBodyReader<T> responseBodyReader;

	// ADDTITIONAL SETTINGS
	@Setter private Duration timeout = Duration.ofSeconds(10);
	@Setter private MattpAuthProvider authProvider = new NoAuthProvider();
	@Setter private boolean followRedirects = true;


	// SEND
	@API public RequestResponse<T> send()
	{
		HttpUriRequest apacheRequest = buildApacheRequest();

		RequestTimeouter requestTimeouter = new RequestTimeouter(apacheRequest, timeout);
		requestTimeouter.start();

		try(CloseableHttpClient httpClient = buildHttpClient();
				CloseableHttpResponse response = httpClient.execute(apacheRequest))
		{
			if(requestTimeouter.didTimeOutAndEnd())
				return new ConnectionError<>("Request aborted due to timeout");

			return processResponse(response);
		}
		catch(IOException|RuntimeException e)
		{
			if(requestTimeouter.didTimeOutAndEnd())
				return new ConnectionError<>("Request aborted due to timeout");

			return new ConnectionError<>(ExceptionUtils.getStackTrace(e));
		}
	}


	// RESPONSE
	private RequestResponse<T> processResponse(HttpResponse response) throws IOException
	{
		de.domisum.lib.auxilium.mattp.response.StatusLine statusLine = convertApacheToDomainStatusLine(response.getStatusLine());

		if(didRequestFail(response))
			return new RequestFailure<>(statusLine, readResponseBodyOnFailure(response));

		return new RequestSuccess<>(statusLine, readHeadersOnSuccess(response), readResponseBodyOnSuccess(response));
	}

	private T readResponseBodyOnSuccess(HttpResponse response) throws IOException
	{
		return readResponseBody(response, responseBodyReader);
	}

	private MattpHeaders readHeadersOnSuccess(HttpResponse response)
	{
		List<MattpHeader> headers = new ArrayList<>();

		for(Header header : response.getAllHeaders())
			headers.add(new MattpHeader(header.getName(), header.getValue()));

		return new MattpHeaders(headers);
	}

	private String readResponseBodyOnFailure(HttpResponse response) throws IOException
	{
		return readResponseBody(response, new MattpStringReader());
	}

	private <BodyT> BodyT readResponseBody(HttpResponse response, MattpResponseBodyReader<BodyT> reader) throws IOException
	{
		try(InputStream responseBodyStream = response.getEntity().getContent())
		{
			return reader.read(responseBodyStream);
		}
	}


	// BUILD CLIENT
	private CloseableHttpClient buildHttpClient()
	{
		HttpClientBuilder clientBuilder = HttpClients.custom();
		authProvider.provideAuthFor(clientBuilder);
		clientBuilder.setDefaultRequestConfig(buildRequestConfig().build());

		if(!followRedirects)
			clientBuilder.disableRedirectHandling();

		return clientBuilder.build();
	}

	private Builder buildRequestConfig()
	{
		Builder requestConfigBuilder = RequestConfig.custom();

		// noinspection deprecation
		requestConfigBuilder.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY);
		requestConfigBuilder
				.setSocketTimeout((int) timeout.toMillis())
				.setConnectTimeout((int) timeout.toMillis())
				.setConnectionRequestTimeout((int) timeout.toMillis());

		return requestConfigBuilder;
	}


	// BUILD REQUEST
	private HttpUriRequest buildApacheRequest()
	{
		HttpRequestBase apacheRequest = getRawMethodRequest();

		addHeadersToRequest(apacheRequest);
		if(request.getBody() != null)
			addBodyToRequest(apacheRequest);

		return apacheRequest;
	}

	private HttpRequestBase getRawMethodRequest()
	{
		AbstractURL url = request.getUrl();

		switch(request.getMattpMethod())
		{
			case GET:
				return new HttpGet(url.toString());
			case HEAD:
				return new HttpHead(url.toString());
			case POST:
				return new HttpPost(url.toString());
			case PUT:
				return new HttpPut(url.toString());
			case DELETE:
				return new HttpDelete(url.toString());
			case TRACE:
				return new HttpTrace(url.toString());
			case OPTIONS:
				return new HttpOptions(url.toString());
			case PATCH:
				return new HttpPatch(url.toString());
		}

		throw new ShouldNeverHappenError();
	}

	private void addHeadersToRequest(HttpMessage apacheRequest)
	{
		for(MattpHeader header : request.getHeaders())
			apacheRequest.addHeader(header.getKey(), header.getValue());
	}

	private void addBodyToRequest(HttpMessage apacheRequest)
	{
		apacheRequest.addHeader("Content-Type", request.getBody().getContentType());
		((HttpEntityEnclosingRequest) apacheRequest).setEntity(new InputStreamEntity(request.getBody().getAsInputStream()));
	}


	// UTIL
	private de.domisum.lib.auxilium.mattp.response.StatusLine convertApacheToDomainStatusLine(StatusLine apacheStatusLine)
	{
		return new de.domisum.lib.auxilium.mattp.response.StatusLine(
				apacheStatusLine.getProtocolVersion().toString(),
				apacheStatusLine.getStatusCode(),
				apacheStatusLine.getReasonPhrase()
		);
	}


	// CONDITION UTIL
	private boolean didRequestFail(HttpResponse response)
	{
		int statusCode = response.getStatusLine().getStatusCode();
		int statusCodeFirstDigit = statusCode/100;
		return statusCodeFirstDigit >= 4;
	}

}
