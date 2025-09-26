package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil
{
	
	// CONTAINS
	@API
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> Set<T> getAllContained(Throwable throwable, Class<T> type)
	{
		ValidationUtil.notNull(throwable, "throwable");
		
		var contained = new HashSet<T>();
		if(type.equals(throwable.getClass()))
			contained.add((T) throwable);
		
		var cause = throwable.getCause();
		if(cause != null)
			contained.addAll(getAllContained(cause, type));
		
		return contained;
	}
	
	@API
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> Optional<T> getSingleContained(Throwable throwable, Class<T> type)
	{
		ValidationUtil.notNull(type, "type");
		if(throwable == null)
			return Optional.empty();
		
		if(type.isInstance(throwable))
			return Optional.of((T) throwable);
		
		var cause = throwable.getCause();
		if(cause == null)
			return Optional.empty();
		return getSingleContained(cause, type);
	}
	
	@API
	public static boolean doesContain(Throwable throwable, Class<? extends Throwable> type)
	{
		return getSingleContained(throwable, type).isPresent();
	}
	
	
	// TO STRING
	@API
	public static String convertToString(Throwable throwable)
	{
		var stringBuilder = new StringBuilder();
		stringBuilder.append(throwable.toString()).append("\n");
		
		for(var stackTraceElement : throwable.getStackTrace())
			stringBuilder.append("    ").append(stackTraceElement.toString()).append("\n");
		
		if(throwable.getCause() != null)
			stringBuilder.append("Caused by: ").append(convertToString(throwable.getCause()));
		
		return stringBuilder.toString();
	}
	
	@API
	public static String getSynopsis(Throwable throwable)
	{
		String causeSynopsis = throwable.getCause() == null ? null : getSynopsis(throwable.getCause());
		String synopsis = throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
		if(causeSynopsis != null)
			synopsis += "; caused by: (" + causeSynopsis + ")";
		
		synopsis = synopsis.replaceAll("\n", "<br>");
		return synopsis;
	}
	
}
