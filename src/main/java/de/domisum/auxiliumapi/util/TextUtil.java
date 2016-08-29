package de.domisum.auxiliumapi.util;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

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


	// COMPLEX
	@APIUsage
	public static List<String> splitTextIntoLines(String text, int maxLineLength)
	{
		List<String> lines = new ArrayList<>();

		String lastLine = null;
		while(text.length() > maxLineLength)
		{
			int lastSpaceIndex = -1;
			for(int i = 0; i < maxLineLength; i++)
				if(text.charAt(i) == ' ')
					lastSpaceIndex = i;

			int lineLength = maxLineLength;
			if(lastSpaceIndex != -1)
				lineLength = lastSpaceIndex;

			String line = text.substring(0, lineLength);

			// carry on the chat colors of the last line, each line's color is treated seperately
			if(lastLine != null)
				line = ChatColor.getLastColors(lastLine)+line;

			lines.add(line);
			text = text.substring(lineLength+1);
			lastLine = line;
		}

		// add last remaining bit, again add colors from line before
		String line = text;
		if(lastLine != null)
			line = ChatColor.getLastColors(lastLine)+line;
		lines.add(line);

		return lines;
	}

	public static List<String> splitTextIntoLinesConsideringNewLines(String text, int maxLineLength)
	{
		List<String> lines = new ArrayList<>();
		String[] splitByNewLine = text.split("\\n");
		for(String s : splitByNewLine)
			lines.addAll(splitTextIntoLines(s.trim(), maxLineLength));

		return lines;
	}

	@APIUsage
	public static String getSymbolMeter(char symbol, int value, int maxValue, String onColor, String offColor)
	{
		String meter = onColor+"";

		for(int i = 0; i < maxValue; i++)
			meter += (i == value ? offColor : "")+symbol;

		return meter+ChatColor.RESET;
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
