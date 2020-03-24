package io.domisum.lib.auxiliumlib.display;

import com.google.common.collect.Range;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RomanNumeral implements CharSequence
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

	public static RomanNumeral of(String numeral)
	{
		int number = parse(numeral.toUpperCase());
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
				if(remaining >= romanToken.value)
				{
					remaining -= romanToken.value;
					roman.append(romanToken.name());
					break;
				}

		return roman.toString();
	}

	private static int parse(String roman)
	{
		String remaining = roman;
		int value = 0;

		var lastToken = RomanToken.M;
		while(!remaining.isEmpty())
		{
			var romanToken = getHighestValueToken(remaining);
			if(romanToken == null)
				throw new IllegalArgumentException("invalid characters in roman numeral: "+roman);

			if(romanToken.value > lastToken.value)
				throw new IllegalArgumentException(PHR.r("invalid order of tokens: {} ({} before {})",
						roman,
						lastToken,
						romanToken
				));

			value += romanToken.value;
			remaining = remaining.substring(romanToken.name().length());
			lastToken = romanToken;
		}

		return value;
	}

	private static RomanToken getHighestValueToken(String remaining)
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

		M(1000),
		CM(900),
		D(500),
		CD(400),
		C(100),
		XC(90),
		L(50),
		XL(40),
		X(10),
		IX(9),
		V(5),
		IV(4),
		I(1);


		private final int value;

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
