package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PHR
{

	// CONSTANTS
	@API public static final String PLACEHOLDER = "{}";


	// REPLACE
	public static String r(String text, Object... values)
	{
		List<Integer> placeholderIndices = new ArrayList<>();
		int lastIndex = 0;
		while(lastIndex != -1)
		{
			int index = text.indexOf(PLACEHOLDER, lastIndex+1);
			if(index != -1)
				placeholderIndices.add(index);

			lastIndex = index;
		}

		if(placeholderIndices.size() != values.length)
			throw new IllegalArgumentException("given values: "+values.length+", found placeholders: "+placeholderIndices.size());

		String filledInString = text;
		// iterate from back so inserting string doesn't change the indices of the other placeholders
		for(int i = placeholderIndices.size()-1; i >= 0; i--)
		{
			int index = placeholderIndices.get(i);
			filledInString = StringUtil.replaceLast(filledInString, PLACEHOLDER, Objects.toString(values[i]));
		}

		return filledInString;
	}

}
