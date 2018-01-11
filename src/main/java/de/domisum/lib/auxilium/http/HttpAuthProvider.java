package de.domisum.lib.auxilium.http;

import org.apache.http.impl.client.HttpClientBuilder;

public abstract class HttpAuthProvider
{

	protected abstract void provideAuthFor(HttpClientBuilder httpClientBuilder);

}
