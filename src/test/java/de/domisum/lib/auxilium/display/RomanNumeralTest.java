package de.domisum.lib.auxilium.display;

import de.domisum.lib.auxilium.data.container.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RomanNumeralTest
{

	private static final List<Pair<Integer, String>> NUMERALS = Arrays.asList(
			new Pair<>(1, "I"),
			new Pair<>(3, "III"),
			new Pair<>(4, "IV"),
			new Pair<>(5, "V"),
			new Pair<>(8, "VIII"),
			new Pair<>(9, "IX"),
			new Pair<>(10, "X"),

			new Pair<>(49, "XLIX"),
			new Pair<>(50, "L"),
			new Pair<>(98, "XCVIII"),
			new Pair<>(99, "XCIX"),
			new Pair<>(100, "C"),
			new Pair<>(499, "CDXCIX"),
			new Pair<>(500, "D"),
			new Pair<>(999, "CMXCIX"),
			new Pair<>(1000, "M"),
			new Pair<>(1001, "MI"),
			new Pair<>(3000, "MMM"),
			new Pair<>(3999, "MMMCMXCIX")
	);


	// TEST: VALID INPUTS
	@Test
	public void testNumbersToNumeral()
	{
		for(Pair<Integer, String> numeral : NUMERALS)
			Assertions.assertEquals(numeral.getB(), RomanNumeral.of(numeral.getA()).toString());
	}

	@Test
	public void testParsing()
	{
		for(Pair<Integer, String> numeral : NUMERALS)
			Assertions.assertEquals((int) numeral.getA(), RomanNumeral.of(numeral.getB()).getNumber());
	}

	@Test
	public void testLowercaseParsing()
	{
		for(Pair<Integer, String> numeral : NUMERALS)
			Assertions.assertEquals((int) numeral.getA(), RomanNumeral.of(numeral.getB().toLowerCase()).getNumber());
	}

	@Test
	public void testMixedCaseParsing()
	{
		Assertions.assertEquals(8, RomanNumeral.of("ViIi").getNumber());
		Assertions.assertEquals(999, RomanNumeral.of("cmXcIX").getNumber());
	}


	// TEST: ERRORS
	@Test
	public void testIllegalNumbers()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of(-4000));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of(-1));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of(0));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of(4000));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of(5009234));
	}


	@Test
	public void testTooBigNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("MMMM"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("MMMMI"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("MMMMXI"));
	}

	@Test
	public void testInvalidNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("IIX"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("IM"));
	}

	@Test
	public void testInvalidCharactersInNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("IXasdf"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.of("XIwow"));
	}

}
