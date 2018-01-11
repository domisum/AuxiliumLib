package de.domisum.lib.auxilium.mattp;

import org.apache.http.impl.client.HttpClientBuilder;

public abstract class MattpAuthProvider
{

	protected abstract void provideAuthFor(HttpClientBuilder httpClientBuilder);

}
