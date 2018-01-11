package de.domisum.lib.auxilium.http.response.responses;

import de.domisum.lib.auxilium.http.response.RequestResponse;
import de.domisum.lib.auxilium.http.response.StatusLine;
import de.domisum.lib.auxilium.util.PHR;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RequestSuccess<T> implements RequestResponse<T>
{

	private final StatusLine statusLine;
	private final T body;


	// OBJECT
	@Override public String toString()
	{
		return PHR.r("SUCCESS-{}: {}", statusLine, body);
	}


	// GETTERS
	@Override public Optional<StatusLine> getStatusLine()
	{
		return Optional.of(statusLine);
	}

	@Override public Optional<T> getBody()
	{
		return Optional.of(body);
	}

	@Override public Optional<String> getErrorMessage()
	{
		return Optional.empty();
	}

}
