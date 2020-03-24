package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Analyzer<I, O>
{

	@API
	O analyze(I input);

}
