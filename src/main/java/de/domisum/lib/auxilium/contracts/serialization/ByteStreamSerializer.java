package de.domisum.lib.auxilium.contracts.serialization;

import java.io.IOException;
import java.io.InputStream;

public interface ByteStreamSerializer<T>
{

	InputStream serialize(T object) throws IOException;

	T deserialize(InputStream stream) throws IOException;

}
