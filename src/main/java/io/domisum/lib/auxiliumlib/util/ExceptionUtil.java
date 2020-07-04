package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil
{
	
	// CONTAINS
	@API
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> Optional<T> getContained(Throwable throwable, Class<T> type)
	{
		ValidationUtil.notNull(throwable, "throwable");
		
		if(type.equals(throwable.getClass()))
			return Optional.of((T) throwable);
		
		var cause = throwable.getCause();
		if(cause == null)
			return Optional.empty();
		
		return getContained(cause, type);
	}
	
	@API
	public static boolean doesContain(Throwable throwable, Class<? extends Throwable> type)
	{
		return getContained(throwable, type).isPresent();
	}
	
	
	// TO STRING
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
	public static String getSynopsis(Throwable throwable)
	{
		String causeSynopsis = throwable.getCause() == null ? null : getSynopsis(throwable.getCause());
		String synopsis = throwable.getClass().getSimpleName()+": "+throwable.getMessage();
		if(causeSynopsis != null)
			synopsis += "; caused by: ("+causeSynopsis+")";
		
		return synopsis;
	}
	
}
