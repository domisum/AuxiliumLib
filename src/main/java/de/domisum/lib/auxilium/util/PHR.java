package de.domisum.lib.auxilium.util;

import java.util.Objects;

public class PHR
{

	public static String r(String text, Object... values)
	{
		int numberOfFoundPlaceholders = 0;

		String replacedText = text;
		for(Object value : values)
		{
			if(!replacedText.contains("{}"))
				throw new IllegalArgumentException(
						"text doesn't contain enough placeholders ("+getPlaceHolderReport(numberOfFoundPlaceholders, values));

			replacedText = replacedText.replaceFirst("\\{}", Objects.toString(value));
			numberOfFoundPlaceholders++;
		}

		if(replacedText.contains("{}"))
			throw new IllegalArgumentException(
					"text contains too many placeholders ("+getPlaceHolderReport(numberOfFoundPlaceholders, values)+")");

		return replacedText;
	}

	private static String getPlaceHolderReport(int numberOfFoundPlaceholders, Object... values)
	{
		return "given values: "+values.length+", found placeholders: "+numberOfFoundPlaceholders;
	}

}
