package de.domisum.lib.auxilium.http.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// STATUS LINE
@RequiredArgsConstructor
public class StatusLine
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
