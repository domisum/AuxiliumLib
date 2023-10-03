package io.domisum.lib.auxiliumlib.util.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathUtilTest
{
	
	@Test
	public void testRemapLinear()
	{
		// equal start and end
		Assertions.assertThrows(IllegalArgumentException.class, ()->MathUtil.remapLinear(0, 0, 1, 2, 0));
		Assertions.assertThrows(IllegalArgumentException.class, ()->MathUtil.remapLinear(3, 3, 1, 2, 0));
		
		// ordered positive values
		Assertions.assertEquals(1.5d, MathUtil.remapLinear(10d, 20d, 0d, 3d, 15d));
		Assertions.assertEquals(5+(1/3d), MathUtil.remapLinear(10d, 40d, 5d, 6d, 20d));
		Assertions.assertEquals(7d, MathUtil.remapLinear(10d, 20d, 5d, 6d, 30d)); // out of base bounds
		
		// ordered negative
		Assertions.assertEquals(-100d-(100/3d), MathUtil.remapLinear(-5d, 10d, -200d, -100d, 5d));
		Assertions.assertEquals(-225d, MathUtil.remapLinear(-5d, 15d, -200d, -100d, -10d)); // out of base bounds
		
		// inverted order positive
		Assertions.assertEquals(75d, MathUtil.remapLinear(3d, 1d, 50d, 100d, 2d));
		Assertions.assertEquals(70d, MathUtil.remapLinear(10d, 5d, 100d, 50d, 7d));
		Assertions.assertEquals(10d, MathUtil.remapLinear(10d, 5d, 100d, 50d, 1d)); // out of base bounds
		
		// inverted order negative
		Assertions.assertEquals(7.5d, MathUtil.remapLinear(-100d, -200d, 5d, 15d, -125d));
		Assertions.assertEquals(-7.5d, MathUtil.remapLinear(-100d, -200d, -5d, -15d, -125d));
		Assertions.assertEquals(0d, MathUtil.remapLinear(-100d, -200d, -5d, -15d, -50d)); // out of base bounds
	}
	
	@Test
	public void testRound()
	{
		Assertions.assertEquals(7.14d, MathUtil.round(7.1415926, 2));
		
		// test rounding up and down of positive values
		Assertions.assertEquals(88.9d, MathUtil.round(88.94d, 1));
		Assertions.assertEquals(89.0d, MathUtil.round(88.95d, 1));
		Assertions.assertEquals(29934.987d, MathUtil.round(29934.9874d, 3));
		Assertions.assertEquals(29934.988d, MathUtil.round(29934.9875d, 3));
		
		// test rounding of 0
		Assertions.assertEquals(0d, MathUtil.round(0d, 10));
		Assertions.assertEquals(0d, MathUtil.round(0d, 0));
		
		// test rounding up and down of negative values
		Assertions.assertEquals(-7.4d, MathUtil.round(-7.44d, 1));
		Assertions.assertEquals(-7.4d, MathUtil.round(-7.45d, 1));
		Assertions.assertEquals(-7.5d, MathUtil.round(-7.46d, 1));
		
		// test negative decimal places
		Assertions.assertEquals(8000, MathUtil.round(8123, -3));
		Assertions.assertEquals(1570, MathUtil.round(1568d, -1));
	}
	
	
	@Test
	public void testClamp()
	{
		Assertions.assertEquals(0.5, MathUtil.clamp(0, 1, 0.5));
		
		// too small
		Assertions.assertEquals(0, MathUtil.clamp(0, 1, -0.5));
		Assertions.assertEquals(381.7, MathUtil.clamp(381.7, 381.88, 5));
		
		// too big
		Assertions.assertEquals(-150, MathUtil.clamp(-200, -150, 5));
		Assertions.assertEquals(1, MathUtil.clamp(0, 1, 5));
		Assertions.assertEquals(381.88, MathUtil.clamp(381.7, 381.88, 500));
	}
	
	@Test
	void testClampError()
	{
		Assertions.assertThrows(IllegalArgumentException.class, ()->MathUtil.clamp(2, 1, 0));
	}
	
}
