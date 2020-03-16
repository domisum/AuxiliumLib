package io.domisum.lib.auxiliumlib.util.java;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
	public static List<Class<?>> getClasses(String path)
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
				Optional<Class<?>> classOptional = getClass(ci.getName());
				classOptional.ifPresent(classes::add);
			}

			return classes;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

}
