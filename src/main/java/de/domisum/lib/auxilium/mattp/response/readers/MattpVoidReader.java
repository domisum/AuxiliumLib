package de.domisum.lib.auxilium.mattp.response.readers;

import de.domisum.lib.auxilium.mattp.response.MattpResponseBodyReader;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;

@API
@RequiredArgsConstructor
public class MattpVoidReader implements MattpResponseBodyReader<Void>
{

	// READ
	@Override public Void read(InputStream inputStream)
	{
		return null;
	}

}
