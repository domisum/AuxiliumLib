package de.domisum.lib.auxilium.contracts.serialization;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.io.InputStream;

@API
public interface ToFromInputStreamSerializer<T>
{

	InputStream serializeToInputStream(T object) throws IOException;

	T deserializeFromInputStream(InputStream stream) throws IOException;

}
