package io.domisum.lib.auxiliumlib.work.reserver;

import io.domisum.lib.auxiliumlib.work.ReservedWork;
import io.domisum.lib.auxiliumlib.work.WorkReserver;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class SubjectCooldownWorkReserver<T>
	extends WorkReserver<T>
{
	
	// STATE
	private Set<T> allSubjects;
	
	
	// CONSTANT METHODS
	protected abstract Duration SUCCESS_COOLDOWN();
	
	protected Duration FAIL_COOLDOWN()
	{
		return Duration.ofMinutes(5);
	}
	
	
	// WORK
	@Override
	protected synchronized Optional<T> getNextSubject(Collection<T> reservedSubjects)
	{
		for(T s : getEligibleSubjects())
			if(!reservedSubjects.contains(s))
				if(!isOnCooldown(s))
					return Optional.of(s);
		
		return Optional.empty();
	}
	
	protected Set<T> getEligibleSubjects()
	{
		if(allSubjects == null)
			allSubjects = new HashSet<>(getAllSubjects());
		return allSubjects;
	}
	
	@Override
	protected synchronized void onSuccess(ReservedWork<T> work)
	{
		putOnCooldown(work.getSubject(), SUCCESS_COOLDOWN());
	}
	
	@Override
	protected synchronized void onFail(ReservedWork<T> work)
	{
		putOnCooldown(work.getSubject(), FAIL_COOLDOWN());
	}
	
	
	// IMPL
	protected abstract Collection<T> getAllSubjects();
	
	protected abstract boolean isOnCooldown(T subject);
	
	protected abstract void putOnCooldown(T subject, Duration cooldown);
	
}
