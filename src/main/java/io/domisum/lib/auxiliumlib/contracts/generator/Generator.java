package io.domisum.lib.auxiliumlib.contracts.generator;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Generator<I, O>
{
	
	@API
	O generate(I input);
	
}
