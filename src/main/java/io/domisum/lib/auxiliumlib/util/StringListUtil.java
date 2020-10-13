package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringListUtil
{
	
	// HORIZONTALLY
	@API
	public static String listHorizontally(List<?> list, String delimiter)
	{
		var combined = new StringBuilder();
		for(int i = 0; i < list.size(); i++)
		{
			var element = list.get(i);
			boolean isLastElement = (i+1) == list.size();
			
			combined.append(element);
			combined.append(isLastElement ? "" : delimiter);
		}
		
		return combined.toString();
	}
	
	@API
	public static String listHorizontally(List<?> list)
	{
		return listHorizontally(list, ", ");
	}
	
	
	@API
	public static String listHorizontally(Collection<?> collection, String delimiter)
	{
		return listHorizontally(new ArrayList<>(collection), delimiter);
	}
	
	@API
	public static String listHorizontally(Collection<?> collection)
	{
		return listHorizontally(collection, ", ");
	}
	
	
	@API
	public static <T> String listHorizontally(Map<?, T> map, String delimiter, Function<T, Object> valueFunction)
	{
		var mappings = new ArrayList<String>();
		for(var entry : map.entrySet())
		{
			String mapping = PHR.r("'{}'->'{}'", entry.getKey(), valueFunction.apply(entry.getValue()));
			mappings.add(mapping);
		}
		mappings.sort(Comparator.naturalOrder());
		
		return listHorizontally(mappings, delimiter);
	}
	
	@API
	public static <T> String listHorizontally(Map<?, T> map, Function<T, Object> valueFunction)
	{
		return listHorizontally(map, ", ", valueFunction);
	}
	
	
	@API
	public static String listHorizontally(Map<?, ?> map, String delimiter)
	{
		return listHorizontally(map, delimiter, v->v);
	}
	
	@API
	public static String listHorizontally(Map<?, ?> map)
	{
		return listHorizontally(map, ", ");
	}
	
	
	// VERTICALLY
	@API
	public static String listVertically(List<?> list)
	{
		var itemDisplays = new ArrayList<String>();
		for(var item : list)
			itemDisplays.add(PHR.r(" - {}", item.toString()));
		
		return listHorizontally(itemDisplays, "\n");
	}
	
	@API
	public static String listVertically(Collection<?> collection)
	{
		return listVertically(new ArrayList<>(collection));
	}
	
	@API
	public static String listVertically(Map<?, ?> map)
	{
		var entryStrings = new ArrayList<String>();
		for(var entry : map.entrySet())
		{
			String entryString = "'"+entry.getKey()+"' -> '"+entry.getValue()+"'";
			entryStrings.add(entryString);
		}
		
		Collections.sort(entryStrings);
		return listVertically(entryStrings);
	}
	
}
