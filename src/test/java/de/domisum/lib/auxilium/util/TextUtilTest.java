package de.domisum.lib.auxilium.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TextUtilTest
{

	// TEST secondsToMinuteSecond
	@Test public void testSecondsToMinuteSecondsSimpleValues()
	{
		Assertions.assertEquals("0:00", TextUtil.secondsToMinuteSeconds(0));
		Assertions.assertEquals("0:01", TextUtil.secondsToMinuteSeconds(1));
		Assertions.assertEquals("0:30", TextUtil.secondsToMinuteSeconds(30));
		Assertions.assertEquals("1:30", TextUtil.secondsToMinuteSeconds(90));
	}

	@Test public void testSecondsToMinuteSecondsNegative()
	{
		Assertions.assertEquals("0:00", TextUtil.secondsToMinuteSeconds(-0));
		Assertions.assertEquals("-0:01", TextUtil.secondsToMinuteSeconds(-1));
		Assertions.assertEquals("-0:30", TextUtil.secondsToMinuteSeconds(-30));
		Assertions.assertEquals("-1:00", TextUtil.secondsToMinuteSeconds(-60));
	}

	@Test public void testSecondsToMInuteSecondsBigValues()
	{
		Assertions.assertEquals((87765/60)+":"+(87765%60), TextUtil.secondsToMinuteSeconds(87765));
		Assertions.assertEquals("-"+(12864/60)+":"+(12864%60), TextUtil.secondsToMinuteSeconds(-12864));
	}

}
