package io.domisum.lib.auxiliumlib.contracts.serialization;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;
import java.io.InputStream;

@API
public interface ToFromInputStreamSerializer<T>
{
	
	@API
	InputStream serializeToInputStream(T object)
			throws IOException;
	
	@API
	T deserializeFromInputStream(InputStream stream)
			throws IOException;
	
}
