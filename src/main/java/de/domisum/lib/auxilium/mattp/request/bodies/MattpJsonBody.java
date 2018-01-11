package de.domisum.lib.auxilium.mattp.request.bodies;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.nio.charset.Charset;

@API
public class MattpJsonBody extends MattpPlaintextBody
{

	// INIT
	@API public MattpJsonBody(String text, Charset charset)
	{
		super(text, charset);
	}

	@API public MattpJsonBody(String text)
	{
		super(text);
	}


	// BODY
	@Override public String getContentType()
	{
		return "application/json";
	}

}
