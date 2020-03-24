package io.domisum.lib.auxiliumlib.contracts.serialization;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;
import java.io.InputStream;

@API
public interface ToFromInputStreamSerializer<T>
{

	InputStream serializeToInputStream(T object) throws IOException;

	T deserializeFromInputStream(InputStream stream) throws IOException;

}
