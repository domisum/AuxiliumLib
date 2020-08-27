package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
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
		if(string.isBlank())
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
	
	@API
	public static <T> void contains(Collection<T> collection, T element, String collectionName)
	{
		notNull(collection, collectionName);
		if(!collection.contains(element))
			throw new IllegalArgumentException("Collection '"+collectionName+"' has to contain element '"+element+"', but didn't");
	}
	
	@API
	public static void noNullElements(Collection<?> collection, String collectionName)
	{
		notNull(collection, collectionName);
		if(collection.contains(null))
			throw new IllegalArgumentException("Collection '"+collectionName+"' can't contain null, but did");
	}
	
	
	// COMPARISON
	@API
	public static void greaterThanOrEqual(double value, double minimumIncl, String valueName, String minimumInclName)
	{
		if(value < minimumIncl)
			throw new IllegalArgumentException("double '"+valueName+"' ("+value+") has to be greater than or equal to '"+
				minimumInclName+"' ("+minimumIncl+"), but wasn't");
	}
	
	@API
	public static void lessThanOrEqual(double value, double maximumIncl, String valueName, String maximumInclName)
	{
		if(value > maximumIncl)
			throw new IllegalArgumentException("double '"+valueName+"' ("+value+") has to be less than or equal to '"+
				maximumInclName+"' ("+maximumIncl+"), but wasn't");
	}
	
	@API
	public static void lessThanOrEqual(int value, int maximumIncl, String valueName, String maximumInclName)
	{
		if(value > maximumIncl)
			throw new IllegalArgumentException("double '"+valueName+"' ("+value+") has to be less than or equal to '"+
				maximumInclName+"' ("+maximumIncl+"), but wasn't");
	}
	
	@API
	public static void greaterZero(double number, String variableName)
	{
		if(number <= 0)
			throw new IllegalArgumentException("double '"+variableName+"' has to be greater than zero, but was "+number);
	}
	
	@API
	public static void greaterZero(int number, String variableName)
	{
		if(number <= 0)
			throw new IllegalArgumentException("int '"+variableName+"' has to be greater than zero, but was "+number);
	}
	
	@API
	public static void notNegative(double number, String variableName)
	{
		if(number < 0)
			throw new IllegalArgumentException("double '"+variableName+"' can't be negative, but was "+number);
	}
	
	@API
	public static void notNegative(int number, String variableName)
	{
		if(number < 0)
			throw new IllegalArgumentException("int '"+variableName+"' can't be negative, but was "+number);
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
	public static void greaterThanOrEqual(Duration value, Duration minimumIncl, String valueName, String minimumInclName)
	{
		if(Compare.greaterThanOrEqual(minimumIncl, value))
			throw new IllegalArgumentException("Duration '"+valueName+"' ("+DurationDisplay.display(value)+
				") has to be greater than or equal to '"+minimumInclName+"' ("+DurationDisplay.display(minimumIncl)+"), but wasn't");
	}
	
	@API
	public static void lessThanOrEqual(Duration value, Duration maximumIncl, String valueName, String maximumInclName)
	{
		if(Compare.lessThanOrEqual(maximumIncl, value))
			throw new IllegalArgumentException("Duration '"+valueName+"' ("+DurationDisplay.display(value)+
				") has to be less than or equal to '"+maximumInclName+"' ("+DurationDisplay.display(maximumIncl)+"), but wasn't");
	}
	
	@API
	public static void greaterZero(Duration duration, String variableName)
	{
		notNull(duration, variableName);
		if(Compare.lessThanOrEqual(duration, Duration.ZERO))
			throw new IllegalArgumentException("Duration '"+variableName+"' has to be greater than zero, but was "+duration);
	}
	
	
	// SPECIAL VALUES
	@API
	public static void validatePortInRange(int port, String portName)
	{
		final int MAX_PORT_VALUE = 65535;
		inIntervalInclIncl(1, MAX_PORT_VALUE, port, portName);
	}
	
}
