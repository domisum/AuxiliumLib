package io.domisum.lib.auxiliumlib.util;

import com.google.common.collect.Sets;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil
{
	
	// CONCAT
	@API
	public static String repeat(String toRepeat, int count, String delimiter)
	{
		ValidationUtil.notNull(toRepeat, "toRepeat");
		ValidationUtil.notNegative(count, "count");
		ValidationUtil.notNull(delimiter, "delimiter");
		
		var builder = new StringBuilder();
		for(int i = 0; i < count; i++)
		{
			builder.append(toRepeat);
			if(i < count-1)
				builder.append(delimiter);
		}
		
		return builder.toString();
	}
	
	@API
	public static String indentWithSpaces(Object toIndent, int spaceCount)
	{
		return indent(toIndent, " ".repeat(spaceCount));
	}
	
	@API
	public static String indent(Object toIndent, String indentationString)
	{
		var lines = splitLines(Objects.toString(toIndent));
		
		var newLines = new ArrayList<String>();
		for(String line : lines)
			newLines.add(indentationString+line);
		
		return StringListUtil.list(newLines, "\n");
	}
	
	
	// ANALYSIS
	@API
	public static String getLongestCommonPrefix(String s1, String s2)
	{
		var commonPrefix = new StringBuilder();
		for(int ci = 0; ci < Math.min(s1.length(), s2.length()); ci++)
		{
			char c1 = s1.charAt(ci);
			char c2 = s2.charAt(ci);
			
			if(c1 != c2)
				break;
			
			commonPrefix.append(c1);
		}
		
		return commonPrefix.toString();
	}
	
	@API
	public static boolean containsAny(String containerString, Iterable<? extends CharSequence> snippets)
	{
		for(CharSequence s : snippets)
			if(containerString.contains(s))
				return true;
		
		return false;
	}
	
	
	// ESCAPING
	@API
	public static String escapeStringForRegex(String input)
	{
		final var charactersToEscape = Arrays.asList(ArrayUtils.toObject("<([{\\^-=$!|]})?*+.>".toCharArray()));
		
		String escaped = input;
		for(int i = 0; i < escaped.length(); i++)
		{
			char charAt = escaped.charAt(i);
			if(charactersToEscape.contains(charAt))
			{
				escaped = escaped.substring(0, i)+"\\"+charAt+escaped.substring(i+1);
				// noinspection AssignmentToForLoopParameter
				i++;
			}
		}
		
		return escaped;
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
			return string.substring(0, pos)+to+string.substring(pos+from.length());
		
		return string;
	}
	
	@API
	public static List<String> splitLines(String toSplit)
	{
		return split(toSplit, "\\R");
	}
	
	@API
	public static List<String> split(String toSplit, String delimiterRegex)
	{
		if(toSplit.isEmpty())
			return Collections.emptyList();
		
		String[] split = toSplit.split(delimiterRegex, -1); // -1 to include trailing empty strings
		return new ArrayList<>(Arrays.asList(split));
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
			throw new IllegalArgumentException("number of placeholders does not match number of value collection");
		
		if(values.length == 0)
			return Sets.newHashSet(base);
		
		var permutations = new HashSet<String>();
		for(String s : values[0])
		{
			String replaced = base.replaceFirst(placeholderEscaped, s);
			Collection<String>[] remainingValues = Arrays.copyOfRange(values, 1, values.length);
			
			var subPermutations = generateAllPermutations(replaced, remainingValues);
			permutations.addAll(subPermutations);
		}
		
		return permutations;
	}
	
}
