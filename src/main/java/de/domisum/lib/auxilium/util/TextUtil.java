package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.List;
import java.util.Objects;

@APIUsage
public class TextUtil
{

	// BASIC
	@APIUsage public static String replaceLast(String string, String from, String to)
	{
		int pos = string.lastIndexOf(from);
		if(pos > -1)
			return string.substring(0, pos)+to+string.substring(pos+from.length(), string.length());

		return string;
	}

	@APIUsage public static String repeat(String string, int repeats)
	{
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < repeats; i++)
			result.append(string);

		return result.toString();
	}


	// NUMBERS
	@APIUsage public static String asRomanNumeral(int number)
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


	// TO STRING
	@APIUsage public static String getListAsString(List<?> list)
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

}
