package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.List;

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
		if((number < 1) || (number > 3999))
			throw new IllegalArgumentException(
					"Only numbers from 1 to 3999 can be represented as roman numerals; number given: "+number);

		int numberLeft = number;

		StringBuilder roman = new StringBuilder();
		for(; numberLeft >= 1000; numberLeft -= 1000)
			roman.append("M");

		if(numberLeft >= 900)
		{
			roman.append("CM");
			numberLeft -= 900;
		}
		else if(numberLeft >= 500)
		{
			roman.append("D");
			numberLeft -= 500;
		}
		else if(numberLeft >= 400)
		{
			roman.append("CD");
			numberLeft -= 400;
		}

		for(; numberLeft >= 100; numberLeft -= 100)
			roman.append("C");

		if(numberLeft >= 90)
		{
			roman.append("XC");
			numberLeft -= 90;
		}
		else if(numberLeft >= 50)
		{
			roman.append("L");
			numberLeft -= 50;
		}
		else if(numberLeft >= 40)
		{
			roman.append("XL");
			numberLeft -= 40;
		}

		for(; numberLeft >= 10; numberLeft -= 10)
			roman.append("X");

		if(numberLeft == 9)
		{
			roman.append("IX");
			numberLeft -= 9;
		}
		else if(numberLeft >= 5)
		{
			roman.append("V");
			numberLeft -= 5;
		}
		else if(numberLeft == 4)
		{
			roman.append("IV");
			numberLeft -= 4;
		}

		for(; numberLeft >= 1; numberLeft--)
			roman.append("I");

		return roman.toString();
	}


	// TO STRING
	@APIUsage public static String getListAsString(List<?> list)
	{
		String string = "list[";

		for(int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			String objectString = "null";
			if(obj != null)
				objectString = obj.toString();

			string += objectString;

			if(i < (list.size()-1))
				string += ";";
		}

		string += "]";
		return string;
	}

}
