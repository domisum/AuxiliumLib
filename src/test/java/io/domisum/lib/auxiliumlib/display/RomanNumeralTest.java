package io.domisum.lib.auxiliumlib.display;

import io.domisum.lib.auxiliumlib.datacontainers.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class RomanNumeralTest
{
	
	private static final List<Pair<Integer,String>> VALID_NUMERALS = Arrays.asList(
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
		for(Pair<Integer,String> numeral : VALID_NUMERALS)
			Assertions.assertEquals(numeral.getB(), RomanNumeral.of(numeral.getA()).toString());
	}
	
	@Test
	public void testParsing()
	{
		for(Pair<Integer,String> numeral : VALID_NUMERALS)
			Assertions.assertEquals((int) numeral.getA(), RomanNumeral.parse(numeral.getB()).getNumber());
	}
	
	@Test
	public void testLowercaseParsing()
	{
		for(Pair<Integer,String> numeral : VALID_NUMERALS)
			Assertions.assertEquals((int) numeral.getA(), RomanNumeral.parse(numeral.getB().toLowerCase()).getNumber());
	}
	
	@Test
	public void testMixedCaseParsing()
	{
		Assertions.assertEquals(8, RomanNumeral.parse("ViIi").getNumber());
		Assertions.assertEquals(999, RomanNumeral.parse("cmXcIX").getNumber());
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
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("MMMM"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("MMMMI"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("MMMMXI"));
	}
	
	@Test
	public void testInvalidTokenOrderNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("IIX"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("IM"));
	}
	
	@Test
	public void testTokenRepeatedTooManyTimesNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("IIII"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("IVIV"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("VV"));
	}
	
	@Test
	public void testInvalidCharactersInNumerals()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("IXasdf"));
		Assertions.assertThrows(IllegalArgumentException.class, ()->RomanNumeral.parse("XIwow"));
	}
	
}
