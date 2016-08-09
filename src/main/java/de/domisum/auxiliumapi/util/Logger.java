package de.domisum.auxiliumapi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger
{

	// CONSTANTS
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("z HH:mm:ss", Locale.GERMAN);


	// -------
	// LOGGING
	// -------
	public static void info(String message)
	{
		String formattedMessage = getTimePrefix()+" [INFO] "+message;

		System.out.println(formattedMessage);
	}

	public static void warn(String message)
	{
		String formattedMessage = getTimePrefix()+" [WARN] "+message;

		System.out.println(formattedMessage);
	}

	public static void err(String message)
	{
		String formattedMessage = getTimePrefix()+" [ERR] "+message;

		System.err.println(formattedMessage);
	}

	public static void err(String message, Exception e)
	{
		err(message);

		if(e != null)
			e.printStackTrace();
	}


	private static String getTimePrefix()
	{
		Date currentTime = new Date(System.currentTimeMillis());

		return "["+timeFormat.format(currentTime)+"]";
	}

}
