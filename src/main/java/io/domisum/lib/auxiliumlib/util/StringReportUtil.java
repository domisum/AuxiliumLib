package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.contracts.IoFunction;
import io.domisum.lib.auxiliumlib.exceptions.ProgrammingError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringReportUtil
{
	
	private static final String BULLET = " - ";
	private static final String INDENT = " ".repeat(BULLET.length());
	private static final String QUOT = "'";
	
	
	@API
	public static <T> String ioMapReport(List<T> list, IoFunction<T, Object> valueFunction)
		throws IOException
	{
		var itemDisplays = new ArrayList<String>();
		for(var item : list)
		{
			String valueDisplay = Objects.toString(valueFunction.apply(item));
			
			valueDisplay = StringUtil.indent(valueDisplay, INDENT);
			if(valueDisplay.length() >= INDENT.length())
				valueDisplay = valueDisplay.substring(INDENT.length());
			valueDisplay = BULLET + valueDisplay;
			
			itemDisplays.add(valueDisplay);
		}
		return StringListUtil.list(itemDisplays, "\n");
	}
	
	@API
	public static <T> String report(List<T> list, Function<T, Object> valueFunction)
	{
		try
		{
			return ioMapReport(list, valueFunction::apply);
		}
		catch(IOException e)
		{
			throw new ProgrammingError("Should never happen", e);
		}
	}
	
	@API
	public static <T> String report(List<T> list)
	{
		return report(list, Objects::toString);
	}
	
	@API
	public static String report(Collection<?> collection)
	{
		return report(new ArrayList<>(collection));
	}
	
	
	@API
	public static String report(Map<?, ?> map)
	{
		return report(map, Objects::toString);
	}
	
	@API
	public static <T> String report(Map<?, T> map, Function<T, Object> valueFunction)
	{
		return report(map, k -> (k instanceof String) ? QUOT + k + QUOT : k, valueFunction);
	}
	
	@API
	public static <K, V> String report(Map<K, V> map,
									   Function<K, Object> keyFunction, Function<V, Object> valueFunction)
	{
		var entryStrings = new ArrayList<String>();
		for(var entry : map.entrySet())
		{
			String keyString = Objects.toString(keyFunction.apply(entry.getKey()));
			String valueString = Objects.toString(valueFunction.apply(entry.getValue()));
			
			String entryString = keyString + " -> " + valueString;
			entryStrings.add(entryString);
		}
		
		Collections.sort(entryStrings);
		return report(entryStrings);
	}
	
}
