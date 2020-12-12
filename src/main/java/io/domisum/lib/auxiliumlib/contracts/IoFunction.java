package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoFunction<I, O>
{
	
	@API
	O apply(I input)
		throws IOException;
	
}
