package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.IncompleteCodeError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.event.Level;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggerUtil
{
	
	// LOGGING
	@API
	public static void log(Logger logger, Level level, String message, Object... objects)
	{
		if(level == Level.TRACE)
			logger.trace(message, objects);
		else if(level == Level.DEBUG)
			logger.debug(message, objects);
		else if(level == Level.INFO)
			logger.info(message, objects);
		else if(level == Level.WARN)
			logger.warn(message, objects);
		else if(level == Level.ERROR)
			logger.error(message, objects);
		else
			throw new IncompleteCodeError("Unknown log level: "+level);
	}
	
}
