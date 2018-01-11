package de.domisum.lib.auxilium.http.response.readers;

import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpVoidReader implements HttpResponseBodyReader<Void>
{

	// READ
	@Override public Void read(InputStream inputStream)
	{
		return null;
	}

}
