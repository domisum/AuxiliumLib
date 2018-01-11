package de.domisum.lib.auxilium.http.response.responses;

import de.domisum.lib.auxilium.http.response.RequestResponse;
import de.domisum.lib.auxilium.http.response.StatusLine;
import de.domisum.lib.auxilium.util.PHR;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RequestFailure<T> implements RequestResponse<T>
{

	private final StatusLine statusLine;
	private final String errorMessage;


	// OBJECT
	@Override public String toString()
	{
		return PHR.r("FAILURE | {}: {}", statusLine, errorMessage);
	}


	// GETTERS
	@Override public Optional<StatusLine> getStatusLine()
	{
		return Optional.of(statusLine);
	}

	@Override public Optional<T> getBody()
	{
		return Optional.empty();
	}

	@Override public Optional<String> getErrorMessage()
	{
		return Optional.of(errorMessage);
	}

}
