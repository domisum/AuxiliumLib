package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.List;

@APIUsage
public class TextUtil
{

	// BASIC
	@APIUsage
	public static String replaceLast(String string, String from, String to)
	{
		int pos = string.lastIndexOf(from);
		if(pos > -1)
			return string.substring(0, pos)+to+string.substring(pos+from.length(), string.length());

		return string;
	}

	@APIUsage
	public static String repeat(String string, int repeats)
	{
		String result = "";
		for(int i = 0; i < repeats; i++)
			result += string;

		return result;
	}


	// NUMBERS
	@APIUsage
	public static String asRomanNumeral(int number)
	{
		if((number < 1) || (number > 3999))
			throw new IllegalArgumentException(
					"Only numbers from 1 to 3999 can be represented as roman numerals; number given: "+number);

		String roman = "";
		for(; number >= 1000; number -= 1000)
			roman += "M";

		if(number >= 900)
		{
			roman += "CM";
			number -= 900;
		}
		else if(number >= 500)
		{
			roman += "D";
			number -= 500;
		}
		else if(number >= 400)
		{
			roman += "CD";
			number -= 400;
		}

		for(; number >= 100; number -= 100)
			roman += "C";

		if(number >= 90)
		{
			roman += "XC";
			number -= 90;
		}
		else if(number >= 50)
		{
			roman += "L";
			number -= 50;
		}
		else if(number >= 40)
		{
			roman += "XL";
			number -= 40;
		}

		for(; number >= 10; number -= 10)
			roman += "X";

		if(number == 9)
		{
			roman += "IX";
			number -= 9;
		}
		else if(number >= 5)
		{
			roman += "V";
			number -= 5;
		}
		else if(number == 4)
		{
			roman += "IV";
			number -= 4;
		}

		for(; number >= 1; number--)
			roman += "I";

		return roman;
	}


	// TO STRING
	@APIUsage
	public static String getListAsString(List<?> list)
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
