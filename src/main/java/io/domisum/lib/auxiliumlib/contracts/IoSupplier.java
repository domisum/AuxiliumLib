package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoSupplier<T>
{
	
	@API
	T get()
		throws IOException;
	
}
