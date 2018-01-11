package de.domisum.lib.auxilium.http.request.bodies;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.nio.charset.Charset;

@API
public class HttpJsonBody extends HttpPlaintextBody
{

	// INIT
	@API public HttpJsonBody(String text, Charset charset)
	{
		super(text, charset);
	}

	@API public HttpJsonBody(String text)
	{
		super(text);
	}


	// BODY
	@Override public String getContentType()
	{
		return "application/json";
	}

}
