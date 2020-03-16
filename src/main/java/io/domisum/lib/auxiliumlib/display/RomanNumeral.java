package io.domisum.lib.auxiliumlib.display;

import com.google.common.collect.Range;
import io.domisum.lib.auxiliumlib.util.PHR;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RomanNumeral
{

	// CONSTANTS
	private static final Range<Integer> RANGE = Range.closed(1, 3999);

	// ATTRIBUTES
	@Getter
	private final int number;
	private final String roman;


	// INIT
	public static RomanNumeral of(int number)
	{
		validateNumberInRange(number);

		return new RomanNumeral(number, romanOf(number));
	}

	public static RomanNumeral of(String numeral)
	{
		int number = parse(numeral.toUpperCase());
		validateNumberInRange(number);

		return new RomanNumeral(number, romanOf(number));
	}

	private static void validateNumberInRange(int number)
	{
		if(!RANGE.contains(number))
			throw new IllegalArgumentException("number "+number+" out of range: "+RANGE);
	}


	// CONVERSION
	private static String romanOf(int number)
	{
		int remaining = number;
		StringBuilder roman = new StringBuilder();

		while(remaining > 0)
			for(RomanToken rt : RomanToken.values())
				if(remaining >= rt.value)
				{
					remaining -= rt.value;
					roman.append(rt.name());
					break;
				}

		return roman.toString();
	}

	private static int parse(String roman)
	{
		String remaining = roman;
		int value = 0;

		RomanToken lastToken = RomanToken.M;
		while(!remaining.isEmpty())
		{
			RomanToken romanToken = getHighestValueToken(remaining);
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
		for(RomanToken romanToken : RomanToken.values())
			if(remaining.startsWith(romanToken.name()))
				return romanToken;

		return null;
	}


	// OBJECT
	@Override
	public String toString()
	{
		return roman;
	}


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

}
