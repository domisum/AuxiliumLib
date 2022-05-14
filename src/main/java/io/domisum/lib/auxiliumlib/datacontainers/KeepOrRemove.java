package io.domisum.lib.auxiliumlib.datacontainers;

import java.util.function.Function;

public enum KeepOrRemove
{
	
	KEEP,
	REMOVE;
	
	
	public static <T> void iterate(Iterable<T> iterable, Function<T, KeepOrRemove> inspectAction)
	{
		var iterator = iterable.iterator();
		while(iterator.hasNext())
		{
			var whatDo = inspectAction.apply(iterator.next());
			if(whatDo == KeepOrRemove.REMOVE)
				iterator.remove();
		}
	}
	
}
