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

	private static final int SECONDS_IN_MINUTE = 60;


	// NUMBERS
	@API public static String secondsToMinuteSeconds(int seconds)
	{
		boolean negative = seconds < 0;
		int unsignedSeconds = negative ? -seconds : seconds;
		int leftoverSeconds = unsignedSeconds%SECONDS_IN_MINUTE;

		String sign = negative ? "-" : "";
		int minutes = unsignedSeconds/SECONDS_IN_MINUTE;
		String secondsWithoutMinutes = ((leftoverSeconds < 10) ? "0" : "")+leftoverSeconds;
		return sign+minutes+":"+secondsWithoutMinutes;
	}


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

	@API public static String getGenitive(String of)
	{
		if(of.endsWith("s") || of.endsWith("x"))
			return of+"'";

		return of+"'s";
	}

}
