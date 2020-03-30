package io.domisum.lib.auxiliumlib.util.java;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil
{
	
	@API
	public static String convertToString(Throwable throwable)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(throwable.toString()).append("\n");
		
		for(StackTraceElement stackTraceElement : throwable.getStackTrace())
			stringBuilder.append("    ").append(stackTraceElement.toString()).append("\n");
		
		if(throwable.getCause() != null)
			stringBuilder.append("Caused by: ").append(convertToString(throwable.getCause()));
		
		return stringBuilder.toString();
	}
	
	@API
	public static boolean doesContain(Throwable throwable, Class<? extends Throwable> type)
	{
		if(throwable == null)
			return false;
		
		if(type.isAssignableFrom(throwable.getClass()))
			return true;
		
		return doesContain(throwable.getCause(), type);
	}
	
	@API
	public static String getShortSynopsis(Throwable throwable)
	{
		String causeSynopsis = throwable.getCause() == null ? null : getShortSynopsis(throwable.getCause());
		String synopsis = throwable.getClass().getSimpleName()+": "+throwable.getMessage();
		if(causeSynopsis != null)
			synopsis += "; caused by: ("+causeSynopsis+")";
		
		return synopsis;
	}
	
}
