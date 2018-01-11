package de.domisum.lib.auxilium.http.auth;

import de.domisum.lib.auxilium.http.HttpAuthProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class NoAuthProvider extends HttpAuthProvider
{

	@Override protected void provideAuthFor(HttpClientBuilder httpClientBuilder)
	{
		// do nothing
	}

}
