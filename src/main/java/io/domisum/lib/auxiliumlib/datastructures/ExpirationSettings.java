package io.domisum.lib.auxiliumlib.datastructures;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@API
@RequiredArgsConstructor
public class ExpirationSettings
{
	
	@Getter
	private final Duration expirationDuration;
	private final boolean randomizeExpirationDuration;
	private final boolean onlyExpireUnused;
	
	
	// INIT
	@API
	public static ExpirationSettings after(Duration expirationDuration)
	{
		return new ExpirationSettings(expirationDuration, false, false);
	}
	
	@API
	public static ExpirationSettings afterRandomized(Duration expirationDuration)
	{
		return new ExpirationSettings(expirationDuration, true, false);
	}
	
	@API
	public static ExpirationSettings unusedAfter(Duration expirationDuration)
	{
		return new ExpirationSettings(expirationDuration, false, true);
	}
	
	@API
	public static ExpirationSettings unusedAfterRandomized(Duration expirationDuration)
	{
		return new ExpirationSettings(expirationDuration, true, true);
	}
	
	
	// GETTERS
	public boolean shouldRandomizeExpirationDuration()
	{
		return randomizeExpirationDuration;
	}
	
	public boolean shouldOnlyExpireUnused()
	{
		return onlyExpireUnused;
	}
	
}
