package de.domisum.lib.auxilium.util.java;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@APIUsage
public class ClazzUtil
{

	@APIUsage
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

	@APIUsage
	public static List<Class<?>> getClasses(String path)
	{
		ClassPath classPath;
		try
		{
			classPath = ClassPath.from(ClazzUtil.class.getClassLoader());
			Set<ClassInfo> classInfo = classPath.getTopLevelClassesRecursive(path);
			Iterator<ClassInfo> iterator = classInfo.iterator();

			List<Class<?>> classes = new ArrayList<>();

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
