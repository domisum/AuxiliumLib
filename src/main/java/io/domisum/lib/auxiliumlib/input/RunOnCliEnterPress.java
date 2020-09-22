package io.domisum.lib.auxiliumlib.input;

import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RunOnCliEnterPress
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RunOnCliEnterPress.class);
	
	
	// START
	public static synchronized void start(Runnable onPress)
	{
		ThreadUtil.createAndStartDaemonThread(()->run(onPress), "runOnCliEnterPress");
	}
	
	private static void run(Runnable onPress)
	{
		try(var scanner = new Scanner(System.in))
		{
			if(scanner.hasNextLine())
			{
				LOGGER.info("Observed CLI enter, will run given runnable");
				onPress.run();
			}
		}
	}
	
}
