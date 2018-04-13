package de.domisum.lib.auxilium.run;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class RetryUntilSuccessfulIOAction<O>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final Duration DEFAULT_RETRY_INTERVAL = Duration.ofSeconds(5);

	// INPUT
	private final IOAction<O> action;
	private final String failMessage;

	// SETTINGS
	private Duration retryInterval = DEFAULT_RETRY_INTERVAL;


	// INIT
	public RetryUntilSuccessfulIOAction(VoidIOAction action, String failMessage)
	{
		this.action = ()->
		{
			action.execute();
			return null;
		};
		this.failMessage = failMessage;
	}


	// SETTERS
	public RetryUntilSuccessfulIOAction<O> setRetryInterval(Duration retryInterval)
	{
		this.retryInterval = retryInterval;
		return this;
	}


	// EXECUTE
	public O execute()
	{
		while(true)
			try
			{
				return action.execute();
			}
			catch(IOException e)
			{
				logger.warn(failMessage+" (will retry)", e);
				ThreadUtil.sleep(retryInterval);
			}
	}


	// INTERFACE
	public interface IOAction<O>
	{

		O execute() throws IOException;

	}

	public interface VoidIOAction
	{

		void execute() throws IOException;

	}

}
