package io.domisum.lib.auxiliumlib.display;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DoubleDisplay
	implements CharSequence
{
	
	// CONSTANTS
	private static final String SEPARATOR = "~";
	private static final int ROUNDING_DECIMAL_PLACES = 3;
	
	// ATTRIBUTES
	@Getter
	private final Double number;
	private String display; // lazy init for possible performance boost when used extremely often
	
	
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
	
	
	// DISPLAY GENERATION
	private synchronized void ensureDisplayGenerated()
	{
		if(display == null)
			display = generateDisplay(number);
	}
	
	private static String generateDisplay(Double number)
	{
		if(number == null)
			return "/";
		if(number < 0)
			return "-"+generateDisplay(Math.abs(number));
		if(Objects.equals(number, 0.0))
			return "0.0";
		
		var bestFittingSiPrefix = getBestFittingSiPrefix(number);
		double dividedNumber = number/bestFittingSiPrefix.getValue();
		String displaySuffix = (bestFittingSiPrefix == SiPrefix.NONE) ?
			"" :
			(SEPARATOR+bestFittingSiPrefix.getShortDisplay());
		
		double dividedRoundedNumber = MathUtil.round(dividedNumber, ROUNDING_DECIMAL_PLACES);
		if(dividedRoundedNumber == 0)
		{
			double lowestDisplayableRoundedNumber = 1/Math.pow(10, ROUNDING_DECIMAL_PLACES);
			return "below"+SEPARATOR+lowestDisplayableRoundedNumber+displaySuffix;
		}
		
		return dividedRoundedNumber+displaySuffix;
	}
	
	private static SiPrefix getBestFittingSiPrefix(double number)
	{
		double numberBaseTenExponent = Math.log10(number);
		
		if(numberBaseTenExponent >= -2 && numberBaseTenExponent < 3) // custom range
			return SiPrefix.NONE;
		
		var bestFittingSiPrefix = SiPrefix.getSmallest();
		for(var prefix : SiPrefix.values())
			if(numberBaseTenExponent >= prefix.getBaseTenExponent())
				bestFittingSiPrefix = prefix;
		return bestFittingSiPrefix;
	}
	
	
	// SIPREFIX
	@RequiredArgsConstructor
	private enum SiPrefix
	{
		
		YOCTO(-24, "y"),
		ZEPTO(-21, "z"),
		ATTO(-18, "a"),
		FEMTO(-15, "f"),
		PICO(-12, "p"),
		NANO(-9, "n"),
		MICRO(-6, "mic"),
		MILLI(-3, "milli"),
		
		NONE(0, null),
		
		KILO(3, "K"),
		MEGA(6, "M"),
		GIGA(9, "G"),
		TERA(12, "T"),
		PETA(15, "P"),
		EXA(18, "E"),
		ZETTA(21, "Z"),
		YOTTA(24, "Y");
		
		
		@Getter
		private final int baseTenExponent;
		@Getter
		private final double value;
		@Getter
		private final String shortDisplay;
		
		
		// INIT
		SiPrefix(int baseTenExponent, String shortDisplay)
		{
			this.baseTenExponent = baseTenExponent;
			value = Math.pow(10, baseTenExponent);
			this.shortDisplay = shortDisplay;
		}
		
		public static SiPrefix getSmallest()
		{
			return values()[0]; // because they are ordered
		}
		
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
