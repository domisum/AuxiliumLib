package io.domisum.lib.auxiliumlib.contracts.generator;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public interface Generator<I, O>
{

	O generate(I input);

}
