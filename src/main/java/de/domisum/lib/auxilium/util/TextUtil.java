package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextUtil
{

	// NUMBERS
	@API public static String asRomanNumeral(int number)
	{
		if(number < 1 || number > 3999)
			throw new IllegalArgumentException(
					"Only numbers from 1 to 3999 can be represented as roman numerals; number given: "+number);

		int numberLeft = number;
		StringBuilder roman = new StringBuilder();

		int[] numbers = new int[] {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
		String[] symbols = new String[] {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

		for(int i = 0; i < numbers.length; i++)
		{
			// append until no longer applicable
			boolean changed = true;
			while(changed)
			{
				int newNumber = romanNumeralAppendIfApplicable(numberLeft, roman, numbers[i], symbols[i]);

				changed = newNumber != numberLeft;
				numberLeft = newNumber;
			}
		}

		return roman.toString();
	}

	private static int romanNumeralAppendIfApplicable(int numberLeft, StringBuilder romanNumeral, int minimum, String toAppend)
	{
		if(numberLeft < minimum)
			return numberLeft;

		romanNumeral.append(toAppend);
		return numberLeft-minimum;
	}

	@API public static String secondsToMinuteSeconds(int seconds)
	{
		boolean negative = seconds < 0;
		if(negative)
			seconds = -seconds;

		int leftoverSeconds = seconds%60;
		return (negative ? "-" : "")+(seconds/60)+":"+(leftoverSeconds < 10 ? "0" : "")+leftoverSeconds;
	}


	// TO STRING
	@API public static String getListAsString(List<?> list)
	{
		StringBuilder string = new StringBuilder();

		for(int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			string.append(Objects.toString(obj));

			if(i < (list.size()-1))
				string.append(", ");
		}

		return string.toString();
	}


	// SPECIAL CHARACTERS
	@API public static String replaceUmlauts(String input)
	{
		String withoutUmlauts = input;

		withoutUmlauts = withoutUmlauts.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
		withoutUmlauts = withoutUmlauts.replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");

		return withoutUmlauts;
	}

	@API public static String getGenitive(String of)
	{
		if(of.endsWith("s") || of.endsWith("x"))
			return of+"'";

		return of+"'s";
	}

}
