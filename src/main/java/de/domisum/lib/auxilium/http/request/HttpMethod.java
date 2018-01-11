package de.domisum.lib.auxilium.http.request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HttpMethod
{

	GET(true),
	HEAD(true),
	OPTIONS(true),
	TRACE(true),

	POST(false),
	PUT(false),
	DELETE(false),
	PATCH(false);


	// ATTRIBUTES
	public final boolean safe;

}
