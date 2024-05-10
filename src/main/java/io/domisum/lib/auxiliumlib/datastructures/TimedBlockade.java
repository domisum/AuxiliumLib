package io.domisum.lib.auxiliumlib.datastructures;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TimedBlockade
{
	
	private final TimedMultiBlockade<Void> multiBlockade;
	
	
	// INIT
	@API
	public TimedBlockade(Duration defaultDuration)
	{
		this(new TimedMultiBlockade<>(defaultDuration));
	}
	
	@API
	public TimedBlockade()
	{
		this(new TimedMultiBlockade<>());
	}
	
	
	// LOCK
	@API
	public boolean tryBlock(Duration duration)
	{
		return multiBlockade.tryBlock(null, duration);
	}
	
	@API
	public boolean tryBlock()
	{
		return multiBlockade.tryBlock(null);
	}
	
	@API
	public void block(Duration duration)
	{
		multiBlockade.block(null, duration);
	}
	
	@API
	public void block()
	{
		multiBlockade.block(null);
	}
	
	@API
	public void unblock()
	{
		multiBlockade.unblock(null);
	}
	
	
	// STATUS
	@API
	public boolean isBlocked()
	{
		return multiBlockade.isBlocked(null);
	}
	
	@API
	public Optional<Duration> getRemainingBlockDuration()
	{
		return multiBlockade.getRemainingBlockDuration(null);
	}
	
	@API
	public Optional<Instant> getBlockReleaseInstant()
	{
		return multiBlockade.getNextBlockReleaseInstant();
	}
	
}
