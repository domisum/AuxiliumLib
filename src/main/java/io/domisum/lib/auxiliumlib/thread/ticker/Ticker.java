package io.domisum.lib.auxiliumlib.thread.ticker;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.display.DurationDisplay;
import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.util.Compare;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import io.domisum.lib.auxiliumlib.util.ValidationUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@API
public abstract class Ticker
{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// SHOULD STOP
	private static final Map<Thread, Ticking> tickingsByThread = new ConcurrentHashMap<>();
	
	@API
	public static boolean shouldStop()
	{
		var thread = Thread.currentThread();
		var ticking = tickingsByThread.get(thread);
		if(ticking == null)
			return false;
		
		return ticking.getStatus() == TickingStatus.STOPPING;
	}
	
	@API
	public static void sleepButReactToShouldStop(Duration targetDuration)
	{
		var start = Instant.now();
		
		while(!shouldStop())
		{
			var elapsedDuration = TimeUtil.since(start);
			if(Compare.greaterThan(elapsedDuration, targetDuration))
				break;
			
			var remaining = targetDuration.minus(elapsedDuration);
			var sleep = ObjectUtils.min(remaining, Duration.ofMillis(100));
			ThreadUtil.sleep(sleep);
		}
	}
	
	
	// CONSTANTS
	private static final Duration TIMEOUT_DEFAULT = null;
	
	// SETTINGS
	@Getter private final String name;
	private final Duration interval;
	@Setter @Nullable private Duration timeout;
	private final boolean isDaemon;
	@Setter private boolean verbose = true;
	
	
	// STATUS
	private final AtomicInteger tickingNumber = new AtomicInteger(0);
	private volatile Ticking ticking;
	
	
	// INIT
	@API
	public static Ticker create(String name, Duration interval, Runnable tick)
	{
		return create(name, interval, TIMEOUT_DEFAULT, tick);
	}
	
	@API
	public static Ticker create(String name, Duration interval, @Nullable Duration timeout, Runnable tick)
	{
		return create(name, interval, timeout, false, tick);
	}
	
	@API
	public static Ticker createDaemon(String name, Duration interval, @Nullable Duration timeout, Runnable tick)
	{
		return create(name, interval, timeout, true, tick);
	}
	
	@API
	public static Ticker createDaemon(String name, Duration interval, Runnable tick)
	{
		return create(name, interval, TIMEOUT_DEFAULT, true, tick);
	}
	
	private static Ticker create(String name, Duration interval, @Nullable Duration timeout, boolean isDaemon, Runnable tick)
	{
		return new Ticker(name, interval, timeout, isDaemon)
		{
			
			@Override
			protected void tick()
			{
				tick.run();
			}
		};
	}
	
	
	@API
	protected Ticker(String name, Duration interval,
					 @Nullable Duration timeout, boolean isDaemon)
	{
		ValidationUtil.notNull(name, "name");
		ValidationUtil.greaterZero(interval, "interval");
		this.name = name;
		this.interval = interval;
		
		if(timeout != null)
			ValidationUtil.greaterZero(timeout, "timeout");
		this.timeout = timeout;
		this.isDaemon = isDaemon;
	}
	
	
	// CONTROL
	@API
	public synchronized void start()
	{
		var ticking = getTicking();
		if(ticking != null)
			throw new IllegalStateException("Can't start ticker '" + name + "' with status " + ticking.status);
		
		startWithoutChecks();
	}
	
	@API
	public synchronized void startIfNotAlreadyRunning()
	{
		var ticking = getTicking();
		if(ticking != null)
			return;
		
		startWithoutChecks();
	}
	
	private void startWithoutChecks()
	{
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
		
		if(verbose)
			logger.info("Stopping ticker '{}'", name);
		ticking.stop(hard);
	}
	
	
	@API
	public void dumpStackTrace(String loggerMessage)
	{
		var ticking = this.ticking;
		if(ticking == null)
			return;
		
		logger.warn(loggerMessage, ThreadUtil.displayThread(ticking.tickThread));
	}
	
	
	// GETTERS
	@API
	public Optional<Duration> getTimeout()
	{
		return Optional.ofNullable(timeout);
	}
	
	@API
	public synchronized boolean isRunning()
	{
		return getTicking() != null;
	}
	
	
	// TICK
	protected abstract void tick();
	
	@API
	protected void watchdogTick(Consumer<String> timeoutWithReason) {}
	
	
	// TICKING
	private Ticking getTicking()
	{
		if(ticking != null && ticking.status == TickingStatus.DEAD)
			ticking = null;
		
		return ticking;
	}
	
	// package visible for TickerWatchdog
	class Ticking
	{
		
		private final String id = name + "_" + tickingNumber.getAndIncrement();
		private final Thread tickThread;
		@Getter
		private volatile TickingStatus status = TickingStatus.RUNNING;
		private volatile Instant lastTickStart;
		
		
		// INIT
		public Ticking()
		{
			if(isDaemon)
				tickThread = ThreadUtil.createDaemonThread(this::run, name);
			else
				tickThread = ThreadUtil.createThread(this::run, name);
			
			// watch regardless of whether timeout is null so watchdogTick method is run
			TickerWatchdog.watch(this);
			tickingsByThread.put(tickThread, this);
			
			tickThread.start();
			if(verbose)
				logger.info("Started ticking '{}'", id);
		}
		
		
		// CONTROL
		private void stop(boolean hard)
		{
			if(status != TickingStatus.RUNNING)
				return;
			
			boolean self = Objects.equals(Thread.currentThread(), tickThread);
			if(verbose)
				logger.info("Stopping ticking '{}' in ticker (hard: {}, self: {})", id, hard, self);
			
			status = TickingStatus.STOPPING;
			if(hard)
				tickThread.interrupt();
			if(!self)
				ThreadUtil.join(tickThread);
		}
		
		
		// TICK
		private void run()
		{
			runMainLoop();
			end();
		}
		
		private void runMainLoop()
		{
			while(status == TickingStatus.RUNNING)
			{
				lastTickStart = Instant.now();
				tickCaught();
				lastTickStart = null;
				
				if(status == TickingStatus.RUNNING)
					ThreadUtil.sleep(interval);
			}
		}
		
		private void tickCaught()
		{
			try
			{
				tick();
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
		
		private void end()
		{
			status = TickingStatus.DEAD;
			tickingsByThread.remove(tickThread);
			if(verbose)
				logger.info("Ticking '{}' ended", id);
		}
		
		
		// WATCHDOG
		public void watchdogTick()
		{
			// get local reference to avoid impact of changes in variable during run of method
			var lastTickStart = this.lastTickStart;
			if(lastTickStart == null)
				return;
			
			if(timeout != null && TimeUtil.isOlderThan(lastTickStart, timeout))
			{
				tickTimeout();
				return;
			}
			
			Ticker.this.watchdogTick(this::timeout);
		}
		
		private void tickTimeout()
		{
			timeout("tick timeout: " + DurationDisplay.of(timeout));
		}
		
		private void timeout(String reason)
		{
			logger.error("Ticking '{}' in ticker '{}' timed out ({}). Current stacktrace:\n{}",
				id, name, reason, ThreadUtil.displayThread(tickThread));
			
			boolean shouldRestart = status == TickingStatus.RUNNING;
			
			tickThread.setName(tickThread.getName() + "#timedOut");
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
	
}

