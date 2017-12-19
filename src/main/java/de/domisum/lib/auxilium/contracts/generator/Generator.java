package de.domisum.lib.auxilium.contracts.generator;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public interface Generator<I, O>
{

	O generate(I input);

}
