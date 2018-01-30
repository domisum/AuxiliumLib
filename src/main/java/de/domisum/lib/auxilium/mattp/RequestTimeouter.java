package de.domisum.lib.auxilium.mattp;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class RequestTimeouter
{

	// CONSTANTS
	private static final Duration TIMEOUT = Duration.ofSeconds(60);

	// REFERENCES
	private final HttpUriRequest request;

	// STATUS
	private final Timer timer = new Timer(true);
	private boolean ended = false;
	private final Object lock = new Object();


	// PROCESS
	public void start()
	{
		TimerTask abortTask = new TimerTask()
		{
			@Override public void run()
			{
				synchronized(lock)
				{
					if(ended)
						return;

					ended = true;
					request.abort();
				}
			}
		};

		timer.schedule(abortTask, TIMEOUT.toMillis());
	}

	public boolean didTimeOut()
	{
		synchronized(lock)
		{
			if(ended)
				return true;

			ended = true;
			timer.cancel();

			return false;
		}
	}

}
