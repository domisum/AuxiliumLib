package de.domisum.lib.auxilium.util.keys;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.RandomUtil;

@APIUsage
public class Base64Key
{

	// CONSTANTS
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";


	// GENERATION
	@APIUsage public static String generate(int length)
	{
		String key = "";
		for(int i = 0; i < length; i++)
			key += getRandomCharacter();

		return key;
	}

	private static char getRandomCharacter()
	{
		return CHARACTERS.charAt(RandomUtil.nextInt(CHARACTERS.length()));
	}

}
