package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil
{

	// COMMON
	@API public static void notNull(Object object, String variableName)
	{
		if(object == null)
			throw new IllegalArgumentException(variableName+" can't be null");
	}


	// STRING
	@API public static void notEmpty(String string, String variableName)
	{
		notNull(string, variableName);

		if(string.isEmpty())
			throw new IllegalArgumentException(variableName+" can't be empty");
	}


	// COMPARISON
	@API public static void greaterZero(double number, String variableName)
	{
		if(number <= 0)
			throw new IllegalArgumentException(variableName+" has to be greater than zero, but was "+number);
	}


	// INTERVAL
	@API public static void inIntervalInclIncl(double start, double end, double value, String variableName)
	{
		inInterval(start, true, end, true, value, variableName);
	}

	@API public static void inIntervalExclIncl(double start, double end, double value, String variableName)
	{
		inInterval(start, false, end, true, value, variableName);
	}

	private static void inInterval(
			double start, boolean startInclusive, double end, boolean endInclusive, double value, String variableName)
	{
		if(end < start)
			throw new IllegalArgumentException("start and end are the wrong way around");

		if((value < start) || (value > end))
			throw new IllegalArgumentException(
					variableName+" has to be in interval "+displayInterval(start, startInclusive, end, endInclusive)+", but was "
							+value);

		if((value == start) && !startInclusive)
			throw new IllegalArgumentException(
					variableName+" has to be in interval "+displayInterval(start, startInclusive, end, endInclusive)+", but was "
							+value+" (is equal to start and therefore excluded)");

		if((value == end) && !endInclusive)
			throw new IllegalArgumentException(
					variableName+" has to be in interval "+displayInterval(start, startInclusive, end, endInclusive)+", but was "
							+value+" (is equal to end and therefore excluded)");
	}

	private static String displayInterval(double start, boolean startInclusive, double end, boolean endInclusive)
	{
		return (startInclusive ? "[" : "]")+start+", "+end+(endInclusive ? "]" : "[");
	}

}
