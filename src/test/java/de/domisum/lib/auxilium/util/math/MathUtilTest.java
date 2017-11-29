package de.domisum.lib.auxilium.util.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathUtilTest
{

	@Test public void testRound()
	{
		Assertions.assertEquals(3.14d, MathUtil.round(Math.PI, 2));

		// test rounding up and down
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
	}

}
