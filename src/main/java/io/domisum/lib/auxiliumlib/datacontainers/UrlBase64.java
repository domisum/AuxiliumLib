package io.domisum.lib.auxiliumlib.datacontainers;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.RandomUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
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
		String regularB64 = urlB64ToRegularB64(urlBase64);
		return Base64.getDecoder().decode(regularB64);
	}
	
	@API
	public static String decodeToString(String urlBase64)
	{
		byte[] decoded = decode(urlBase64);
		return new String(decoded, StandardCharsets.UTF_8);
	}
	
	@API
	public static String encode(byte[] data)
	{
		String regularBase64 = Base64.getEncoder().encodeToString(data);
		return regularB64UrlB64To(regularBase64);
	}
	
	@API
	public static String encode(String data)
	{
		byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
		return encode(dataBytes);
	}
	
	
	@API
	public static String urlB64ToRegularB64(String urlBase64)
	{
		return urlBase64.replace('-', '+').replace('_', '/');
	}
	
	@API
	public static String regularB64UrlB64To(String urlBase64)
	{
		return urlBase64.replace('+', '-').replace('/', '_');
	}
	
}
