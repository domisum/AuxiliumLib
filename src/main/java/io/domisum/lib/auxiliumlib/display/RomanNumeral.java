package io.domisum.lib.auxiliumlib.display;

import com.google.common.collect.Range;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RomanNumeral
		implements CharSequence
{
	
	// CONSTANTS
	private static final Range<Integer> RANGE = Range.closed(1, 3999);
	
	// ATTRIBUTES
	@Getter
	private final int number;
	private String roman; // lazy init for possible performance boost
	
	
	// INIT
	public static RomanNumeral of(int number)
	{
		validateNumberInRange(number);
		return new RomanNumeral(number);
	}
	
	public static RomanNumeral parse(String numeral)
	{
		int number = parseRoman(numeral.toUpperCase());
		validateNumberInRange(number);
		return new RomanNumeral(number);
	}
	
	private static void validateNumberInRange(int number)
	{
		if(!RANGE.contains(number))
			throw new IllegalArgumentException("number "+number+" out of range: "+RANGE);
	}
	
	
	// CONVERSION
	private synchronized void ensureRomanGenerated()
	{
		if(roman == null)
			roman = romanOf(number);
	}
	
	private static String romanOf(int number)
	{
		int remaining = number;
		var roman = new StringBuilder();
		
		while(remaining > 0)
			for(var romanToken : RomanToken.values())
				if(remaining >= romanToken.getValue())
				{
					remaining -= romanToken.getValue();
					roman.append(romanToken.name());
					break;
				}
		
		return roman.toString();
	}
	
	private static int parseRoman(String roman)
	{
		String remaining = roman;
		int value = 0;
		
		RomanToken previousToken = null;
		int previousTokenCount = 0;
		while(!remaining.isEmpty())
		{
			var romanToken = getTokenAtStart(remaining);
			if(romanToken == null)
				throw new IllegalArgumentException("invalid characters in roman numeral: "+roman);
			
			if(previousToken != null && romanToken.getValue() > previousToken.getValue())
				throw new IllegalArgumentException(PHR.r("invalid order of tokens: {} ({} before {})", roman, previousToken, romanToken));
			
			value += romanToken.getValue();
			remaining = remaining.substring(romanToken.name().length());
			
			if(romanToken != previousToken)
				previousTokenCount = 0;
			previousToken = romanToken;
			previousTokenCount++;
			if(previousTokenCount > previousToken.getMaxRepeatNumber())
				throw new IllegalArgumentException(PHR.r("token {} repeated too often (max repeats: {})", previousToken, previousToken.getMaxRepeatNumber()));
		}
		
		return value;
	}
	
	private static RomanToken getTokenAtStart(String remaining)
	{
		for(var romanToken : RomanToken.values())
			if(remaining.startsWith(romanToken.name()))
				return romanToken;
		
		return null;
	}
	
	
	// ROMAN TOKEN
	@RequiredArgsConstructor
	private enum RomanToken
	{
		
		M(1000, 3),
		CM(900),
		D(500),
		CD(400),
		C(100, 3),
		XC(90),
		L(50),
		XL(40),
		X(10, 3),
		IX(9),
		V(5),
		IV(4),
		I(1, 3);
		
		
		// ATTRIBUTES
		@Getter
		private final int value;
		@Getter
		private final int maxRepeatNumber;
		
		
		// INIT
		RomanToken(int value)
		{
			this.value = value;
			maxRepeatNumber = 1;
		}
		
	}
	
	
	// CHAR SEQUENCE
	@Override
	public int length()
	{
		ensureRomanGenerated();
		return roman.length();
	}
	
	@Override
	public char charAt(int i)
	{
		ensureRomanGenerated();
		return roman.charAt(i);
	}
	
	@Override
	public CharSequence subSequence(int beginIndex, int endIndex)
	{
		ensureRomanGenerated();
		return roman.subSequence(beginIndex, endIndex);
	}
	
	@Override
	public String toString()
	{
		ensureRomanGenerated();
		return roman;
	}
	
}
