package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface IoPredicate<T>
{
	
	@API
	boolean test(T input)
		throws IOException;
	
}
