package io.domisum.lib.auxiliumlib.display;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DoubleDisplayTest
{
	
	// TEST
	@Test
	public void testSpecialValues()
	{
		assertDisplays(0d, "0.0");
		assertDisplays(null, "/");
	}
	
	@Test
	public void testBigValues()
	{
		assertDisplays(10d, "10.0");
		assertDisplays(999d, "999.0");
		assertDisplays(1000d, "1.0~K");
		assertDisplays(8.888*Math.pow(10, 15), "8.888~P");
		assertDisplays(3.8134*Math.pow(10, 24), "3.813~Y");
		
		assertDisplays(2.2*Math.pow(10, 24+4), "22000.0~Y");
	}
	
	@Test
	public void testCloseToZeroValues()
	{
		assertDisplays(0.1, "100.0~mil");
		assertDisplays(Math.pow(10, -3), "1.0~mil");
		assertDisplays(7.9416*Math.pow(10, -24), "7.942~y");
		
		assertDisplays(3.5*Math.pow(10, -24-3), "0.004~y");
		assertDisplays(3.5*Math.pow(10, -24-4), "below~0.001~y");
	}
	
	@Test
	public void testNegativeValues()
	{
		assertDisplays(-0.1, "-100.0~mil");
		assertDisplays(-Math.pow(10, -3), "-1.0~mil");
		assertDisplays(-7.9416*Math.pow(10, -24), "-7.942~y");
		assertDisplays(-3.5*Math.pow(10, -24-3), "-0.004~y");
		assertDisplays(-3.5*Math.pow(10, -24-4), "-below~0.001~y");
	}
	
	
	// ASSERT
	private static void assertDisplays(Double number, String display)
	{
		Assertions.assertEquals(display, DoubleDisplay.display(number));
	}
	
}
