package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@APIUsage
public class Logger
{

	// CONSTANTS
	@APIUsage public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("z yyyy/MM/dd HH:mm:ss.SSS",
			Locale.ENGLISH);


	// LOGGING
	@APIUsage public static void info(String message)
	{
		String formattedMessage = getTimePrefix()+" [INFO] "+message;

		System.out.println(formattedMessage);
	}

	@APIUsage public static void warn(String message)
	{
		String formattedMessage = getTimePrefix()+" [WARN] "+message;

		System.out.println(formattedMessage);
	}

	@APIUsage public static void err(String message)
	{
		String formattedMessage = getTimePrefix()+" [ERR] "+message;

		System.err.println(formattedMessage);
	}

	@APIUsage public static void err(String message, Exception e)
	{
		err(message);

		if(e != null)
			e.printStackTrace();
	}

	@APIUsage private static String getTimePrefix()
	{
		Date currentTime = new Date(System.currentTimeMillis());

		return "["+TIME_FORMAT.format(currentTime)+"]";
	}

}
