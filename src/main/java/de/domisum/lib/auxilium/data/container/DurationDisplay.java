package de.domisum.lib.auxilium.data.container;

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

	// CONSTANTS
	private static final double PRECISION_INTERVAL = 5000;
	private static final String ZERO_LEFT_SYMBOL = "--";

	// ATTRIBUTES
	@Getter private final Duration duration;
	private final String display;


	// INIT
	@API public static String display(Duration duration)
	{
		return of(duration).toString();
	}

	@API public static DurationDisplay of(Duration duration)
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
			return "0ms";

		if(isDurationTooSmall(duration))
			return "<1ms";

		Duration durationRemaining = duration;
		List<String> displayComponents = new ArrayList<>();
		for(TemporalUnit unit : TemporalUnit.values())
		{
			if(durationRemaining.isZero())
				break;

			if(isUnitTooFineForDuration(duration, unit) && !displayComponents.isEmpty())
				break;

			if(isUnitTooBigForDuration(durationRemaining, unit))
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

	private static boolean isDurationTooSmall(Duration duration)
	{
		TemporalUnit finestUnit = TemporalUnit.values()[TemporalUnit.values().length-1];
		return isUnitTooBigForDuration(duration, finestUnit);
	}

	private static boolean isUnitTooFineForDuration(Duration duration, TemporalUnit unit)
	{
		return howManyTimesFitsInside(duration, unit.getDuration()) > PRECISION_INTERVAL;
	}

	private static boolean isUnitTooBigForDuration(Duration durationRemaining, TemporalUnit unit)
	{
		return lessThan(durationRemaining, unit.getDuration());
	}


	private static double howManyTimesFitsInside(Duration base, Duration part)
	{
		return base.toMillis()/(double) part.toMillis();
	}

	private static boolean lessThan(Duration a, Duration b)
	{
		return a.compareTo(b) < 0;
	}


	// CHAR SEQUENCE
	@Override public int length()
	{
		return display.length();
	}

	@Override public char charAt(int i)
	{
		return display.charAt(i);
	}

	@Override public CharSequence subSequence(int beginIndex, int endIndex)
	{
		return display.subSequence(beginIndex, endIndex);
	}

	@Nonnull @Override public String toString()
	{
		return display;
	}


	// UNIT
	@RequiredArgsConstructor
	private enum TemporalUnit
	{

		DAY(Duration.ofDays(1), "d"),
		HOUR(Duration.ofHours(1), "h"),
		MINUTE(Duration.ofMinutes(1), "m"),
		SECOND(Duration.ofSeconds(1), "s"),
		MILLISECOND(Duration.ofMillis(1), "ms");

		@Getter private final Duration duration;
		@Getter private final String shortName;

	}

}
