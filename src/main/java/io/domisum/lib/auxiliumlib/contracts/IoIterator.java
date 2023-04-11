package io.domisum.lib.auxiliumlib.contracts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IoIterator<T>
{
	
	boolean hasNext()
		throws IOException;
	
	T next()
		throws IOException;
	
	
	default List<T> toList()
		throws IOException
	{
		var items = new ArrayList<T>();
		while(hasNext())
			items.add(next());
		return items;
	}
	
}
