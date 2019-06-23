package de.domisum.lib.auxilium.mattp.response.readers;

import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@API
@RequiredArgsConstructor
public class MattpInputStreamReader implements MattpResponseBodyReader<InputStream>
{

	// READ
	@Override
	public InputStream read(InputStream inputStream)
	{
		return inputStream;
	}

}
