package de.domisum.lib.auxilium.util.http;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@API
@RequiredArgsConstructor
public abstract class HttpFetch<T>
{

	// SETTINGS
	private final AbstractURL url;

	private HttpCredentials credentials;
	@Getter(AccessLevel.PROTECTED) private ExceptionHandler<Exception> onFail = ExceptionHandler.noAction();


	// INIT
	@API public HttpFetch<T> credentials(HttpCredentials credentials)
	{
		this.credentials = credentials;
		return this;
	}

	@API public HttpFetch<T> onFail(ExceptionHandler<Exception> onFail)
	{
		this.onFail = onFail;
		return this;
	}


	// FETCH
	@API public Optional<T> fetch()
	{
		CloseableHttpClient httpClient = buildHttpClient();
		HttpGet httpGet = new HttpGet(url.toString());

		try(CloseableHttpResponse response = httpClient.execute(httpGet);
				InputStream responseStream = response.getEntity().getContent())
		{
			return fetch(responseStream);
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}

	protected abstract Optional<T> fetch(InputStream inputStream);


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


}