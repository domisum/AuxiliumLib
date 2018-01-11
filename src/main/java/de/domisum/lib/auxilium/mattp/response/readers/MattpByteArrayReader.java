package de.domisum.lib.auxilium.mattp.response.readers;

import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class MattpByteArrayReader implements MattpResponseBodyReader<byte[]>
{

	// READ
	@Override public byte[] read(InputStream inputStream) throws IOException
	{
		return IOUtils.toByteArray(inputStream);
	}

}
