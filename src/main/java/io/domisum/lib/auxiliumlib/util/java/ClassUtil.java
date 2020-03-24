package io.domisum.lib.auxiliumlib.util.java;

import com.google.common.reflect.ClassPath;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassUtil
{
	
	// CLASS
	@API
	public static Optional<Class<?>> getClass(String path)
	{
		try
		{
			return Optional.ofNullable(Class.forName(path));
		}
		catch(ClassNotFoundException ignored)
		{
			return Optional.empty();
		}
	}
	
	@API
	@SuppressWarnings("UnstableApiUsage")
	public static List<Class<?>> getClasses(String path)
	{
		try
		{
			var classPath = ClassPath.from(ClassUtil.class.getClassLoader());
			var classInfos = classPath.getTopLevelClassesRecursive(path);
			
			var classes = new ArrayList<Class<?>>();
			for(var classInfo : classInfos)
				getClass(classInfo.getName()).ifPresent(classes::add);
			
			return classes;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
}
