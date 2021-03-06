package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PlaceHolderReplacer.
 * <p>
 * Replaces placeholders in strings with supplied values.
 * <p>
 * Throws an exception if the number of placeholders in the String does not match the supplied number of objects.
 * <p>
 * The names of the class and the static methods are chosen as an acronym
 * to keep static method calls as short as possible.
 */
@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PHR
	implements CharSequence
{
	
	// CONSTANTS
	@API
	public static final String PLACEHOLDER = "{}";
	
	// INPUT
	private final String text;
	private final Object[] values;
	
	// CACHED
	private String replaced;
	
	
	// INIT
	@API
	public static String r(String text, Object... values)
	{
		return rcs(text, values).toString();
	}
	
	/**
	 * Creates a lazy CharSequence object which will only do the replacing if any of the CharSequence methods or #toString() is
	 * called. This can improve performance when this method is called extremely often in performance critical parts of the code.
	 *
	 * @param text   the text with placeholders
	 * @param values the values with which the placeholders shall be replaced
	 * @return a CharSequence which represents the string with placeholders replaced by the supplied values
	 */
	@API
	public static CharSequence rcs(String text, Object... values)
	{
		return new PHR(text, values);
	}
	
	
	// OBJECT
	@Override
	public synchronized String toString()
	{
		ensureReplacedSet();
		return replaced;
	}
	
	
	// REPLACE
	private void ensureReplacedSet()
	{
		if(replaced == null)
			replaced = replacePlaceholders();
	}
	
	private String replacePlaceholders()
	{
		var placeholderIndices = determinePlaceholderIndices(text);
		if(placeholderIndices.size() != values.length)
			throw new IllegalArgumentException("given values: "+values.length+", found placeholders: "+placeholderIndices.size());
		return replacePlaceholdersWithValues(text, placeholderIndices, values);
	}
	
	private static List<Integer> determinePlaceholderIndices(String text)
	{
		var placeholderIndices = new ArrayList<Integer>();
		int searchFrom = 0;
		while(true)
		{
			int index = text.indexOf(PLACEHOLDER, searchFrom);
			if(index == -1)
				break;
			
			placeholderIndices.add(index);
			searchFrom = index+1;
		}
		
		return placeholderIndices;
	}
	
	private static String replacePlaceholdersWithValues(String text, List<Integer> placeholderIndices, Object[] values)
	{
		String filledInString = text;
		for(int i = placeholderIndices.size()-1; i >= 0; i--) // iterate from back so inserting string doesn't change the indices of the other placeholders
		{
			int placeholderStartIndex = placeholderIndices.get(i);
			
			String beforePlaceholder = filledInString.substring(0, placeholderStartIndex);
			String valueForPlaceholder = Objects.toString(values[i]);
			String afterPlaceholder = filledInString.substring(placeholderStartIndex+PLACEHOLDER.length());
			
			filledInString = beforePlaceholder+valueForPlaceholder+afterPlaceholder;
		}
		
		return filledInString;
	}
	
	
	// CHARSEQUENCE
	@Override
	public synchronized int length()
	{
		ensureReplacedSet();
		return replaced.length();
	}
	
	@Override
	public synchronized char charAt(int index)
	{
		ensureReplacedSet();
		return replaced.charAt(index);
	}
	
	@Override
	public synchronized CharSequence subSequence(int start, int end)
	{
		ensureReplacedSet();
		return replaced.subSequence(start, end);
	}
	
}
