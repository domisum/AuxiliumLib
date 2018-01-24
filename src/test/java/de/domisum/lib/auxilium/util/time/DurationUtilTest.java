package de.domisum.lib.auxilium.util.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class DurationUtilTest
{

	// TEST formatMMSS
	@Test public void testSecondsToMinuteSecondsSimpleValues()
	{
		Assertions.assertEquals("0:00", DurationUtil.formatMMSS(Duration.ofSeconds(0)));
		Assertions.assertEquals("0:01", DurationUtil.formatMMSS(Duration.ofSeconds(1)));
		Assertions.assertEquals("0:30", DurationUtil.formatMMSS(Duration.ofSeconds(30)));
		Assertions.assertEquals("1:30", DurationUtil.formatMMSS(Duration.ofSeconds(90)));
	}

	@Test public void testSecondsToMinuteSecondsNegative()
	{
		Assertions.assertEquals("0:00", DurationUtil.formatMMSS(Duration.ofSeconds(-0)));
		Assertions.assertEquals("-0:01", DurationUtil.formatMMSS(Duration.ofSeconds(-1)));
		Assertions.assertEquals("-0:30", DurationUtil.formatMMSS(Duration.ofSeconds(-30)));
		Assertions.assertEquals("-1:00", DurationUtil.formatMMSS(Duration.ofSeconds(-60)));
	}

	@Test public void testSecondsToMInuteSecondsBigValues()
	{
		Assertions.assertEquals((87765/60)+":"+(87765%60), DurationUtil.formatMMSS(Duration.ofSeconds(87765)));
		Assertions.assertEquals("-"+(12864/60)+":"+(12864%60), DurationUtil.formatMMSS(Duration.ofSeconds(-12864)));
	}

}
