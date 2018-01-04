package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
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
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@API
@RequiredArgsConstructor
public abstract class HttpFetch<T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final AbstractURL url;
	private HttpMethod method = HttpMethod.GET;
	private HttpCredentials credentials;

	private int numberOfTries = 3;
	@Getter(AccessLevel.PROTECTED) private ExceptionHandler<IOException> onFail = ExceptionHandler.noAction();


	// INIT
	@API public HttpFetch<T> method(HttpMethod method)
	{
		this.method = method;
		return this;
	}

	@API public HttpFetch<T> credentials(HttpCredentials credentials)
	{
		this.credentials = credentials;
		return this;
	}

	@API public HttpFetch<T> numberOfTries(int numberOfTries)
	{
		this.numberOfTries = numberOfTries;
		return this;
	}

	@API public HttpFetch<T> onFail(ExceptionHandler<IOException> onFail)
	{
		this.onFail = onFail;
		return this;
	}


	// FETCH
	@API public Optional<T> fetch()
	{
		int tries = method.retry ? numberOfTries : 1;

		for(int i = 0; i < tries; i++)
		{
			boolean last = i == (tries-1);

			Optional<T> fetchOptional = tryFetching(last);
			if(fetchOptional.isPresent())
				return fetchOptional;
			else
				logger.warn("Failed to fetch '{}'{}", url, last ? "" : ", retrying...");
		}

		return Optional.empty();
	}

	private Optional<T> tryFetching(boolean last)
	{
		CloseableHttpClient httpClient = buildHttpClient();
		HttpUriRequest httpRequest = method.getRequest(url);

		try(CloseableHttpResponse response = httpClient.execute(httpRequest);
				InputStream responseStream = response.getEntity().getContent())
		{
			if((response.getStatusLine().getStatusCode()/100) == 2) // success
				return convertToSpecific(responseStream);

			if(last)
				logger.warn(
						"Failed to fetch: {}: {}",
						response.getStatusLine(),
						org.apache.commons.io.IOUtils.toString(responseStream, StandardCharsets.UTF_8));
		}
		catch(IOException e)
		{
			if(last)
			{
				logger.debug("Failed to fetch {}, exception: {}", url, e);
				onFail.handle(e);
			}
		}

		return Optional.empty();
	}

	protected abstract Optional<T> convertToSpecific(InputStream inputStream);


	// HTTP
	private CloseableHttpClient buildHttpClient()
	{
		HttpClientBuilder clientBuilder = HttpClients.custom();
		injectCredentialsProvider(clientBuilder);

		return clientBuilder.build();
	}

	private void injectCredentialsProvider(HttpClientBuilder httpClientBuilder)
	{
		if(credentials == null)
			return;

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(credentials.getUsername(), credentials.getPassword()));

		httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
	}


	@RequiredArgsConstructor
	public enum HttpMethod
	{

		GET(true),
		HEAD(true),
		POST(false),
		PUT(false),
		DELETE(false),
		TRACE(true),
		OPTIONS(false),
		PATCH(false);

		// ATTRIBUTES
		private final boolean retry;


		// GETTERS
		private HttpUriRequest getRequest(AbstractURL url)
		{
			switch(this)
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

	}

}
