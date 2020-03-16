package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
public interface Converter<I, O>
{

	@API
	O convert(I input);

}
