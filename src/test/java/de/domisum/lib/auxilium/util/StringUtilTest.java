package de.domisum.lib.auxilium.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilTest
{

	// TEST COMMON PREFIX
	@Test public void testPrefixNoCommon()
	{
		Assertions.assertEquals("", StringUtil.getCommonPrefix("", ""));
		Assertions.assertEquals("", StringUtil.getCommonPrefix("b", "a"));
		Assertions.assertEquals("", StringUtil.getCommonPrefix("bfischerd", "afischerd"));
	}

	@Test public void testPrefixCommon()
	{
		Assertions.assertEquals("common", StringUtil.getCommonPrefix("commonMeme", "commonXd"));

		Assertions.assertEquals("a", StringUtil.getCommonPrefix("abcd", "asdf"));
		Assertions.assertEquals("a", StringUtil.getCommonPrefix("a", "asdf"));
		Assertions.assertEquals("a", StringUtil.getCommonPrefix("abcd", "a"));

		Assertions.assertEquals("+-*/", StringUtil.getCommonPrefix("+-*/", "+-*/59"));
	}


	// TEST ESCAPE REGEX CHAR
	@Test public void testEscapeRegexCharacters()
	{
		Assertions.assertEquals("\\.", StringUtil.escapeStringForRegex("."));
		Assertions.assertEquals("\\\\", StringUtil.escapeStringForRegex("\\"));
	}

}
