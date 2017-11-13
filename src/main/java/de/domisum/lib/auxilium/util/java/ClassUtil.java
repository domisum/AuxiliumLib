package de.domisum.lib.auxilium.util.java;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import de.domisum.lib.auxilium.util.java.annotations.API;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@API
public class ClassUtil
{

	@API public static Class<?> getClass(String path)
	{
		try
		{
			return Class.forName(path);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@API public static List<Class<?>> getClasses(String path)
	{
		try
		{
			ClassPath classPath = ClassPath.from(ClassUtil.class.getClassLoader());
			Set<ClassInfo> classInfo = classPath.getTopLevelClassesRecursive(path);
			Iterator<ClassInfo> iterator = classInfo.iterator();

			List<Class<?>> classes = new ArrayList<>();

			while(iterator.hasNext())
			{
				ClassInfo ci = iterator.next();
				Class<?> clazz = ClassUtil.getClass(ci.getName());
				classes.add(clazz);
			}

			return classes;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
