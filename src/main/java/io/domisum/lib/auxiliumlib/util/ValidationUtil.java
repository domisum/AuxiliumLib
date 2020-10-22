package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil
{
	
	// SHARED
	private static void throwIae(String message, Object... placeholderValues)
	{
		throw new IllegalArgumentException(PHR.r(message, placeholderValues));
	}
	
	
	// COMMON
	@API
	public static void notNull(Object object, String variableName)
	{
		if(object == null)
			throwIae("'{}' can't be null", variableName);
	}
	
	
	// STRING
	@API
	public static void notBlank(String string, String variableName)
	{
		notNull(string, variableName);
		if(string.isBlank())
			throwIae("String '{}' can't be blank", variableName);
	}
	
	
	// COLLECTIONS
	@API
	public static void notEmpty(Collection<?> collection, String collectionName)
	{
		notNull(collection, collectionName);
		if(collection.isEmpty())
			throwIae("Collection '{}' can't be empty", collectionName);
	}
	
	@API
	public static <T> void contains(Collection<T> collection, T element, String collectionName)
	{
		notNull(collection, collectionName);
		if(!collection.contains(element))
			throwIae("Collection '{}' has to contain element '{}', but didn't", collection, element);
	}
	
	@API
	public static <T> void containsAll(Collection<T> collection, Collection<? extends T> elements, String collectionName)
	{
		notNull(collection, collectionName);
		for(T element : elements)
			contains(collection, element, collectionName);
	}
	
	@API
	public static <T, E extends T> void containsAll(Collection<T> collection, E[] elements, String collectionName)
	{
		containsAll(collection, Arrays.asList(elements), collectionName);
	}
	
	@API
	public static void noNullElements(Collection<?> collection, String collectionName)
	{
		notNull(collection, collectionName);
		if(collection.contains(null))
			throwIae("Collection '{}' can't contain null, but did", collectionName);
	}
	
	
	// COMPARISON
	@API
	public static void greaterThanOrEqual(double value, double minimumIncl, String valueName, String minimumInclName)
	{
		if(value < minimumIncl)
			throwIae("double '{}' ({}) has to be greater than or equal to '{}' ({})", valueName, value, minimumInclName, minimumIncl);
	}
	
	@API
	public static void lessThanOrEqual(double value, double maximumIncl, String valueName, String maximumInclName)
	{
		if(value > maximumIncl)
			throwIae("double '{}' ({}) has to be less than or equal to '{}' ({})", valueName, value, maximumInclName, maximumIncl);
	}
	
	@API
	public static void lessThanOrEqual(int value, int maximumIncl, String valueName, String maximumInclName)
	{
		if(value > maximumIncl)
			throwIae("int '{}' ({}) has to be less than or equal to '{}' ({})", valueName, value, maximumInclName, maximumIncl);
	}
	
	@API
	public static void lessThanOrEqual(int value, int maximumIncl, String valueName)
	{
		if(value > maximumIncl)
			throwIae("int '{}' ({}) has to be less than or equal to {}", valueName, value, maximumIncl);
	}
	
	@API
	public static void greaterZero(double number, String variableName)
	{
		if(number <= 0)
			throwIae("double '{}' has to be greater than zero, but was {}", variableName, number);
	}
	
	@API
	public static void greaterZero(int number, String variableName)
	{
		if(number <= 0)
			throwIae("int '{}' has to be greater than zero, but was {}", variableName, number);
	}
	
	@API
	public static void notNegative(double number, String variableName)
	{
		if(number < 0)
			throwIae("double '{}' can't be negative, but was {}", variableName, number);
	}
	
	@API
	public static void notNegative(int number, String variableName)
	{
		if(number < 0)
			throwIae("int '{}' can't be negative, but was {}", variableName, number);
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
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("int '{}' has to be in interval {}, but was {}", variableName, intervalDisplay, value);
		}
		
		if((value == start) && !startInclusive)
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("int '{}' has to be in interval {}, but was {} (is equal to start and therefore excluded)",
				variableName, intervalDisplay, value);
		}
		
		if((value == end) && !endInclusive)
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("int '{}' has to be in interval {}, but was {} (is equal to end and therefore excluded)",
				variableName, intervalDisplay, value);
		}
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
	
	private static void inInterval(
		double start, boolean startInclusive, double end, boolean endInclusive, double value, String variableName)
	{
		if(end < start)
			throw new IllegalArgumentException("Start and end are the wrong way around");
		
		if((value < start) || (value > end))
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("double '{}' has to be in interval {}, but was {}", variableName, intervalDisplay, value);
		}
		
		if((value == start) && !startInclusive)
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("double '{}' has to be in interval {}, but was {} (is equal to start and therefore excluded)",
				variableName, intervalDisplay, value);
		}
		
		if((value == end) && !endInclusive)
		{
			String intervalDisplay = displayInterval(start, startInclusive, end, endInclusive);
			throwIae("double '{}' has to be in interval {}, but was {} (is equal to end and therefore excluded)",
				variableName, intervalDisplay, value);
		}
	}
	
	private static String displayInterval(double start, boolean startInclusive, double end, boolean endInclusive)
	{
		String startBracket = startInclusive ? "[" : "]";
		String endBracket = endInclusive ? "]" : "[";
		return startBracket+start+" to "+end+endBracket;
	}
	
	
	// DURATION
	@API
	public static void greaterThanOrEqual(
		Duration value, Duration minimumIncl, String valueName, String minimumInclName)
	{
		if(Compare.greaterThanOrEqual(value, minimumIncl))
			throwIae("Duration '{}' ({}) has to be greater than or equal to '{}' ({})",
				valueName, DurationDisplay.of(value), minimumInclName, DurationDisplay.of(minimumIncl));
	}
	
	@API
	public static void lessThanOrEqual(
		Duration value, Duration maximumIncl, String valueName, String maximumInclName)
	{
		if(Compare.lessThanOrEqual(value, maximumIncl))
			throwIae("Duration '{}' ({}) has to be less than or equal to '{}' ({})",
				valueName, DurationDisplay.of(value), maximumInclName, DurationDisplay.of(maximumIncl));
	}
	
	@API
	public static void greaterZero(Duration duration, String variableName)
	{
		notNull(duration, variableName);
		if(Compare.lessThanOrEqual(duration, Duration.ZERO))
			throwIae("Duration '{}' has to be greater than zero, but was {}",
				variableName, DurationDisplay.of(duration));
	}
	
	
	// SPECIAL VALUES
	@API
	public static void validatePortInRange(int port, String portName)
	{
		final int MAX_PORT_VALUE = 65535;
		inIntervalInclIncl(1, MAX_PORT_VALUE, port, portName);
	}
	
}
