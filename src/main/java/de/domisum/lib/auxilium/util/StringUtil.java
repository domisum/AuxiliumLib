package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil
{

	// CONCAT
	@API public static String repeat(String string, int repeats)
	{
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < repeats; i++)
			result.append(string);

		return result.toString();
	}


	@API public static String listToString(List<?> list, String delimiter)
	{
		StringBuilder combined = new StringBuilder();
		for(int i = 0; i < list.size(); i++)
		{
			combined.append(list.get(i));
			combined.append(((i+1) == list.size()) ? "" : delimiter);
		}

		return combined.toString();
	}

	@API public static String collectionToString(Collection<?> collection, String delimiter)
	{
		return listToString(new ArrayList<>(collection), delimiter);
	}

	@API public static String concatWithSpace(String... toConcat)
	{
		return concat(" ", toConcat);
	}

	@API public static String concat(String delimiter, String... toConcat)
	{
		return listToString(Arrays.asList(toConcat), delimiter);
	}

	@API public static String concatAsManyAsPossible(List<String> strings, String delimiter, int maxLength)
	{
		String lastPassing = "";
		for(int maxIndex = 0; maxIndex < strings.size(); maxIndex++)
		{
			List<String> stringSublist = strings.subList(0, maxIndex+1);
			String concat = collectionToString(stringSublist, delimiter);
			if(concat.length() <= maxLength)
				lastPassing = concat;
			else
				break;
		}

		return lastPassing;
	}


	// ANALYSIS
	@API public static String getCommonPrefix(String s1, String s2)
	{
		StringBuilder common = new StringBuilder();

		for(int ci = 0; ci < Math.min(s1.length(), s2.length()); ci++)
		{
			char c1 = s1.charAt(ci);
			char c2 = s2.charAt(ci);

			if(c1 != c2)
				break;

			common.append(c1);
		}

		return common.toString();
	}


	// MISC
	@API public static String escapeStringForRegex(String input)
	{
		List<Character> charactersToEscape = Arrays.asList(ArrayUtils.toObject("<([{\\^-=$!|]})?*+.>".toCharArray()));

		String escaped = input;
		for(int i = 0; i < escaped.length(); i++)
		{
			char charAt = escaped.charAt(i);
			if(charactersToEscape.contains(charAt))
			{
				escaped = escaped.substring(0, i)+("\\"+charAt)+escaped.substring(i+1);
				i++;
			}
		}

		return escaped;
	}

	@API public static String truncateStart(String string, int maxLength)
	{
		String toBeContinued = "...";

		if(string.length() <= maxLength)
			return string;

		int desiredBaseStringLength = maxLength-toBeContinued.length();
		return toBeContinued+string.substring(string.length()-desiredBaseStringLength);
	}

	@API public static String replaceLast(String string, String from, String to)
	{
		int pos = string.lastIndexOf(from);
		if(pos > -1)
			return string.substring(0, pos)+to+string.substring(pos+from.length(), string.length());

		return string;
	}

}
