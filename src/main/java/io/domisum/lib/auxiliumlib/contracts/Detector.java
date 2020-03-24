package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Detector<I, O>
{
	
	@API
	O detect(I input);
	
}
