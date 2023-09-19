package io.domisum.lib.auxiliumlib.util.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringSeededRandom
{
	
	@API
	public static Random random(Object stringSeed)
	{
		long seed = numerify(stringSeed);
		return new Random(seed);
	}
	
	@API
	public static long numerify(Object string)
	{
		byte[] md5 = DigestUtils.md5(Objects.toString(string));
		return ByteBuffer.wrap(md5).getLong();
	}
	
}
