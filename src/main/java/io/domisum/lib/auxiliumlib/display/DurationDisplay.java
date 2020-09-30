package io.domisum.lib.auxiliumlib.display;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationDisplay
	implements CharSequence
{
	
	// ATTRIBUTES
	@Getter
	private final Duration duration;
	private String display; // lazy init for possible performance boost when used extremely often
	
	
	// INIT
	@API
	public static String display(Duration duration)
	{
		return of(duration).toString();
	}
	
	@API
	public static DurationDisplay of(Duration duration)
	{
		return new DurationDisplay(duration);
	}
	
	
	// DISPLAY GENERATION
	private synchronized void ensureDisplayGenerated()
	{
		if(display == null)
			display = generateDisplay(duration);
	}
	
	private static String generateDisplay(Duration duration)
	{
		if(duration == null)
			return "/";
		
		if(duration.isNegative())
			return "-"+generateDisplay(duration.abs());
		
		if(duration.isZero())
			return "0";
		
		var durationRemaining = duration;
		var displayComponents = new ArrayList<String>();
		for(var temporalUnit : TemporalUnit.values())
		{
			if(displayComponents.size() >= 2)
				break;
			
			if(isUnitTooBigForDuration(durationRemaining, temporalUnit) && displayComponents.isEmpty())
				continue;
			
			double ofUnitDecimal = howManyTimesFitsInside(durationRemaining, temporalUnit.getDuration());
			long ofUnit = (long) Math.floor(ofUnitDecimal);
			
			String unitComponent = ofUnit+temporalUnit.getShortName();
			displayComponents.add(unitComponent);
			
			var ofUnitDuration = temporalUnit.getDuration().multipliedBy(ofUnit);
			durationRemaining = durationRemaining.minus(ofUnitDuration);
		}
		
		return StringUtil.listToString(displayComponents, ":");
	}
	
	private static boolean isUnitTooBigForDuration(Duration durationRemaining, TemporalUnit unit)
	{
		return isLessThan(durationRemaining, unit.getDuration());
	}
	
	
	private static double howManyTimesFitsInside(Duration base, Duration part)
	{
		return base.toNanos()/(double) part.toNanos();
	}
	
	private static boolean isLessThan(Duration a, Duration b)
	{
		return a.compareTo(b) < 0;
	}
	
	
	// UNIT
	@RequiredArgsConstructor
	private enum TemporalUnit
	{
		
		DAY(Duration.ofDays(1), "d"),
		HOUR(Duration.ofHours(1), "h"),
		MINUTE(Duration.ofMinutes(1), "m"),
		SECOND(Duration.ofSeconds(1), "s"),
		MILLISECOND(Duration.ofMillis(1), "ms"),
		MICROSECOND(Duration.ofNanos(1000), "us"),
		NANOSECOND(Duration.ofNanos(1), "ns");
		
		
		@Getter
		private final Duration duration;
		@Getter
		private final String shortName;
		
	}
	
	
	// CHAR SEQUENCE
	@Override
	public int length()
	{
		ensureDisplayGenerated();
		return display.length();
	}
	
	@Override
	public char charAt(int i)
	{
		ensureDisplayGenerated();
		return display.charAt(i);
	}
	
	@Override
	public CharSequence subSequence(int beginIndex, int endIndex)
	{
		ensureDisplayGenerated();
		return display.subSequence(beginIndex, endIndex);
	}
	
	@Override
	public String toString()
	{
		ensureDisplayGenerated();
		return display;
	}
	
}
