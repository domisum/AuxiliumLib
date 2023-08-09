package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoBiConsumer<T, V>
{
	
	@API
	void accept(T inputA, V inputB)
		throws IOException;
	
}
