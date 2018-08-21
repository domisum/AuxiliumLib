package de.domisum.lib.auxilium.mattp.request.bodies;

import de.domisum.lib.auxilium.mattp.request.MattpRequestBody;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@API
@RequiredArgsConstructor
public class MattpInputStreamBody implements MattpRequestBody
{

	private final InputStream inputStream;


	// BODY
	@Override public String getContentType()
	{
		return "application/octet-stream";
	}

	@Override public InputStream getAsInputStream()
	{
		return inputStream;
	}

}
