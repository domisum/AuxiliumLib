package de.domisum.lib.auxilium.util.time;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationUtil
{

	// MATH
	@API
	public static Duration getDelta(Duration duration1, Duration duration2)
	{
		return duration1.minus(duration2).abs();
	}

	@API
	public static Duration min(Duration a, Duration b)
	{
		return (a.compareTo(b) < 0) ? a : b;
	}

	@API
	public static Duration max(Duration a, Duration b)
	{
		return (a.compareTo(b) > 0) ? a : b;
	}


	// FLOATING COMMA CONVERSION
	@API
	public static double getMinutesDecimal(Duration duration)
	{
		return duration.getSeconds()/(double) Duration.ofMinutes(1).getSeconds();
	}


	// DISPLAY
	@API
	public static String getMinutesSeconds(Duration duration)
	{
		long secondsMinutesCombinedSeconds = duration.getSeconds();
		long minutes = secondsMinutesCombinedSeconds/60;
		long seconds = secondsMinutesCombinedSeconds%60;

		String secondsString = seconds+"";
		if(secondsString.length() == 1)
			secondsString = "0"+secondsString;

		return minutes+":"+secondsString;
	}


	// NOW
	@API
	public static Duration toNow(Temporal from)
	{
		return Duration.between(from, Instant.now());
	}

	@API
	public static boolean isOlderThan(Instant instant, Duration duration)
	{
		Duration age = toNow(instant);
		return age.compareTo(duration) > 0;
	}

	@API
	public static boolean hasPassed(Instant instant)
	{
		return instant.isBefore(Instant.now());
	}

}
