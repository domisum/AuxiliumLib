package io.domisum.lib.auxiliumlib.work.reserver.s;

import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.util.FileUtil;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public abstract class SubjectFileLastModifiedCooldownWorkReserver<T>
	extends SubjectCooldownWorkReserver<T>
{
	
	// IMPLEMENTATION
	@Override
	protected boolean isOnCooldown(T subject)
	{
		var file = getSubjectFile(subject);
		if(!file.exists())
			return false;
		var onCooldownUntil = Instant.parse(FileUtil.readString(file));
		return TimeUtil.isInFuture(onCooldownUntil);
	}
	
	@Override
	protected void putOnCooldown(T subject, Duration cooldown)
	{
		String onCooldownUntilTimestamp = Instant.now().plus(cooldown).toString();
		FileUtil.writeString(getSubjectFile(subject), onCooldownUntilTimestamp);
	}
	
	protected File getSubjectFile(T subject)
	{
		return new File(getDirectory(), getFileName(subject));
	}
	
	
	// OVERRIDE THIS
	protected abstract File getDirectory();
	
	protected abstract String getFileName(T subject);
	
}
