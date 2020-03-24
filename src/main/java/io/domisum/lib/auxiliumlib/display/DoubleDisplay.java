package io.domisum.lib.auxiliumlib.display;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
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
	private final Double number;
	private final String display;


	// INIT
	@API
	public static String display(Double number)
	{
		return of(number).toString();
	}

	@API
	public static DoubleDisplay of(Double number)
	{
		return new DoubleDisplay(number);
	}

	@API
	public static String display(double number)
	{
		return display((Double) number);
	}

	@API
	public static DoubleDisplay of(double number)
	{
		return of((Double) number);
	}


	private DoubleDisplay(Double number)
	{
		this.number = number;
		display = generateDisplay(number);
	}


	// DISPLAY GENERATION
	private static String generateDisplay(Double number)
	{
		if(number == null)
			return "/";

		if(number < 0)
			return "-"+generateDisplay(Math.abs(number));

		SIPrefix bestSiPrefix = getBestSiPrefix(number);
		double dividedBySiPrefix = number/bestSiPrefix.getValue();
		String siPrefixSuffix = (bestSiPrefix == SIPrefix.NONE) ? "" : (SEPARATOR+bestSiPrefix.name().toLowerCase());

		double dividedRounded = MathUtil.round(dividedBySiPrefix, 3);
		String display = dividedRounded+siPrefixSuffix;
		if((dividedRounded == 0) && (number != 0))
			display = "lessthan"+SEPARATOR+display;

		return display;
	}

	private static SIPrefix getBestSiPrefix(double number)
	{
		if(number == 0)
			return SIPrefix.NONE;

		double numberBaseTenExponent = Math.log10(number);

		SIPrefix bestSIPrefix = SIPrefix.YOCTO;
		for(SIPrefix prefix : SIPrefix.values())
			if(numberBaseTenExponent >= prefix.getBaseTenExponent())
				bestSIPrefix = prefix;

		return bestSIPrefix;
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
