package io.domisum.lib.auxiliumlib.work;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkResult
{
	
	private final boolean successful;
	@Getter private final Effort effort;
	
	
	// INIT
	public static WorkResult didSomeWork() {return new WorkResult(true, Effort.SOME);}
	
	public static WorkResult successfulWithoutEffort() {return new WorkResult(true, Effort.NONE);}
	
	public static WorkResult unableToAttempt() {return new WorkResult(false, Effort.NONE);}
	
	public static WorkResult workFailed() {return new WorkResult(false, Effort.SOME);}
	
	
	public static WorkResult merge(WorkResult a, WorkResult b)
	{
		return new WorkResult(
			a.isSuccess() && b.isSuccess(),
			Effort.merge(a.getEffort(), b.getEffort()));
	}
	
	
	// GETTERS
	public boolean isSuccess() {return successful;}
	
	public boolean isFail() {return !successful;}
	
	public boolean isSuccessfulWithoutEffort() {return isSuccess() && effort == Effort.NONE;}
	
	
	// DERIVE
	public WorkResult deriveMerged(WorkResult other) {return merge(this, other);}
	
}
