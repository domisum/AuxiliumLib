package de.domisum.lib.auxilium.contracts;

import de.domisum.lib.auxilium.util.java.annotations.API;

public interface SmartComparable<T> extends Comparable<T>
{

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

}
