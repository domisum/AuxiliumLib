package io.domisum.lib.auxiliumlib.work;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkResult
{
	
	@Getter
	private final boolean successful;
	@Getter
	private final Effort effort;
	
	
	// INIT
	public static WorkResult worked()
	{
		return new WorkResult(true, Effort.SOME);
	}
	
	public static WorkResult successfulWithoutEffort()
	{
		return new WorkResult(true, Effort.NONE);
	}
	
	public static WorkResult unableToAttempt()
	{
		return new WorkResult(false, Effort.NONE);
	}
	
	public static WorkResult workFailed()
	{
		return new WorkResult(false, Effort.SOME);
	}
	
}
