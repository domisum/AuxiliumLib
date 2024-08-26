package io.domisum.lib.auxiliumlib.work;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class WorkResult
{
	
	private final boolean successful;
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
	
	
	public static WorkResult merge(WorkResult a, WorkResult b)
	{
		return new WorkResult(
			a.successful && b.successful,
			a.effort == Effort.SOME || b.effort == Effort.SOME ? Effort.SOME : Effort.NONE
		);
	}
	
}
