package de.domisum.lib.auxilium.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.zip.DataFormatException;

public class CompressionUtilTest
{

	// TESTS: PROPER INPUT
	@Test public void testEmptyData()
	{
		assertCompressDecompressEquals(new byte[] {});
	}

	@Test public void testUniformData()
	{
		assertCompressDecompressEquals(new byte[] {0});
		assertCompressDecompressEquals(new byte[] {-1});
		assertCompressDecompressEquals(new byte[] {1});
		assertCompressDecompressEquals(new byte[] {127});
		assertCompressDecompressEquals(new byte[] {-128});

		assertCompressDecompressEquals(getUniformData(64, 2));
		assertCompressDecompressEquals(getUniformData(64, 87));
		assertCompressDecompressEquals(getUniformData(77, -34));
		assertCompressDecompressEquals(getUniformData(77, 89));
		assertCompressDecompressEquals(getUniformData(987977, 0));
		assertCompressDecompressEquals(getUniformData(987977, 127));
	}

	@Test public void testBigUniformData()
	{
		assertCompressDecompressEquals(getUniformData(32*1024*1024, -83));
		assertCompressDecompressEquals(getUniformData(32*1024*1024, 31));
	}

	@Test public void testPseudorandomData()
	{
		Random random = new Random(381);

		for(int i = 0; i < 200; i++)
			assertCompressDecompressEquals(getPseudorandomData(random.nextInt(128*1024), random));
	}


	// TESTS: ERROR HANDLING
	@Test public void testInvalidDecompressData()
	{
		Assertions.assertThrows(DataFormatException.class, ()->CompressionUtil.decompress(getUniformData(5, 0)));
	}


	// ARRANGE
	private byte[] getUniformData(int length, int value)
	{
		byte[] data = new byte[length];
		Arrays.fill(data, (byte) value);

		return data;
	}

	private byte[] getPseudorandomData(int length, Random random)
	{
		byte[] data = new byte[length];
		random.nextBytes(data);

		return data;
	}


	// ASSERT
	private void assertCompressDecompressEquals(byte[] data)
	{
		for(CompressionUtil.Speed speed : CompressionUtil.Speed.values())
			assertCompressDecompressEquals(data, speed);
	}

	private void assertCompressDecompressEquals(byte[] data, CompressionUtil.Speed speed)
	{
		byte[] compressed = CompressionUtil.compress(data, speed);
		byte[] decompressed = CompressionUtil.decompress(compressed);

		Assertions.assertArrayEquals(data, decompressed);
	}

}
