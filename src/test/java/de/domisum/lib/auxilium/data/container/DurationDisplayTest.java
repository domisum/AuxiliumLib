package de.domisum.lib.auxilium.data.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class DurationDisplayTest
{

	// TEST
	@Test public void testSpecialDurations()
	{
		assertDisplays(Duration.ofMillis(0), "0ms");
		assertDisplays(Duration.ofNanos(1), "<1ms");

		assertDisplays(Duration.ofMillis(-0), "0ms");
		assertDisplays(Duration.ofNanos(-1), "-<1ms");
	}

	@Test public void testSimpleDurations()
	{
		assertDisplays(Duration.ofMillis(1), "1ms");
		assertDisplays(Duration.ofMillis(999), "999ms");
		assertDisplays(Duration.ofSeconds(1), "1s");
		assertDisplays(Duration.ofSeconds(59), "59s");
		assertDisplays(Duration.ofMinutes(1), "1m");
		assertDisplays(Duration.ofHours(1), "1h");
		assertDisplays(Duration.ofDays(1), "1d");

		assertDisplays(Duration.ofDays(7), "7d");
		assertDisplays(Duration.ofDays(3874), "3874d");
	}

	@Test public void testComposite()
	{
		assertDisplays(Duration.ofSeconds(3).plusMillis(481), "3s:481ms");
		assertDisplays(Duration.ofSeconds(2).plusMillis(7), "2s:7ms");

		assertDisplays(Duration.ofHours(7).plusMinutes(23), "7h:23m");
		assertDisplays(Duration.ofHours(1).plusMinutes(1).plusSeconds(1), "1h:1m:1s");
	}

	@Test public void testLossOfPrecision()
	{
		assertDisplays(Duration.ofDays(5).plusMinutes(1), "5d");
		assertDisplays(Duration.ofHours(9).plusMillis(838), "9h");
		assertDisplays(Duration.ofHours(12).plusMinutes(30).plusMillis(838), "12h:30m");
	}


	@Test public void testNegative()
	{
		assertDisplays(Duration.ofDays(7).negated(), "-7d");
		assertDisplays(Duration.ofDays(3874).negated(), "-3874d");

		assertDisplays(Duration.ofSeconds(3).plusMillis(481).negated(), "-3s:481ms");
		assertDisplays(Duration.ofHours(7).plusMinutes(23).negated(), "-7h:23m");

		assertDisplays(Duration.ofDays(5).plusMinutes(1).negated(), "-5d");
		assertDisplays(Duration.ofHours(9).plusMillis(838).negated(), "-9h");
		assertDisplays(Duration.ofHours(12).plusMinutes(30).plusMillis(838).negated(), "-12h:30m");
	}


	// ASSERT
	private static void assertDisplays(Duration duration, String display)
	{
		Assertions.assertEquals(display, DurationDisplay.of(duration).toString());
	}

}
