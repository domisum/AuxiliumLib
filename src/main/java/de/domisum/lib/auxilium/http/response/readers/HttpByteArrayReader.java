package de.domisum.lib.auxilium.http.response.readers;

import de.domisum.lib.auxilium.http.response.HttpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpByteArrayReader implements HttpResponseBodyReader<byte[]>
{

	// READ
	@Override public byte[] read(InputStream inputStream) throws IOException
	{
		return IOUtils.toByteArray(inputStream);
	}

}
