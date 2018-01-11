package de.domisum.lib.auxilium.mattp.response;

import java.io.IOException;
import java.io.InputStream;

public interface MattpResponseBodyReader<T>
{

	T read(InputStream inputStream) throws IOException;

}
