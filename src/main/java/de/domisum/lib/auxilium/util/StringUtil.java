package de.domisum.lib.auxilium.util;

import com.google.common.collect.Sets;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil
{

	// CONCAT
	@API
	public static String repeat(String string, int repeats)
	{
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < repeats; i++)
			result.append(string);

		return result.toString();
	}


	@API
	public static String listToString(List<?> list, String delimiter)
	{
		StringBuilder combined = new StringBuilder();
		for(int i = 0; i < list.size(); i++)
		{
			combined.append(list.get(i));
			combined.append(((i+1) == list.size()) ? "" : delimiter);
		}

		return combined.toString();
	}

	@API
	public static String collectionToString(Collection<?> collection, String delimiter)
	{
		return listToString(new ArrayList<>(collection), delimiter);
	}

	@API
	public static String concatWithSpace(String... toConcat)
	{
		return concat(" ", toConcat);
	}

	@API
	public static String concat(String delimiter, String... toConcat)
	{
		return listToString(Arrays.asList(toConcat), delimiter);
	}

	@API
	public static String concatAsManyAsPossible(List<String> stringsInput, String delimiter, int maxLength)
	{
		List<String> strings = new ArrayList<>(stringsInput);

		String lastPassing = "";
		for(String string : strings)
		{
			String delimiterBeforeCurrent = lastPassing.isEmpty() ? "" : delimiter;

			String withCurrent = lastPassing+delimiterBeforeCurrent+string;
			if(withCurrent.length() <= maxLength)
				lastPassing = withCurrent;
		}

		return lastPassing;
	}


	// ANALYSIS
	@API
	public static String getCommonPrefix(String s1, String s2)
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


	// ESCAPING
	@API
	public static String escapeStringForRegex(String input)
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

	@API
	public static String escapeUrlParameterString(String urlString)
	{
		try
		{
			return URLEncoder.encode(urlString, "UTF-8");
		}
		catch(UnsupportedEncodingException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API
	public static String escapeUrlPathString(String input)
	{
		// https://stackoverflow.com/a/4605816/4755690

		StringBuilder result = new StringBuilder();
		for(char c : input.toCharArray())
			if(isCharUnsafe(c))
			{
				result.append('%');
				result.append(charToHex(c/16));
				result.append(charToHex(c%16));
			}
			else
				result.append(c);

		return result.toString();
	}

	private static char charToHex(int c)
	{
		return (char) ((c < 10) ? ('0'+c) : (('A'+c)-10));
	}

	private static boolean isCharUnsafe(char c)
	{
		if(c > 'z') // z is ascii 122
			return true;

		return " %$&+,/:;=?@<>#%".indexOf(c) >= 0;
	}


	// MISC
	@API
	public static String truncateStart(String string, int maxLength)
	{
		String toBeContinued = "...";

		if(string.length() <= maxLength)
			return string;

		int desiredBaseStringLength = maxLength-toBeContinued.length();
		return toBeContinued+string.substring(string.length()-desiredBaseStringLength);
	}

	@API
	public static String replaceLast(String string, String from, String to)
	{
		int pos = string.lastIndexOf(from);
		if(pos > -1)
			return string.substring(0, pos)+to+string.substring(pos+from.length(), string.length());

		return string;
	}

	@API
	public static List<String> split(String toSplit, String delimiter)
	{
		return new ArrayList<>(Arrays.asList(toSplit.split(delimiter)));
	}


	// COMBINATORICS
	@API
	@SafeVarargs
	public static Set<String> generateAllPermutations(String base, Collection<String>... values)
	{
		String placeholder = "{}";
		final String placeholderEscaped = escapeStringForRegex(placeholder);

		int numberOfPlaceholders = StringUtils.countMatches(base, placeholder);
		if(numberOfPlaceholders != values.length)
			throw new IllegalArgumentException("number of placeholders does not match number of value collections");

		if(values.length == 0)
			return Sets.newHashSet(base);

		Set<String> permutations = new HashSet<>();
		for(String s : values[0])
		{
			String replaced = base.replaceFirst(placeholderEscaped, s);
			Collection<String>[] remainingValues = Arrays.copyOfRange(values, 1, values.length);

			Set<String> subPermutations = generateAllPermutations(replaced, remainingValues);
			permutations.addAll(subPermutations);
		}

		return permutations;
	}

}
