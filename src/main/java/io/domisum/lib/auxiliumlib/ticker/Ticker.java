package io.domisum.lib.auxiliumlib.ticker;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.util.TimeUtil;
import io.domisum.lib.auxiliumlib.util.thread.ThreadUtil;
import lombok.Getter;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@API
public abstract class Ticker
{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	private static final Duration TIMEOUT_DEFAULT = null;
	
	// SETTINGS
	@Getter
	private final String name;
	private final Duration interval;
	@Nullable
	private final Duration timeout;
	
	// STATUS
	private Ticking ticking;
	
	
	// INIT HELPER
	@API
	public static Ticker create(String name, Duration interval, Tickable tickable)
	{
		return create(name, interval, TIMEOUT_DEFAULT, tickable);
	}
	
	@API
	public static Ticker create(String name, Duration interval, @Nullable Duration timeout, Tickable tickable)
	{
		return new Ticker(name, interval, timeout)
		{
			
			@Override
			protected void tick(Supplier<Boolean> shouldStop)
			{
				tickable.tick(shouldStop);
			}
			
		};
	}
	
	@API
	public static Ticker create(String name, Duration interval, Runnable tick)
	{
		return create(name, interval, TIMEOUT_DEFAULT, tick);
	}
	
	@API
	public static Ticker create(String name, Duration interval, @Nullable Duration timeout, Runnable tick)
	{
		return new Ticker(name, interval, timeout)
		{
			
			@Override
			protected void tick(Supplier<Boolean> shouldStop)
			{
				tick.run();
			}
			
		};
	}
	
	
	// INIT
	@API
	protected Ticker(String name, Duration interval, @Nullable Duration timeout)
	{
		Validate.notNull(name, "name can't be null");
		Validate.notNull(interval, "interval can't be null");
		Validate.isTrue(interval.compareTo(Duration.ZERO) > 0, "interval has to be greater than zero");
		
		this.name = name;
		this.interval = interval;
		this.timeout = timeout;
	}
	
	@API
	protected Ticker(String name, Duration interval)
	{
		this(name, interval, TIMEOUT_DEFAULT);
	}
	
	
	// CONTROL
	@API
	public synchronized void start()
	{
		var alreadyActiveTicking = getTicking();
		if(alreadyActiveTicking != null)
			throw new IllegalStateException("Can't start ticker '"+name+"' with status "+alreadyActiveTicking.status);
		
		logger.info("Starting ticker '{}'", name);
		ticking = new Ticking();
	}
	
	
	@API
	public synchronized void stopSoft()
	{
		stop(false);
	}
	
	@API
	public synchronized void stopHard()
	{
		stop(true);
	}
	
	private synchronized void stop(boolean hard)
	{
		var ticking = getTicking();
		if(ticking == null)
			return;
		
		logger.info("Stopping ticker '{}'", name);
		ticking.stop(hard);
	}
	
	
	@API
	public synchronized boolean isRunning()
	{
		return getTicking() != null;
	}
	
	
	// TICK
	protected abstract void tick(Supplier<Boolean> shouldStop);
	
	
	// TICKING
	private Ticking getTicking()
	{
		if(ticking != null && ticking.status == TickingStatus.DEAD)
			ticking = null;
		
		return ticking;
	}
	
	class Ticking
	{
		
		private final String id = UUID.randomUUID().toString();
		private final Thread tickThread;
		@Getter
		private TickingStatus status = TickingStatus.RUNNING;
		private Instant lastTickStart;
		
		
		// INIT
		public Ticking()
		{
			tickThread = ThreadUtil.createAndStartThread(this::run, name);
			if(timeout != null)
				TickerWatchdog.watch(this);
			
			logger.info("Started ticking '{}' in ticker '{}'", id, name);
		}
		
		
		// CONTROL
		private void stop(boolean hard)
		{
			if(status != TickingStatus.RUNNING)
				return;
			
			boolean self = Objects.equals(Thread.currentThread(), tickThread);
			logger.info("Stopping ticking '{}' in ticker '{}' (hard: {}, self: {})", id, name, hard, self);
			
			status = TickingStatus.STOPPING;
			if(hard)
				tickThread.interrupt();
			if(!self)
				ThreadUtil.join(tickThread);
		}
		
		
		// TICK
		private void run()
		{
			while(status == TickingStatus.RUNNING)
			{
				lastTickStart = Instant.now();
				tickCaught();
				lastTickStart = null;
				
				if(status == TickingStatus.RUNNING)
					ThreadUtil.sleep(interval);
			}
			
			status = TickingStatus.DEAD;
			logger.info("Ticking '{}' in ticker '{}' ended", id, name);
		}
		
		private void tickCaught()
		{
			try
			{
				tick(()->status != TickingStatus.RUNNING);
			}
			catch(RuntimeException e)
			{
				logger.error("Exception occured during tick", e);
			}
			catch(ThreadDeath e)
			{
				logger.info("Detected ThreadDeath in ticking '{}'", id, e);
				throw e;
			}
			catch(Throwable t)
			{
				logger.error("Uncaught exception in ticker '{}'", name, t);
				throw t;
			}
		}
		
		
		// WATCHDOG
		void watchdogTick()
		{
			// get local reference to avoid impact of changes in variable during run of method
			var lastTickStart = this.lastTickStart;
			if(lastTickStart == null)
				return;
			
			if(TimeUtil.isOlderThan(lastTickStart, timeout))
				timeout();
		}
		
		private void timeout()
		{
			logger.error("Ticking '{}' in ticker '{}' timed out (after {}). Current stacktrace:\n{}",
					id, name, DurationDisplay.of(timeout), ThreadUtil.convertThreadToString(tickThread));
			
			boolean shouldRestart = status == TickingStatus.RUNNING;
			
			tickThread.setName(tickThread.getName()+"#timedOut");
			tickThread.setDaemon(true);
			tickThread.interrupt();
			ThreadUtil.tryKill(tickThread);
			lastTickStart = null;
			status = TickingStatus.DEAD;
			
			if(shouldRestart)
				start();
		}
		
	}
	
	enum TickingStatus
	{
		
		RUNNING,
		STOPPING,
		DEAD
		
	}
	
	
	// TICKABLE
	public interface Tickable
	{
		
		void tick(Supplier<Boolean> shouldStop);
		
	}
	
}

