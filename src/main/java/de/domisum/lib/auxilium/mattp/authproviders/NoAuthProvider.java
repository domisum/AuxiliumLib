package de.domisum.lib.auxilium.mattp.authproviders;

import de.domisum.lib.auxilium.mattp.MattpAuthProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class NoAuthProvider extends MattpAuthProvider
{

	@Override protected void provideAuthFor(HttpClientBuilder httpClientBuilder)
	{
		// do nothing
	}

}
