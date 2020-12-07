package io.domisum.lib.auxiliumlib.datacontainers;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlBase64
{
	
	// CONSTANTS
	@API
	public static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
	
	
	// GENERATION
	@API
	public static String generate(int length)
	{
		var key = new StringBuilder();
		for(int i = 0; i < length; i++)
			key.append(getRandomCharacter());
		
		return key.toString();
	}
	
	private static char getRandomCharacter()
	{
		return CHARACTERS.charAt(RandomUtil.nextInt(CHARACTERS.length()));
	}
	
	
	// CODING
	@API
	public static byte[] decode(String urlBase64)
	{
		String base64 = urlBase64.replace('-', '+').replace('_', '/');
		return Base64.getDecoder().decode(base64);
	}
	
}
