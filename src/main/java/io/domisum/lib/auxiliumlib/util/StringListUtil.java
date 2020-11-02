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
	
	private static final String DEFAULT_DELIMITER = ", ";
	
	
	// HORIZONTALLY
	@API
	public static <T> String listHorizontally(List<T> list, Function<T, Object> function, String delimiter)
	{
		var combined = new StringBuilder();
		for(int i = 0; i < list.size(); i++)
		{
			var element = list.get(i);
			var elementDisplay = function == null ? element : function.apply(element);
			combined.append(elementDisplay);
			
			boolean isLastElement = (i+1) == list.size();
			combined.append(isLastElement ? "" : delimiter);
		}
		
		return combined.toString();
	}
	
	@API
	public static <T> String listHorizontally(List<T> list, Function<T, Object> function)
	{
		return listHorizontally(list, function, DEFAULT_DELIMITER);
	}
	
	@API
	public static String listHorizontally(List<?> list)
	{
		return listHorizontally(list, DEFAULT_DELIMITER);
	}
	
	@API
	public static String listHorizontally(List<?> list, String delimiter)
	{
		return listHorizontally(list, null, delimiter);
	}
	
	
	@API
	public static String listHorizontally(Collection<?> collection, String delimiter)
	{
		return listHorizontally(new ArrayList<>(collection), delimiter);
	}
	
	@API
	public static String listHorizontally(Collection<?> collection)
	{
		return listHorizontally(collection, DEFAULT_DELIMITER);
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
		return listHorizontally(map, DEFAULT_DELIMITER, valueFunction);
	}
	
	
	@API
	public static String listHorizontally(Map<?, ?> map, String delimiter)
	{
		return listHorizontally(map, delimiter, v->v);
	}
	
	@API
	public static String listHorizontally(Map<?, ?> map)
	{
		return listHorizontally(map, DEFAULT_DELIMITER);
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
