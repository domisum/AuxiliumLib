package de.domisum.lib.auxilium.contracts.serialization;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.io.InputStream;

@API
public interface ByteStreamSerializer<T>
{

	InputStream serialize(T object) throws IOException;

	T deserialize(InputStream stream) throws IOException;

}
