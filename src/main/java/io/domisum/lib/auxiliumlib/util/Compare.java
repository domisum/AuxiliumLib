package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Compare
{
	
	@API
	public static <T extends Comparable<T>> boolean greaterThan(T a, T b)
	{
		return a.compareTo(b) > 0;
	}
	
	@API
	public static <T extends Comparable<T>> boolean greaterThanOrEqual(T a, T b)
	{
		return a.compareTo(b) >= 0;
	}
	
	@API
	public static <T extends Comparable<T>> boolean equal(T a, T b)
	{
		return a.compareTo(b) == 0;
	}
	
	@API
	public static <T extends Comparable<T>> boolean notEqual(T a, T b)
	{
		return a.compareTo(b) != 0;
	}
	
	@API
	public static <T extends Comparable<T>> boolean lessThanOrEqual(T a, T b)
	{
		return a.compareTo(b) <= 0;
	}
	
	@API
	public static <T extends Comparable<T>> boolean lessThan(T a, T b)
	{
		return a.compareTo(b) < 0;
	}
	
}
