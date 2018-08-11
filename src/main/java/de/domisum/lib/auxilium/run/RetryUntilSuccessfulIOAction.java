package de.domisum.lib.auxilium.run;

import de.domisum.lib.auxilium.data.container.DurationDisplay;
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


	// INPUT
	private final IOAction<O> action;
	private final String failMessage;


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


	// EXECUTE
	public O execute()
	{
		for(Duration wait = Duration.ofSeconds(1); true; wait = wait.multipliedBy(2))
			try
			{
				return action.execute();
			}
			catch(IOException e)
			{
				logger.warn(failMessage+" (will retry in " +DurationDisplay.of(wait) + ")", e);
				ThreadUtil.sleep(wait);
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
