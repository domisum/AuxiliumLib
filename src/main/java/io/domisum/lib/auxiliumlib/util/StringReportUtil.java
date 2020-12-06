package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringReportUtil
{
	
	@API
	public static String report(List<?> list)
	{
		var itemDisplays = new ArrayList<String>();
		for(var item : list)
			itemDisplays.add(PHR.r(" - {}", item.toString()));
		
		return StringListUtil.list(itemDisplays, "\n");
	}
	
	@API
	public static String report(Collection<?> collection)
	{
		return report(new ArrayList<>(collection));
	}
	
	@API
	public static String report(Map<?, ?> map)
	{
		return report(map,
			o->(o instanceof String) ?
				"'"+o+"'" :
				Objects.toString(o));
	}
	
	@API
	public static <T> String report(Map<?, T> map, Function<T, Object> valueFunction)
	{
		return report(map, k->(k instanceof String) ? "'"+k+"'" : k, valueFunction);
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
			
			String entryString = keyString+" -> "+valueString;
			entryStrings.add(entryString);
		}
		
		Collections.sort(entryStrings);
		return report(entryStrings);
	}
	
}
