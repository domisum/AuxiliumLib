package de.domisum.lib.auxilium.util;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class StringUtilTest
{

	// TEST COMMON PREFIX
	@Test
	public void testPrefixNoCommon()
	{
		Assertions.assertEquals("", StringUtil.getCommonPrefix("", ""));
		Assertions.assertEquals("", StringUtil.getCommonPrefix("b", "a"));
		Assertions.assertEquals("", StringUtil.getCommonPrefix("bfischerd", "afischerd"));
	}

	@Test
	public void testPrefixCommon()
	{
		Assertions.assertEquals("common", StringUtil.getCommonPrefix("commonMeme", "commonXd"));

		Assertions.assertEquals("a", StringUtil.getCommonPrefix("abcd", "asdf"));
		Assertions.assertEquals("a", StringUtil.getCommonPrefix("a", "asdf"));
		Assertions.assertEquals("a", StringUtil.getCommonPrefix("abcd", "a"));

		Assertions.assertEquals("+-*/", StringUtil.getCommonPrefix("+-*/", "+-*/59"));
	}


	// TEST ESCAPE REGEX CHAR
	@Test
	public void testEscapeRegexCharacters()
	{
		Assertions.assertEquals("\\.", StringUtil.escapeStringForRegex("."));
		Assertions.assertEquals("\\\\", StringUtil.escapeStringForRegex("\\"));
	}


	// TEST TO STRING
	@Test
	public void testListToString()
	{
		Assertions.assertEquals("meme, asdf", StringUtil.listToString(Arrays.asList("meme", "asdf"), ", "));
		Assertions.assertEquals("wow\nnice\nmeme\nxd", StringUtil.listToString(Arrays.asList("wow", "nice", "meme", "xd"), "\n"));
	}


	// COMBINATORICS
	@Test
	public void testGenerateAllPermutations()
	{
		Set<String> expectedOutput1 = Sets.newHashSet("a", "b", "c", "d");
		assertGenerateAllPermutations(expectedOutput1, "{}", Arrays.asList("a", "b", "c", "d"));

		Set<String> expectedOutput2 = Sets.newHashSet("aa", "ab", "bb", "ba");
		assertGenerateAllPermutations(expectedOutput2, "{}{}", Arrays.asList("a", "b"), Arrays.asList("a", "b"));
	}

	@Test
	public void testGenerateAllPermutationsErrorMismatchedPlaceholders()
	{
		Assertions.assertThrows(IllegalArgumentException.class,
				()->StringUtil.generateAllPermutations("{} {}", Arrays.asList("a", "b"))
		);

		Assertions.assertThrows(IllegalArgumentException.class,
				()->StringUtil.generateAllPermutations("{}", Arrays.asList("a", "b"), Arrays.asList("c", "d"))
		);
	}

	@SafeVarargs
	private final void assertGenerateAllPermutations(Set<String> expectedOutput, String base, Collection<String>... values)
	{
		Set<String> permutations = StringUtil.generateAllPermutations(base, values);
		Assertions.assertEquals(expectedOutput, permutations);
	}

}
