package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextUtil
{

	// TO STRING
	@API public static String getListAsString(List<?> list)
	{
		StringBuilder string = new StringBuilder();

		for(int i = 0; i < list.size(); i++)
		{
			Object obj = list.get(i);
			string.append(Objects.toString(obj));

			if(i < (list.size()-1))
				string.append(", ");
		}

		return string.toString();
	}


	// SPECIAL CHARACTERS
	@API public static String replaceUmlauts(String input)
	{
		String withoutUmlauts = input;

		withoutUmlauts = withoutUmlauts.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
		withoutUmlauts = withoutUmlauts.replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");

		return withoutUmlauts;
	}

	@API public static String getGenitiveSingular(String of)
	{
		return of+"'s";
	}

}
