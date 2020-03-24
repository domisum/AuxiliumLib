package io.domisum.lib.auxiliumlib.util.java;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil
{

	@API
	public static String convertThrowableToString(Throwable throwable)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(throwable.toString()).append("\n");

		for(StackTraceElement stackTraceElement : throwable.getStackTrace())
			stringBuilder.append("    ").append(stackTraceElement.toString()).append("\n");

		if(throwable.getCause() != null)
			stringBuilder.append("Caused by: ").append(convertThrowableToString(throwable.getCause()));

		return stringBuilder.toString();
	}

	@API
	public static boolean doesThrowableContain(Throwable throwable, Class<? extends Throwable> type)
	{
		if(throwable == null)
			return false;

		if(type.isAssignableFrom(throwable.getClass()))
			return true;

		return doesThrowableContain(throwable.getCause(), type);
	}

}
