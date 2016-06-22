package de.domisum.auxiliumapi.util.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class ClazzUtil
{

	public static Class<?> getClass(String path)
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

	public static List<Class<?>> getClasses(String path)
	{
		ClassPath classPath;
		try
		{
			classPath = ClassPath.from(ClazzUtil.class.getClassLoader());
			Set<ClassInfo> classInfo = classPath.getTopLevelClassesRecursive(path);
			Iterator<ClassInfo> iterator = classInfo.iterator();

			List<Class<?>> classes = new ArrayList<Class<?>>();

			while(iterator.hasNext())
			{
				ClassInfo ci = iterator.next();
				Class<?> clazz = ClazzUtil.getClass(ci.getName());
				classes.add(clazz);
			}

			return classes;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
