package de.domisum.lib.auxilium.http.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpResponse<T>
{

	private final StatusLine statusLine;
	private final T body;
	private final String errorMessage;


	// INIT
	public static <T> HttpResponse<T> ofConnectionError(String errorMessage)
	{
		return new HttpResponse<>(null, null, "Connection error: "+errorMessage);
	}

	public static <T> HttpResponse<T> ofHttpSuccess(StatusLine statusLine, T response)
	{
		return new HttpResponse<>(statusLine, response, null);
	}

	public static <T> HttpResponse<T> ofHttpError(StatusLine statusLine, String errorMessage)
	{
		return new HttpResponse<>(statusLine, null, errorMessage);
	}


	// GETTERS
	public Optional<StatusLine> getStatusLine()
	{
		return Optional.ofNullable(statusLine);
	}

	public Optional<T> getBody()
	{
		return Optional.ofNullable(body);
	}

	public Optional<String> getErrorMessage()
	{
		return Optional.ofNullable(errorMessage);
	}


	// STATUS LINE
	@RequiredArgsConstructor
	public static class StatusLine
	{

		@Getter private final String protocolVersion;
		@Getter private final int statusCode;
		@Getter private final String reasonPhrase;


		// OBJECT
		@Override public String toString()
		{
			return protocolVersion+" "+statusCode+" "+reasonPhrase;
		}

	}

}
