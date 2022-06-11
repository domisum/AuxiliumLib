package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.ProgrammingError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
			throw new ProgrammingError(e);
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
			throw new ProgrammingError(e);
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
	
	
	// COPY
	@API
	public static void copyValue(Field field, Object from, Object to)
	{
		try
		{
			copyValueUncaught(field, from, to);
		}
		catch(IllegalAccessException e)
		{
			throw new ProgrammingError(e);
		}
	}
	
	@API
	public static void copyValueUncaught(Field field, Object from, Object to)
		throws IllegalAccessException
	{
		Object value = field.get(from);
		field.set(to, value);
	}
	
	
	// READ
	@API
	public static <T> T readFieldValue(Object from, String fieldName, Class<T> type)
	{
		try
		{
			// noinspection unchecked
			return (T) FieldUtils.readField(from, fieldName, true);
		}
		catch(IllegalAccessException e)
		{
			throw new ProgrammingError(e);
		}
	}
	
	
	
	// INIT
	@SuppressWarnings("unchecked")
	@API
	public static <T> T instantiateNoArgs(Class<T> clazz)
	{
		var constructor = clazz.getConstructors()[0];
		var o = instantiateObject(constructor);
		
		return (T) o;
	}
	
	private static Object instantiateObject(Constructor<?> constructor)
	{
		try
		{
			int parameterCount = constructor.getParameterCount();
			return constructor.newInstance(new Object[parameterCount]);
		}
		catch(InstantiationException|IllegalAccessException|InvocationTargetException e)
		{
			throw new ProgrammingError("Failed to instantiate object with constructor "+constructor, e);
		}
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
