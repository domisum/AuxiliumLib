package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.util.CompressionUtil.Speed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class CompressionUtilTest
{
	
	// TESTS: PROPER INPUT
	@Test
	public void testEmptyData()
	{
		compressDecompressAndAssertEquals(new byte[] {});
	}
	
	@Test
	public void testUniformData()
	{
		compressDecompressAndAssertEquals(new byte[] {0});
		compressDecompressAndAssertEquals(new byte[] {-1});
		compressDecompressAndAssertEquals(new byte[] {1});
		compressDecompressAndAssertEquals(new byte[] {127});
		compressDecompressAndAssertEquals(new byte[] {-128});
		
		compressDecompressAndAssertEquals(getSingleValueData(64, 2));
		compressDecompressAndAssertEquals(getSingleValueData(64, 87));
		compressDecompressAndAssertEquals(getSingleValueData(77, -34));
		compressDecompressAndAssertEquals(getSingleValueData(77, 89));
		compressDecompressAndAssertEquals(getSingleValueData(987977, 0));
		compressDecompressAndAssertEquals(getSingleValueData(987977, 127));
	}
	
	@Test
	public void testBigUniformData()
	{
		compressDecompressAndAssertEquals(getSingleValueData(2*1024*1024, -83));
		compressDecompressAndAssertEquals(getSingleValueData(2*1024*1024, 31));
	}
	
	@Test
	public void testBasicData()
	{
		compressDecompressAndAssertEquals(new byte[] {0, 10, 88, 9, -3, 8, -3, 8});
		compressDecompressAndAssertEquals(new byte[] {-1, -107, 3});
	}
	
	@Test
	public void testRandomData()
	{
		var random = new Random(381);
		for(int i = 0; i < 200; i++)
			compressDecompressAndAssertEquals(getPseudorandomData(random.nextInt(128*1024), random));
	}
	
	
	// TESTS: ERROR HANDLING
	@Test
	public void testInvalidDecompressData()
	{
		Assertions.assertThrows(RuntimeException.class, ()->CompressionUtil.decompress(getSingleValueData(5, 0)));
	}
	
	
	// ARRANGE
	private byte[] getSingleValueData(int length, int value)
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
		for(var speed : Speed.values())
			compressDecompressAndAssertEquals(data, speed);
	}
	
	private void compressDecompressAndAssertEquals(byte[] data, Speed speed)
	{
		byte[] compressed = CompressionUtil.compress(data, speed);
		byte[] decompressed = CompressionUtil.decompress(compressed);
		Assertions.assertArrayEquals(data, decompressed);
	}
	
}
