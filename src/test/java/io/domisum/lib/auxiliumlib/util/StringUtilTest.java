package io.domisum.lib.auxiliumlib.util;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class StringUtilTest
{
	
	// LONGEST COMMON PREFIX
	@Test
	public void testPrefixNoCommon()
	{
		Assertions.assertEquals("", StringUtil.getLongestCommonPrefix("", ""));
		Assertions.assertEquals("", StringUtil.getLongestCommonPrefix("b", "a"));
		Assertions.assertEquals("", StringUtil.getLongestCommonPrefix("bfischerd", "afischerd"));
	}
	
	@Test
	public void testPrefixCommon()
	{
		Assertions.assertEquals("common", StringUtil.getLongestCommonPrefix("commonMeme", "commonXd"));
		
		Assertions.assertEquals("a", StringUtil.getLongestCommonPrefix("abcd", "asdf"));
		Assertions.assertEquals("a", StringUtil.getLongestCommonPrefix("a", "asdf"));
		Assertions.assertEquals("a", StringUtil.getLongestCommonPrefix("abcd", "a"));
		
		Assertions.assertEquals("+-*/", StringUtil.getLongestCommonPrefix("+-*/", "+-*/59"));
	}


    // ESCAPE
    @Test
    public void testEscapeRegexCharacters()
    {
        Assertions.assertEquals("\\.", StringUtil.escapeStringForRegex("."));
        Assertions.assertEquals("\\\\", StringUtil.escapeStringForRegex("\\"));
    }

    @Test
    public void testStripQuotes()
    {
        Assertions.assertEquals("asdf", StringUtil.stripQuotes("'asdf'"));
        Assertions.assertEquals("asdlfk;;- ", StringUtil.stripQuotes("\"asdlfk;;- \""));
        Assertions.assertEquals("", StringUtil.stripQuotes("\"\""));
        Assertions.assertEquals("", StringUtil.stripQuotes(""));

        // keep mismatched quotes
        Assertions.assertEquals("\"x'", StringUtil.stripQuotes("\"x'"));
        Assertions.assertEquals("Players'", StringUtil.stripQuotes("Players'"));
    }


	// SPLIT
	@Test
	public void testSplitLines()
	{
		Assertions.assertEquals(Arrays.asList(), StringUtil.splitLines(""));
		Assertions.assertEquals(Arrays.asList("k"), StringUtil.splitLines("k"));
		Assertions.assertEquals(Arrays.asList("key", "value"), StringUtil.splitLines("key\nvalue"));
		Assertions.assertEquals(Arrays.asList("key", "value"), StringUtil.splitLines("key\nvalue"));
		Assertions.assertEquals(Arrays.asList("key", "value"), StringUtil.splitLines("key\r\nvalue"));
	}
	
	@Test
	public void testSplitByRegex()
	{
		Assertions.assertEquals(Arrays.asList("key", "value"), StringUtil.splitByRegex("key1234value", "[0-9]+"));
	}
	
	@Test
	public void testSplitByLiteral()
	{
		Assertions.assertEquals(Arrays.asList("key", "value"), StringUtil.splitByLiteral("key=value", "="));
		Assertions.assertEquals(Arrays.asList("", ""), StringUtil.splitByLiteral("=", "="));
		Assertions.assertEquals(Arrays.asList("x", "d"), StringUtil.splitByLiteral("x.d", "."));
		Assertions.assertEquals(Arrays.asList("x", "d"), StringUtil.splitByLiteral("x/d", "/"));
		Assertions.assertEquals(Arrays.asList("x", "d"), StringUtil.splitByLiteral("x\\d", "\\"));
		Assertions.assertEquals(Arrays.asList("x", "d"), StringUtil.splitByLiteral("x+d", "+"));
		Assertions.assertEquals(Arrays.asList("https", "google.com"), StringUtil.splitByLiteral("https://google.com", "://"));
	}

	
	// COMBINATORICS
	@Test
	public void testGenerateAllPermutations()
	{
		var expectedOutput1 = Sets.newHashSet("a", "b", "c", "d");
		assertGenerateAllPermutations(expectedOutput1, "{}", Arrays.asList("a", "b", "c", "d"));
		
		var expectedOutput2 = Sets.newHashSet("aa", "ab", "bb", "ba");
		assertGenerateAllPermutations(expectedOutput2, "{}{}", Arrays.asList("a", "b"), Arrays.asList("a", "b"));
	}
	
	@Test
	public void testGenerateAllPermutationsErrorMismatchedPlaceholders()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.generateAllPermutations("{} {}", Arrays.asList("a", "b")));
		Assertions.assertThrows(IllegalArgumentException.class, () -> StringUtil.generateAllPermutations("{}", Arrays.asList("a", "b"), Arrays.asList("c", "d")));
	}
	
	@SafeVarargs
	private void assertGenerateAllPermutations(Set<String> expectedOutput, String base, Collection<String>... values)
	{
		var permutations = StringUtil.generateAllPermutations(base, values);
		Assertions.assertEquals(expectedOutput, permutations);
	}
	
}
