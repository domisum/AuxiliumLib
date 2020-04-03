package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.util.java.Compare;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Collection;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil
{
	
	// COMMON
	@API
	public static void notNull(Object object, String variableName)
	{
		if(object == null)
			throw new IllegalArgumentException("'"+variableName+"' can't be null");
	}
	
	
	// STRING
	@API
	public static void notBlank(String string, String variableName)
	{
		notNull(string, variableName);
		
		if(string.isEmpty())
			throw new IllegalArgumentException("String '"+variableName+"' can't be blank");
	}
	
	
	// COLLECTIONS
	@API
	public static void notEmpty(Collection<?> collection, String collectionName)
	{
		notNull(collection, collectionName);
		if(collection.isEmpty())
			throw new IllegalArgumentException("Collection '"+collectionName+"' can't be empty");
	}
	
	
	// COMPARISON
	@API
	public static void greaterZero(double number, String variableName)
	{
		if(number <= 0)
			throw new IllegalArgumentException("double '"+variableName+"' has to be greater than zero, but was "+number);
	}
	
	
	// INTERVAL INT
	@API
	public static void inIntervalInclIncl(int start, int end, int value, String variableName)
	{
		inInterval(start, true, end, true, value, variableName);
	}
	
	@API
	public static void inIntervalExclIncl(int start, int end, int value, String variableName)
	{
		inInterval(start, false, end, true, value, variableName);
	}
	
	@API
	public static void inIntervalExclExcl(int start, int end, int value, String variableName)
	{
		inInterval(start, false, end, false, value, variableName);
	}
	
	private static void inInterval(int start, boolean startInclusive, int end, boolean endInclusive, int value, String variableName)
	{
		if(end < start)
			throw new IllegalArgumentException("Start and end are the wrong way around");
		
		if((value < start) || (value > end))
			throw new IllegalArgumentException("int '"+variableName+"' has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value);
		
		if((value == start) && !startInclusive)
			throw new IllegalArgumentException("int '"+variableName+"' has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value+" (is equal to start and therefore excluded)");
		
		if((value == end) && !endInclusive)
			throw new IllegalArgumentException("int '"+variableName+"+ has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value+" (is equal to end and therefore excluded)");
	}
	
	private static String displayInterval(int start, boolean startInclusive, int end, boolean endInclusive)
	{
		String startBracket = startInclusive ? "[" : "]";
		String endBracket = endInclusive ? "]" : "[";
		return startBracket+start+" to "+end+endBracket;
	}
	
	
	// INTERVAL DOUBLE
	@API
	public static void inIntervalInclIncl(double start, double end, double value, String variableName)
	{
		inInterval(start, true, end, true, value, variableName);
	}
	
	@API
	public static void inIntervalExclIncl(double start, double end, double value, String variableName)
	{
		inInterval(start, false, end, true, value, variableName);
	}
	
	@API
	public static void inIntervalExclExcl(double start, double end, double value, String variableName)
	{
		inInterval(start, false, end, false, value, variableName);
	}
	
	private static void inInterval(double start, boolean startInclusive, double end, boolean endInclusive, double value, String variableName)
	{
		if(end < start)
			throw new IllegalArgumentException("Start and end are the wrong way around");
		
		if((value < start) || (value > end))
			throw new IllegalArgumentException("double '"+variableName+"' has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value);
		
		if((value == start) && !startInclusive)
			throw new IllegalArgumentException("double '"+variableName+"' has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value+" (is equal to start and therefore excluded)");
		
		if((value == end) && !endInclusive)
			throw new IllegalArgumentException("double '"+variableName+"' has to be in interval "+
					displayInterval(start, startInclusive, end, endInclusive)+", but was "+value+" (is equal to end and therefore excluded)");
	}
	
	private static String displayInterval(double start, boolean startInclusive, double end, boolean endInclusive)
	{
		String startBracket = startInclusive ? "[" : "]";
		String endBracket = endInclusive ? "]" : "[";
		return startBracket+start+" to "+end+endBracket;
	}
	
	
	// DURATION
	@API
	public static void greaterThan(Duration a, Duration b, String aName, String bName)
	{
		if(Compare.greaterThanOrEqual(b, a))
			throw new IllegalArgumentException("Duration '"+aName+"' ("+DurationDisplay.display(a)+") has to be greater than '"+
					bName+"' ("+DurationDisplay.display(b)+"), but wasn't");
	}
	
	
	// SPECIAL VALUES
	@API
	public static void validatePortInRange(int port, String portName)
	{
		final int MAX_PORT_VALUE = 65535;
		inIntervalInclIncl(1, MAX_PORT_VALUE, port, portName);
	}
	
}
