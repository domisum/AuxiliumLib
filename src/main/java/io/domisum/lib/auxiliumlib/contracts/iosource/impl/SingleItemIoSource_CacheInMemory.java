package io.domisum.lib.auxiliumlib.contracts.iosource.impl;

import io.domisum.lib.auxiliumlib.contracts.iosource.SingleItemIoSource;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
public abstract class SingleItemIoSource_CacheInMemory<T>
	implements SingleItemIoSource<T>
{
	
	// DEPENDENCIES
	private final SingleItemIoSource<T> backingSource;
	
	// STATE
	private CachedValue cachedValue;
	
	
	// CONSTANT METHODS
	@Nullable
	protected abstract Duration TTL();
	
	
	// SOURCE
	@Override
	public synchronized T get()
		throws IOException
	{
		if(cachedValue != null && cachedValue.isExpired())
			cachedValue = null;
		
		if(cachedValue == null)
		{
			var value = backingSource.get();
			cachedValue = new CachedValue(value);
		}
		
		return cachedValue.getValue();
	}
	
	
	// CACHED
	@RequiredArgsConstructor
	private class CachedValue
	{
		
		@Getter
		private final T value;
		private final Instant createdInstant = Instant.now();
		
		
		// GETTERS
		public boolean isExpired()
		{
			if(TTL() == null)
				return false;
			
			return TimeUtil.isOlderThan(createdInstant, TTL());
		}
		
	}
	
}
