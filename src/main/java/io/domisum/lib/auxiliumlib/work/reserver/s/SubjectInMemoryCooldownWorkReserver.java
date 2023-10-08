package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.util.TimeUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class SubjectInMemoryCooldownWorkReserver<T>
	extends SubjectCooldownWorkReserver<T>
{
	
	private final Map<T, Instant> onCooldownUntilMap = new HashMap<>();
	
	
	// IMPLEMENTATION
	@Override
	protected boolean isOnCooldown(T subject)
	{
		var onCooldownUntil = onCooldownUntilMap.get(subject);
		return onCooldownUntil != null && TimeUtil.isInPast(onCooldownUntil);
	}
	
	@Override
	protected void putOnCooldown(T subject, Duration cooldown)
	{
		onCooldownUntilMap.put(subject, Instant.now().plus(cooldown));
	}
	
}
