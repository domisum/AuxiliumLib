package io.domisum.lib.auxiliumlib.contracts.serdes;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.File;
import java.io.IOException;

@API
public interface FileSerdes<T>
{
	
	@API
	File serializeToFile(T object)
		throws IOException;
	
	@API
	T deserializeFromFile(File file)
		throws IOException;
	
}
