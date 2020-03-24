package io.domisum.lib.auxiliumlib.datacontainers;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Base64Key
{

	// CONSTANTS
	@API
	public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";


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
