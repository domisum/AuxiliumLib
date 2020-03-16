package io.domisum.lib.auxiliumlib.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PHRTest
{

	// TEST: ERRORS
	@Test
	public void testErrorInvalidNumberOfArgs()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->PHR.r("blah {} blah {} xd"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->PHR.r("blah {} blah {} xd", "meme"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->PHR.r("blah {} blah {} xd", 8, 8, 100));
	}


	// TEST: PROPER VALUES
	@Test
	public void testNoPlaceholders()
	{
		assertReplaceEquals("here I go", "here I go");
		assertReplaceEquals("", "");
	}

	@Test
	public void testSingleReplacement()
	{
		assertReplaceEquals("here I go", "here {} go", "I");
		assertReplaceEquals("aha#asdf", "aha#{}df", "as");
		assertReplaceEquals("topKek4", "topKek{}", 4);

		assertReplaceEquals("something", "{}thing", "some");
		assertReplaceEquals("3.141", "3.{}", "141");
	}

	@Test
	public void testMultiReplace()
	{
		assertReplaceEquals("some text goes here", "some {} goes {}", "text", "here");
		assertReplaceEquals(
				"multi replacements are very fun indeed, I'll have to admit",
				"multi replacements {} {} fun {}, {} have to admit",
				"are",
				"very",
				"indeed",
				"I'll"
		);
	}

	@Test
	public void testReplaceWithPlaceholder()
	{
		assertReplaceEquals("here {} go", "here {} go", "{}");
		assertReplaceEquals("some silly face: :-{} xddd", "some silly face: {} xddd", ":-{}");

		assertReplaceEquals("{}{}", "{}{}", "{}", "{}");
		assertReplaceEquals("as{}", "{}{}", "as", "{}");
		assertReplaceEquals("wow {} between", "{} {} {}", "wow", "{}", "between");
	}


	private static void assertReplaceEquals(String expected, String withPlaceholders, Object... values)
	{
		String replaced = PHR.r(withPlaceholders, values);
		Assertions.assertEquals(expected, replaced);
	}

}
