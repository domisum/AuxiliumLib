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
	public static <T> String listHorizontally(Iterable<T> iterable, Function<T, Object> function, String delimiter)
	{
		var display = new StringBuilder();
		boolean removeDelimiter = false;
		for(T element : iterable)
		{
			var elementDisplay = function == null ? element : function.apply(element);
			display.append(elementDisplay);
			display.append(delimiter);
			removeDelimiter = true;
		}
		
		if(removeDelimiter)
			display.delete(display.length()-delimiter.length(), display.length());
		
		return display.toString();
	}
	
	@API
	public static <T> String listHorizontally(Iterable<T> iterable, Function<T, Object> function)
	{
		return listHorizontally(iterable, function, DEFAULT_DELIMITER);
	}
	
	@API
	public static String listHorizontally(Iterable<?> iterable, String delimiter)
	{
		return listHorizontally(iterable, null,  delimiter);
	}
	
	@API
	public static String listHorizontally(Iterable<?> iterable)
	{
		return listHorizontally(iterable, DEFAULT_DELIMITER);
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