package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public interface Converter<I, O>
{

	@API O convert(I input);

}
