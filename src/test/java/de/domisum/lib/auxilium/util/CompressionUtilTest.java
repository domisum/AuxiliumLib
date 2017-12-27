package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.CompressionUtil.Speed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class CompressionUtilTest
{

	// TESTS: PROPER INPUT
	@Test public void testEmptyData()
	{
		// noinspection ZeroLengthArrayAllocation
		compressDecompressAndAssertEquals(new byte[] {});
	}

	@Test public void testUniformData()
	{
		compressDecompressAndAssertEquals(new byte[] {0});
		compressDecompressAndAssertEquals(new byte[] {-1});
		compressDecompressAndAssertEquals(new byte[] {1});
		compressDecompressAndAssertEquals(new byte[] {127});
		compressDecompressAndAssertEquals(new byte[] {-128});

		compressDecompressAndAssertEquals(getUniformData(64, 2));
		compressDecompressAndAssertEquals(getUniformData(64, 87));
		compressDecompressAndAssertEquals(getUniformData(77, -34));
		compressDecompressAndAssertEquals(getUniformData(77, 89));
		compressDecompressAndAssertEquals(getUniformData(987977, 0));
		compressDecompressAndAssertEquals(getUniformData(987977, 127));
	}

	@Test public void testBigUniformData()
	{
		compressDecompressAndAssertEquals(getUniformData(32*1024*1024, -83));
		compressDecompressAndAssertEquals(getUniformData(32*1024*1024, 31));
	}

	@Test public void testPseudorandomData()
	{
		Random random = new Random(381);

		for(int i = 0; i < 200; i++)
			compressDecompressAndAssertEquals(getPseudorandomData(random.nextInt(128*1024), random));
	}


	// TESTS: ERROR HANDLING
	@Test public void testInvalidDecompressData()
	{
		Assertions.assertThrows(RuntimeException.class, ()->CompressionUtil.decompress(getUniformData(5, 0)));
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


	// ACT + ASSERT
	private void compressDecompressAndAssertEquals(byte[] data)
	{
		for(Speed speed : Speed.values())
			compressDecompressAndAssertEquals(data, speed);
	}

	private void compressDecompressAndAssertEquals(byte[] data, Speed speed)
	{
		byte[] compressed = CompressionUtil.compress(data, speed);
		byte[] decompressed = CompressionUtil.decompress(compressed);

		Assertions.assertArrayEquals(data, decompressed);
	}

}
