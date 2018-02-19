package de.domisum.lib.auxilium.util.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class DurationUtilTest
{

	// TEST format
	@Test public void testFormatSeconds()
	{
		Assertions.assertEquals("0s", DurationUtil.format(Duration.ofSeconds(0)));
		Assertions.assertEquals("1s", DurationUtil.format(Duration.ofSeconds(1)));
		Assertions.assertEquals("10s", DurationUtil.format(Duration.ofSeconds(10)));
		Assertions.assertEquals("59s", DurationUtil.format(Duration.ofSeconds(59)));
	}

	@Test public void testFormatMinutesAndSeconds()
	{
		Assertions.assertEquals("1m:0s", DurationUtil.format(Duration.ofSeconds(60)));
		Assertions.assertEquals("7m:37s", DurationUtil.format(Duration.ofMinutes(7).plusSeconds(37)));
		Assertions.assertEquals("59m:59s", DurationUtil.format(Duration.ofMinutes(59).plusSeconds(59)));
	}

	@Test public void testFormatHoursMinutesAndSeconds()
	{
		Assertions.assertEquals("1h:0m:0s", DurationUtil.format(Duration.ofHours(1)));
		Assertions.assertEquals("8h:20m:3s", DurationUtil.format(Duration.ofHours(8).plusMinutes(20).plusSeconds(3)));
	}


	@Test public void testMilliseconds()
	{
		Assertions.assertEquals("0s:127ms", DurationUtil.format(Duration.ofMillis(127)));
		Assertions.assertEquals("0s:1ms", DurationUtil.format(Duration.ofMillis(1)));
	}

	@Test public void testFormatNegative()
	{
		Assertions.assertEquals("-1s", DurationUtil.format(Duration.ofSeconds(1).negated()));
		Assertions.assertEquals("-59m:59s", DurationUtil.format(Duration.ofMinutes(59).plusSeconds(59).negated()));
		Assertions.assertEquals("-1h:0m:0s", DurationUtil.format(Duration.ofHours(1).negated()));
		Assertions.assertEquals("-8h:20m:3s", DurationUtil.format(Duration.ofHours(8).plusMinutes(20).plusSeconds(3).negated()));
	}

}
