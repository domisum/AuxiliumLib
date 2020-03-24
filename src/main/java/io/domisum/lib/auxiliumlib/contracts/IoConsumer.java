package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoConsumer<T>
{
	
	@API
	void accept(T input)
			throws IOException;
	
}
