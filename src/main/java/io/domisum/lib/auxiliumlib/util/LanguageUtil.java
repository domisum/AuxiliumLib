package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LanguageUtil
{

	// SPECIAL CHARACTERS
	@API
	public static String replaceUmlauts(String input)
	{
		String withoutUmlauts = input;

		withoutUmlauts = withoutUmlauts.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
		withoutUmlauts = withoutUmlauts.replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");

		return withoutUmlauts;
	}


	// GRAMMAR
	@API
	public static String getGenitiveSingular(String of)
	{
		return of+"'s";
	}

	@API
	public static String getTupleName(int number)
	{
		switch(number)
		{
			case 1:
				return "single";
			case 2:
				return "double";
			case 3:
				return "triple";
			case 4:
				return "quadruple";
			case 5:
				return "quintuple";
		}

		throw new IllegalArgumentException("tuple name not known for number "+number);
	}

}
