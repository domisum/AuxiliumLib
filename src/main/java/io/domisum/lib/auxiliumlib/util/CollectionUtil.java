package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtil
{
	
	@API
	public static <T extends Comparable<T>> List<T> sorted(Collection<T> collection)
	{
		var list = new ArrayList<>(collection);
		Collections.sort(list);
		return list;
	}
	
}
