package io.domisum.lib.auxiliumlib;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class SafeTimestamper
{
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_SSS");
	
	
	@API
	public static String stamp(ZoneId timezone, Instant instant)
	{
		return DATE_TIME_FORMATTER.withZone(timezone).format(instant);
	}
	
	@API
	public static String stampUtc(Instant instant)
	{
		return stamp(ZoneOffset.UTC, instant);
	}
	
	@API
	public static String stampSystemTimezone(Instant instant)
	{
		return stamp(ZoneId.systemDefault(), instant);
	}
	
	
	@API
	public static Instant parse(ZoneId zoneId, String str)
	{
		return DATE_TIME_FORMATTER.withZone(zoneId).parse(str, Instant::from);
	}
	
	@API
	public static Instant parseUtc(String str)
	{
		return parse(ZoneOffset.UTC, str);
	}
	
	@API
	public static Instant parseSystemTimeZone(String str)
	{
		return parse(ZoneId.systemDefault(), str);
	}
	
}
