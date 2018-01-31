package de.domisum.lib.auxilium.mattp.decorator;

import de.domisum.lib.auxilium.mattp.MattpRequestEnvoy;
import de.domisum.lib.auxilium.mattp.request.MattpRequest;
import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.mattp.response.RequestResponse;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.lang3.Validate;

@API
public class RetryingMattpRequestEnvoy<T> extends MattpRequestEnvoy<T>
{

	// CONSTANTS
	private static final int DEFAULT_NUMBER_OF_TRIES = 3;

	// SETTINGS
	private int numberOfTries = DEFAULT_NUMBER_OF_TRIES;


	// INIT
	@API public RetryingMattpRequestEnvoy(MattpRequest request, MattpResponseBodyReader<T> responseBodyReader)
	{
		super(request, responseBodyReader);

		if(!request.getMattpMethod().safe)
			throw new IllegalArgumentException("can't use retry with a non-safe http method");
	}


	// SETTERS
	@API public void setNumberOfTries(int numberOfTries)
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
