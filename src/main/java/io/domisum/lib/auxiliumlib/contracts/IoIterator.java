package io.domisum.lib.auxiliumlib.contracts;

import java.io.IOException;

public interface IoIterator<T>
{
	
	boolean hasNext()
		throws IOException;
	
	T next()
		throws IOException;
	
}
