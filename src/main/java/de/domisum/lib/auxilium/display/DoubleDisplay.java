package de.domisum.lib.auxilium.display;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.MathUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@API
public final class DoubleDisplay implements CharSequence
{

	// CONSTANTS
	private static final String SEPARATOR = "~";

	// ATTRIBUTES
	@Getter
	private final double number;
	private final String display;


	// INIT
	@API
	public static String display(double number)
	{
		return of(number).toString();
	}

	@API
	public static DoubleDisplay of(double number)
	{
		return new DoubleDisplay(number);
	}

	private DoubleDisplay(double number)
	{
		this.number = number;
		display = generateDisplay(number);
	}


	// DISPLAY GENERATION
	private static String generateDisplay(double number)
	{
		if(number < 0)
			return "-"+generateDisplay(Math.abs(number));

		SIPrefix closestSIPrefix = getClosestSiPrefix(number);
		double dividedBySiPrefix = number/closestSIPrefix.getValue();
		String siPrefixSuffix = (closestSIPrefix == SIPrefix.NONE) ? "" : (SEPARATOR+closestSIPrefix.name().toLowerCase());

		double dividedRounded = MathUtil.round(dividedBySiPrefix, 3);
		String display = dividedRounded+siPrefixSuffix;
		if((dividedRounded == 0) && (number != 0))
			display = "lessthan"+SEPARATOR+display;

		return display;
	}

	private static SIPrefix getClosestSiPrefix(double number)
	{
		if(number == 0)
			return SIPrefix.NONE;

		double numberBaseTenExponent = Math.log10(number);

		double minBaseTenExponentDelta = Double.MAX_VALUE;
		SIPrefix closestPrefix = null;
		for(SIPrefix siPrefix : SIPrefix.values())
		{
			double baseTenExponentDelta = Math.abs(siPrefix.getBaseTenExponent()-numberBaseTenExponent);
			if(baseTenExponentDelta < minBaseTenExponentDelta)
			{
				minBaseTenExponentDelta = baseTenExponentDelta;
				closestPrefix = siPrefix;
			}
		}

		return closestPrefix;
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


	// UNIT
	@RequiredArgsConstructor
	private enum SIPrefix
	{

		YOCTO(-24),
		ZEPTO(-21),
		ATTO(-18),
		FEMTO(-15),
		PICO(-12),
		NANO(-9),
		MICRO(-6),
		MILLI(-3),

		NONE(0),

		KILO(3),
		MEGA(6),
		GIGA(9),
		TERA(12),
		PETA(15),
		EXA(18),
		ZETTA(21),
		YOTTA(24);


		@Getter
		private final int baseTenExponent;
		@Getter
		private final double value;


		// INIT
		SIPrefix(int baseTenExponent)
		{
			this.baseTenExponent = baseTenExponent;
			value = Math.pow(10, baseTenExponent);
		}

	}

}
