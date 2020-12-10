package io.domisum.lib.auxiliumlib.util;

import com.google.common.reflect.ClassPath;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassUtil
{
	
	@API
	public static Class<?> getClass(String className, String errorMessage)
	{
		try
		{
			return Class.forName(className);
		}
		catch(ClassNotFoundException e)
		{
			throw new IllegalArgumentException(errorMessage, e);
		}
	}
	
	@API
	public static Optional<Class<?>> getClassOptional(String className)
	{
		try
		{
			return Optional.ofNullable(Class.forName(className));
		}
		catch(ClassNotFoundException ignored)
		{
			return Optional.empty();
		}
	}
	
	@API
	public static Collection<Class<?>> getClassesWithSimpleName(String simpleName, String... searchPackages)
	{
		var classes = new HashSet<Class<?>>();
		for(String searchPackage : searchPackages)
			for(var clazz : getClasses(searchPackage))
				if(Objects.equals(simpleName, clazz.getSimpleName()))
					classes.add(clazz);
		
		return classes;
	}
	
	
	@API
	@SuppressWarnings("UnstableApiUsage")
	public static Collection<Class<?>> getClasses(String _package)
	{
		try
		{
			var classPath = ClassPath.from(ClassUtil.class.getClassLoader());
			var classInfos = classPath.getTopLevelClassesRecursive(_package);
			
			var classes = new ArrayList<Class<?>>();
			for(var classInfo : classInfos)
				getClassOptional(classInfo.getName()).ifPresent(classes::add);
			
			return classes;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
}
