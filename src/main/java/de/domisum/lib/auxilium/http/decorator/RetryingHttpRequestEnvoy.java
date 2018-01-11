package de.domisum.lib.auxilium.http.decorator;

import de.domisum.lib.auxilium.http.HttpRequestEnvoy;
import de.domisum.lib.auxilium.http.request.HttpRequest;
import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.http.response.RequestResponse;
import de.domisum.lib.auxilium.util.java.annotations.API;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.commons.lang3.Validate;

@API
public class RetryingHttpRequestEnvoy<T> extends HttpRequestEnvoy<T>
{

	// CONSTANTS
	private static final int DEFAULT_NUMBER_OF_TRIES = 3;

	// SETTINGS
	private int numberOfTries = DEFAULT_NUMBER_OF_TRIES;


	// INIT
	@API public RetryingHttpRequestEnvoy(HttpRequest request, HttpResponseBodyReader<T> responseBodyReader)
	{
		super(request, responseBodyReader);

		if(!request.getHttpMethod().safe)
			throw new IllegalArgumentException("can't use retry with a non-safe http method");
	}


	// SETTERS
	@Setter public void setNumberOfTries(int numberOfTries)
	{
		Validate.isTrue(numberOfTries > 0, "numberOfTries has to be greater than 0, was "+numberOfTries);
		this.numberOfTries = numberOfTries;
	}


	// SEND
	@Override public RequestResponse<T> send()
	{
		RequestResponse<T> response = null;

		for(int i = 0; i < numberOfTries; i++)
		{
			response = super.send();
			if(response.isSuccess())
				return response;
		}

		return response;
	}

}
