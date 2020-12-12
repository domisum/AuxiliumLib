package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringListUtil
{
	
	private static final String DEFAULT_DELIMITER = ", ";
	
	
	@API
	public static <T> String list(Iterable<T> iterable, Function<T, Object> function, String delimiter)
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
	public static <T> String list(Iterable<T> iterable, Function<T, Object> function)
	{
		return list(iterable, function, DEFAULT_DELIMITER);
	}
	
	@API
	public static String list(Iterable<?> iterable, String delimiter)
	{
		return list(iterable, null, delimiter);
	}
	
	@API
	public static String list(Iterable<?> iterable)
	{
		return list(iterable, DEFAULT_DELIMITER);
	}
	
	@API
	public static String list(String delimiter, Object... items)
	{
		return list(Arrays.asList(items), delimiter);
	}
	
	
	@API
	public static <K, V> String list(Map<K, V> map, String delimiter,
		Function<K, ?> keyFunction, Function<V, ?> valueFunction)
	{
		var mappings = new ArrayList<String>();
		for(var entry : map.entrySet())
		{
			String mapping = PHR.r("{}->{}", keyFunction.apply(entry.getKey()), valueFunction.apply(entry.getValue()));
			mappings.add(mapping);
		}
		mappings.sort(Comparator.naturalOrder());
		
		return list(mappings, delimiter);
	}
	
	@API
	public static <T> String list(Map<?, T> map, Function<T, ?> valueFunction)
	{
		return list(map, DEFAULT_DELIMITER, StringListUtil::defaultMapEntryFunction, valueFunction);
	}
	
	@API
	public static String list(Map<?, ?> map)
	{
		return list(map, DEFAULT_DELIMITER, StringListUtil::defaultMapEntryFunction, StringListUtil::defaultMapEntryFunction);
	}
	
	
	@API
	public static String list(Stream<?> stream, String delimiter)
	{
		var elementsCollection = stream.collect(Collectors.toList());
		return list(elementsCollection, delimiter);
	}
	
	@API
	public static String list(Stream<?> stream)
	{
		return list(stream, DEFAULT_DELIMITER);
	}
	
	
	@API
	public static <T> String listInSingleQuotes(Iterable<T> iterable)
	{
		return list(iterable, s->"'"+s+"'");
	}
	
	@API
	public static <T> String listInDoubleQuotes(Iterable<T> iterable)
	{
		return list(iterable, s->"\""+s+"\"");
	}
	
	
	// UTIL
	private static <T> Object defaultMapEntryFunction(T t)
	{
		if(t instanceof String)
			return "'"+t+"'";
		
		return Objects.toString(t);
	}
	
}
