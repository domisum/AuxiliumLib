package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.work.reserver.ReservedWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ForAllOnceWorkReserver<T>
	extends SubjectInMemoryCooldownWorkReserver<T>
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// STATE
	private final Set<T> completedForSubjects = new HashSet<>();
	
	
	// CONSTANT METHODS
	@Override
	protected final Duration SUCCESS_COOLDOWN()
	{
		throw new UnsupportedOperationException();
	}
	
	
	// INTERFACE
	public boolean wasSuccessfulFor(T subject)
	{
		return completedForSubjects.contains(subject);
	}
	
	
	// IMPLEMENTATION
	@Override
	protected Collection<T> getEligibleSubjects()
	{
		var subjects = new ArrayList<>(super.getEligibleSubjects());
		subjects.removeAll(completedForSubjects);
		return subjects;
	}
	
	@Override
	protected synchronized void onSuccess(ReservedWork<T> work)
	{
		completedForSubjects.add(work.getSubject());
		if(completedForSubjects.size() == super.getEligibleSubjects().size())
			logger.info("{} is completed for all subjects", getClass().getSimpleName());
	}
	
}
