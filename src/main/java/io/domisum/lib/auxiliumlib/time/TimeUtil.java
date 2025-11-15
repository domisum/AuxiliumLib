package io.domisum.lib.auxiliumlib.time;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.Compare;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil
{
	
	// MATH
	@API
	public static Duration getDeltaAbs(Duration duration1, Duration duration2)
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
	public static double getDaysDecimal(Duration duration)
	{
		return duration.getSeconds() / (double) Duration.ofDays(1).getSeconds();
	}
	
	@API
	public static double getHoursDecimal(Duration duration)
	{
		return duration.getSeconds() / (double) Duration.ofHours(1).getSeconds();
	}
	
	@API
	public static double getMinutesDecimal(Duration duration)
	{
		return duration.getSeconds() / (double) Duration.ofMinutes(1).getSeconds();
	}
	
	@API
	public static double getSecondsDecimal(Duration duration)
	{
		return duration.toMillis() / (double) Duration.ofSeconds(1).toMillis();
	}
	
	@API
	public static double getMillisecondsDecimal(Duration duration)
	{
		return duration.getSeconds() * 1000 + (duration.toNanosPart() / (double) (1000 * 1000));
	}
	
	@API
	public static Duration fromSecondsDecimal(double seconds)
	{
		return Duration.ofMillis(Math.round(seconds * 1000));
	}
	
	@API
	public static Duration fromMinutesDecimal(double minutes)
	{
		return Duration.ofMillis(Math.round(minutes * 60 * 1000));
	}
	
	@API
	public static Duration fromHoursDecimal(double hours)
	{
		return Duration.ofSeconds(Math.round(hours * 60 * 60));
	}
	
	@API
	public static Duration fromDaysDecimal(double days)
	{
		return Duration.ofMinutes(Math.round(days * 24 * 60));
	}
	
	
	@API
	public static Duration multiply(Duration duration, double factor)
	{
		long seconds = Math.round(factor * duration.getSeconds());
		long nanos = Math.round(factor * duration.getNano());
		return Duration.ofSeconds(seconds, nanos);
	}
	
	
	// DISPLAY
	@API
	public static String displayMinutesSeconds(Duration duration)
	{
		final int secondsInMinute = 60;
		
		long totalSeconds = (int) Math.ceil(duration.toMillis() / (double) 1000);
		long displayMinutes = totalSeconds / secondsInMinute;
		long displaySeconds = totalSeconds % secondsInMinute;
		
		String secondsString = displaySeconds + "";
		if(secondsString.length() == 1)
			secondsString = "0" + secondsString;
		
		return displayMinutes + ":" + secondsString;
	}
	
	@API
	public static String displaySecAndRelative(Instant instant)
	{
		return PHR.r("{} ({})", toSecondPrecision(instant), new PrettyTime().format(instant));
	}
	
	@API
	public static Instant toSecondPrecision(Instant instant)
	{
		return instant.minusNanos(instant.getNano());
	}
	
	
	// NOW
	@API
	public static Instant ago(Duration duration)
	{
		return Instant.now().minus(duration);
	}
	
	@API
	public static Duration since(Temporal from)
	{
		return Duration.between(from, Instant.now());
	}
	
	@API
	public static Duration until(Temporal until)
	{
		return Duration.between(Instant.now(), until);
	}
	
	@API
	public static boolean isOlderThan(Instant instant, Duration duration)
	{
		var age = since(instant);
		return Compare.greaterThan(age, duration);
	}
	
	@API
	public static boolean isYoungerThan(Instant instant, Duration duration)
	{
		var age = since(instant);
		return Compare.lessThan(age, duration);
	}
	
	@API
	public static boolean isInPast(Instant instant)
	{
		return instant.isBefore(Instant.now());
	}
	
	@API
	public static boolean isInFuture(Instant instant)
	{
		return instant.isAfter(Instant.now());
	}
	
	@API
	public static boolean isMoreThanXInFuture(Instant instant, Duration minInFutureDuration)
	{
		return isInFuture(instant.minus(minInFutureDuration));
	}
	
	@API
	public static boolean isLessThanXInFuture(Instant instant, Duration maxInFutureDuration)
	{
		return isInPast(instant.minus(maxInFutureDuration));
	}
	
}
