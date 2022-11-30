package io.domisum.lib.auxiliumlib;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExplainMode
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExplainMode.class);
	
	
	public static final String LOG_MESSAGE_PREFIX = "[EXPLAIN] ";
	private static boolean ENABLED = false;
	
	
	public static boolean IS_ENABLED()
	{
		return ENABLED;
	}
	
	
	public static void ENABLE()
	{
		if(ENABLED)
			return;
		
		LOGGER.warn("ENABLING EXPLAIN MODE");
		ENABLED = true;
	}
	
	public static void log(String message, Object... args)
	{
		if(IS_ENABLED())
			LOGGER.warn(LOG_MESSAGE_PREFIX+message, args);
	}
	
}
