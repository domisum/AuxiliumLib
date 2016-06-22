package de.domisum.auxiliumapi.util.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class TextUtil
{

	// BASIC
	public static String replaceLast(String string, String from, String to)
	{
		int pos = string.lastIndexOf(from);
		if(pos > -1)
			return string.substring(0, pos) + to + string.substring(pos + from.length(), string.length());

		return string;
	}

	public static String repeat(String string, int repeats)
	{
		String result = "";
		for(int i = 0; i < repeats; i++)
			result += string;

		return result;
	}


	// COMPLEX
	public static List<String> splitTextIntoLines(String text, int maxLineLength)
	{
		List<String> lines = new ArrayList<String>();

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
				line = ChatColor.getLastColors(lastLine) + line;

			lines.add(line);
			text = text.substring(lineLength + 1);
			lastLine = line;
		}

		// add last remaining bit, again add colors from line before
		String line = text;
		if(lastLine != null)
			line = ChatColor.getLastColors(lastLine) + line;
		lines.add(line);

		return lines;
	}

	public static String getSymbolMeter(char symbol, int value, int maxValue, String onColor, String offColor)
	{
		String meter = onColor + "";

		for(int i = 0; i < maxValue; i++)
			meter += (i == value ? offColor : "") + symbol;

		return meter + ChatColor.RESET;
	}

}
