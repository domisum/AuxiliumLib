package de.domisum.lib.auxilium.util.keys;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.RandomUtil;

@API
public class Base64Key
{

	// CONSTANTS
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";


	// GENERATION
	@API
	public static String generate(int length)
	{
		StringBuilder key = new StringBuilder();
		for(int i = 0; i < length; i++)
			key.append(getRandomCharacter());

		return key.toString();
	}

	private static char getRandomCharacter()
	{
		return CHARACTERS.charAt(RandomUtil.nextInt(CHARACTERS.length()));
	}

}
