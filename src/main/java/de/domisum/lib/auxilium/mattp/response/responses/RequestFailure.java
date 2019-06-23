package de.domisum.lib.auxilium.mattp.response.responses;

import de.domisum.lib.auxilium.mattp.MattpHeaders;
import de.domisum.lib.auxilium.mattp.response.RequestResponse;
import de.domisum.lib.auxilium.mattp.response.StatusLine;
import de.domisum.lib.auxilium.util.PHR;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RequestFailure<T> implements RequestResponse<T>
{

	private final StatusLine statusLine;
	private final String errorMessage;


	// OBJECT
	@Override
	public String toString()
	{
		return PHR.r("FAILURE | {}: {}", statusLine, errorMessage);
	}


	// GETTERS
	@Override
	public boolean isSuccess()
	{
		return false;
	}

	@Override
	public Optional<StatusLine> getStatusLine()
	{
		return Optional.of(statusLine);
	}

	@Override
	public Optional<MattpHeaders> getHeaders()
	{
		return Optional.empty();
	}

	@Override
	public Optional<T> getContent()
	{
		return Optional.empty();
	}

	@Override
	public Optional<String> getLongErrorMessage()
	{
		return Optional.of(toString());
	}

	@Override
	public Optional<String> getShortErrorMessage()
	{
		return Optional.of(PHR.r("FAILURE | {}", statusLine));
	}

}
