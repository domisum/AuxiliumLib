package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

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
	
	@API
	public static <T> Map<T, Integer> count(Stream<T> stream)
	{
		var counts = new HashMap<T, Integer>();
		stream.forEach(t -> counts.put(t, counts.getOrDefault(t, 0) + 1));
		return counts;
	}
	
	@API
	public static <T, I> Map<T, Integer> count(Collection<I> items, Function<I, T> extractor)
	{
		return count(items.stream().map(extractor));
	}
	
	@API
	public static <T> Map<T, Double> fractions(Map<T, ? extends Number> counts)
	{
		double sum = 0.0;
		for(var count : counts.values())
			sum += count.doubleValue();
		
		var fractions = new HashMap<T, Double>();
		for(var entry : counts.entrySet())
			fractions.put(entry.getKey(), sum == 0.0 ? 0 : (entry.getValue().doubleValue() / sum));
		return fractions;
	}
	
}
