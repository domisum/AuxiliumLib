package de.domisum.lib.auxilium.util;

import java.util.Objects;

public class PHR
{

	public static String r(String text, Object... values)
	{
		String replacedText = text;

		for(Object value : values)
		{
			if(!replacedText.contains("{}"))
				throw new IllegalArgumentException("text doesn't contain enough placeholders");

			replacedText = replacedText.replaceFirst("\\{}", Objects.toString(value));
		}

		if(replacedText.contains("{}"))
			throw new IllegalArgumentException("text contains too many placeholders");

		return replacedText;
	}

}
