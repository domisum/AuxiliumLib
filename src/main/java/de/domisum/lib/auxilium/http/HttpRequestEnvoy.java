package de.domisum.lib.auxilium.http;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.http.authproviders.NoAuthProvider;
import de.domisum.lib.auxilium.http.request.HttpHeader;
import de.domisum.lib.auxilium.http.request.HttpRequest;
import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.http.response.RequestResponse;
import de.domisum.lib.auxilium.http.response.readers.HttpStringReader;
import de.domisum.lib.auxilium.http.response.responses.ConnectionError;
import de.domisum.lib.auxilium.http.response.responses.RequestFailure;
import de.domisum.lib.auxilium.http.response.responses.RequestSuccess;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpMessage;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpRequestEnvoy<T>
{

	// REQUEST
	private final HttpRequest request;
	@Setter private HttpAuthProvider authProvider = new NoAuthProvider();

	// RESPONSE
	private final HttpResponseBodyReader<T> responseBodyReader;


	// SEND
	@API public RequestResponse<T> send()
	{
		CloseableHttpClient httpClient = buildHttpClient();
		HttpUriRequest apacheRequest = buildApacheRequest();

		try(CloseableHttpResponse response = httpClient.execute(apacheRequest))
		{
			return processResponse(response);
		}
		catch(IOException e)
		{
			return new ConnectionError<>(ExceptionUtils.getStackTrace(e));
		}
	}


	// RESPONSE
	private RequestResponse<T> processResponse(org.apache.http.HttpResponse response) throws IOException
	{
		de.domisum.lib.auxilium.http.response.StatusLine statusLine = convertApacheToDomainStatusLine(response.getStatusLine());

		if(didRequestFail(response))
			return new RequestFailure<>(statusLine, readResponseBodyOnFailure(response));

		return new RequestSuccess<>(statusLine, readResponseBodyOnSuccess(response));
	}

	private T readResponseBodyOnSuccess(org.apache.http.HttpResponse response) throws IOException
	{
		return readResponseBody(response, responseBodyReader);
	}

	private String readResponseBodyOnFailure(org.apache.http.HttpResponse response) throws IOException
	{
		return readResponseBody(response, new HttpStringReader());
	}

	private <BodyT> BodyT readResponseBody(org.apache.http.HttpResponse response, HttpResponseBodyReader<BodyT> reader)
			throws IOException
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
		return clientBuilder.build();
	}


	// BUILD REQUEST
	private HttpUriRequest buildApacheRequest()
	{
		HttpUriRequest apacheRequest = getRawMethodRequest();

		addHeadersToRequest(apacheRequest);
		if(request.getBody() != null)
			addBodyToRequest(apacheRequest);

		return apacheRequest;
	}

	private HttpUriRequest getRawMethodRequest()
	{
		AbstractURL url = request.getUrl();

		switch(request.getHttpMethod())
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
		for(HttpHeader header : request.getHeaders())
			apacheRequest.addHeader(header.getKey(), header.getValue());
	}

	private void addBodyToRequest(HttpMessage apacheRequest)
	{
		apacheRequest.addHeader("Content-Type", request.getBody().getContentType());
		((HttpEntityEnclosingRequest) apacheRequest).setEntity(new InputStreamEntity(request.getBody().getAsInputStream()));
	}


	// UTIL
	private de.domisum.lib.auxilium.http.response.StatusLine convertApacheToDomainStatusLine(StatusLine apacheStatusLine)
	{
		return new de.domisum.lib.auxilium.http.response.StatusLine(
				apacheStatusLine.getProtocolVersion().toString(),
				apacheStatusLine.getStatusCode(),
				apacheStatusLine.getReasonPhrase());
	}


	// CONDITION UTIL
	private boolean didRequestFail(org.apache.http.HttpResponse response)
	{
		int statusCode = response.getStatusLine().getStatusCode();
		int statusCodeFirstDigit = statusCode/100;
		return statusCodeFirstDigit != 2;
	}

}
