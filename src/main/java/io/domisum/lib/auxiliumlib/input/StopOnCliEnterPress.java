package io.domisum.lib.auxiliumlib.input;

import io.domisum.lib.auxiliumlib.contracts.ApplicationStopper;
import io.domisum.lib.auxiliumlib.util.ThreadUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StopOnCliEnterPress
{
	
	public static void stopOnPress(ApplicationStopper applicationStopper)
	{
		RunOnCliEnterPress.start(()->ThreadUtil.createAndStartThread(applicationStopper::stop, "cliShutdown"));
	}
	
}
