package io.domisum.lib.auxiliumlib.contracts;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Collection;

public interface SmartComparable<T>
	extends Comparable<T>
{
	
	// COMPARISON
	@API
	default boolean isLessThan(T other)
	{
		return compareTo(other) < 0;
	}
	
	@API
	default boolean isLessThanOrEqual(T other)
	{
		return compareTo(other) <= 0;
	}
	
	@API
	default boolean isEqual(T other)
	{
		return compareTo(other) == 0;
	}
	
	@API
	default boolean isGreaterThanOrEqual(T other)
	{
		return compareTo(other) >= 0;
	}
	
	@API
	default boolean isGreaterThan(T other)
	{
		return compareTo(other) > 0;
	}
	
	
	// ALL COMPARISON
	@API
	default boolean isLessThanAll(Collection<? extends T> collection)
	{
		for(T t : collection)
			if(!isLessThan(t))
				return false;
		
		return true;
	}
	
	@API
	default boolean isLessThanOrEqualAll(Collection<? extends T> collection)
	{
		for(T t : collection)
			if(!isLessThanOrEqual(t))
				return false;
		
		return true;
	}
	
	@API
	default boolean isEqualAll(Collection<? extends T> collection)
	{
		for(T t : collection)
			if(!isEqual(t))
				return false;
		
		return true;
	}
	
	@API
	default boolean isGreaterThanOrEqualAll(Collection<? extends T> collection)
	{
		for(T t : collection)
			if(!isGreaterThanOrEqual(t))
				return false;
		
		return true;
	}
	
	@API
	default boolean isGreaterThanAll(Collection<? extends T> collection)
	{
		for(T t : collection)
			if(!isGreaterThan(t))
				return false;
		
		return true;
	}
	
	
	// MIN MAX
	@API
	static <T extends SmartComparable<T>> T max(T a, T b)
	{
		return a.isLessThan(b) ? b : a;
	}
	
	@API
	static <T extends SmartComparable<T>> T min(T a, T b)
	{
		return b.isLessThan(b) ? b : a;
	}
	
	@API
	static <T extends SmartComparable<T>> T max(Collection<T> collection)
	{
		if(collection.isEmpty())
			throw new IllegalArgumentException("Can't get maximum from empty collection");
		
		T max = null;
		for(T t : collection)
			if(max == null || t.isGreaterThan(max))
				max = t;
		
		return max;
	}
	
	@API
	static <T extends SmartComparable<T>> T min(Collection<T> collection)
	{
		if(collection.isEmpty())
			throw new IllegalArgumentException("Can't get minimum from empty collection");
		
		T min = null;
		for(T t : collection)
			if(min == null || t.isLessThan(min))
				min = t;
		
		return min;
	}
	
}
