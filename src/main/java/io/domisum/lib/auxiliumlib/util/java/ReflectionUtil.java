package io.domisum.lib.auxiliumlib.util.java;

import com.google.common.reflect.TypeToken;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.ShouldNeverHappenError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtil
{
	
	// INJECT
	@API
	public static void injectValue(Object object, String fieldName, Object value)
	{
		try
		{
			injectValueUncaught(object, fieldName, value);
		}
		catch(IllegalAccessException e)
		{
			throw new ShouldNeverHappenError(e);
		}
	}
	
	@API
	public static void injectValueUncaught(Object object, String fieldName, Object value)
			throws IllegalAccessException
	{
		for(var field : getAllFields(object))
			if(field.getName().equals(fieldName))
				field.set(object, value);
	}
	
	
	@API
	public static void injectValue(Object object, Class<?> valueClass, Object value)
	{
		try
		{
			injectValueUncaught(object, valueClass, value);
		}
		catch(IllegalAccessException e)
		{
			throw new ShouldNeverHappenError(e);
		}
	}
	
	@API
	public static void injectValueUncaught(Object object, Class<?> valueClass, Object value)
			throws IllegalAccessException
	{
		for(var field : getAllFields(object))
			if(field.getType().isAssignableFrom(valueClass))
				field.set(object, value);
	}
	
	
	// HELPER
	@API
	public static Set<Field> getAllFields(Object object)
	{
		var fields = new HashSet<Field>();
		Class<?> clazz = object.getClass();
		do
		{
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		while(clazz != Object.class);
		
		fields.forEach(f->f.setAccessible(true));
		return fields;
	}
	
}