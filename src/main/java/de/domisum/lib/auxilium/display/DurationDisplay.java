package de.domisum.lib.auxilium.display;

import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@API
public final class DurationDisplay implements CharSequence
{

	// ATTRIBUTES
	@Getter
	private final Duration duration;
	private final String display;


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

	private DurationDisplay(Duration duration)
	{
		this.duration = duration;
		display = generateDisplay(duration);
	}


	// DISPLAY GENERATION
	private static String generateDisplay(Duration duration)
	{
		if(duration.isNegative())
			return "-"+generateDisplay(duration.abs());

		if(duration.isZero())
			return "0";

		Duration durationRemaining = duration;
		List<String> displayComponents = new ArrayList<>();
		for(TemporalUnit unit : TemporalUnit.values())
		{

			if(displayComponents.size() >= 2)
				break;

			if(isUnitTooBigForDuration(durationRemaining, unit) && displayComponents.isEmpty())
				continue;

			double ofUnitDecimal = howManyTimesFitsInside(durationRemaining, unit.getDuration());
			long ofUnit = (long) Math.floor(ofUnitDecimal);

			String unitComponent = ofUnit+unit.getShortName();
			displayComponents.add(unitComponent);

			Duration ofUnitDuration = unit.getDuration().multipliedBy(ofUnit);
			durationRemaining = durationRemaining.minus(ofUnitDuration);
		}

		return StringUtil.listToString(displayComponents, ":");
	}

	private static boolean isUnitTooBigForDuration(Duration durationRemaining, TemporalUnit unit)
	{
		return lessThan(durationRemaining, unit.getDuration());
	}


	private static double howManyTimesFitsInside(Duration base, Duration part)
	{
		return base.toNanos()/(double) part.toNanos();
	}

	private static boolean lessThan(Duration a, Duration b)
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
		return display.length();
	}

	@Override
	public char charAt(int i)
	{
		return display.charAt(i);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex)
	{
		return display.subSequence(beginIndex, endIndex);
	}

	@Nonnull
	@Override
	public String toString()
	{
		return display;
	}

}