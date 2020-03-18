package io.domisum.lib.auxiliumlib.contracts.storage;

import io.domisum.lib.auxiliumlib.contracts.Identifyable;
import io.domisum.lib.auxiliumlib.contracts.serialization.ToStringSerializer;
import io.domisum.lib.auxiliumlib.file.FileUtil;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@API
@RequiredArgsConstructor
public class SerializedIdentifyableStorage<T extends Identifyable> implements Storage<String, T>
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final File directory;
	private final String fileExtension;
	private final ToStringSerializer<T> serializer;


	// SOURCE
	@Override
	public Optional<T> fetch(String id)
	{
		File file = new File(directory, id+getFileExtension());
		if(!file.exists())
			return Optional.empty();

		return loadFromFile(file);
	}

	@Override
	public Collection<T> fetchAll()
	{
		Collection<File> files = FileUtil.listFilesFlat(directory, FileUtil.FileType.FILE);

		List<T> storageItems = new ArrayList<>();
		for(File f : files)
			loadFromFile(f).ifPresent(storageItems::add);

		return storageItems;
	}


	@Override
	public boolean contains(String id)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void store(T item)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(String key)
	{
		throw new UnsupportedOperationException();
	}


	// FILE
	private Optional<T> loadFromFile(File file)
	{
		String extension = FileUtil.getCompositeExtension(file);
		if(!Objects.equals(getFileExtension(), extension))
		{
			logger.warn("Storage directory contains file with invalid extension ({}), skipping: {}", extension, file.getName());
			return Optional.empty();
		}

		T deserialized;
		try
		{
			String fileContent = FileUtil.readString(file);
			deserialized = serializer.deserialize(fileContent);
		}
		catch(RuntimeException e)
		{
			logger.error("An error occured while deserializing {}", file, e);
			return Optional.empty();
		}

		injectId(deserialized, FileUtil.getNameWithoutCompositeExtension(file));

		return Optional.of(deserialized);
	}

	private void injectId(T injectInto, String id)
	{
		try
		{
			Field idField = findIdField(injectInto.getClass());
			idField.setAccessible(true);
			idField.set(injectInto, id);
		}
		catch(NoSuchFieldException|IllegalAccessException e)
		{
			throw new IllegalStateException("could not set id using reflection", e);
		}
	}

	private Field findIdField(Class<?> clazz) throws NoSuchFieldException
	{
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields)
			if("id".equals(f.getName()))
				return f;

		if(clazz.getSuperclass() == Object.class)
			throw new NoSuchFieldException("no field called id neither in class nor in superclasses");

		return findIdField(clazz.getSuperclass());
	}


	// UTIL
	private String getFileExtension()
	{
		if(fileExtension.startsWith("."))
			return fileExtension;

		return "."+fileExtension;
	}

}