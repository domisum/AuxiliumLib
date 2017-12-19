package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;

	// TEMP
	private static Collection<File> temporaryDirectories = new ArrayList<>();


	//  STRING
	@API public static String readString(File file)
	{
		return readString(file, DEFAULT_STRING_ENCODING);
	}

	@API public static String readString(File file, Charset encoding)
	{
		try
		{
			byte[] contentBytes = Files.readAllBytes(file.toPath());
			return new String(contentBytes, encoding);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	@API public static void writeString(File file, String toWrite)
	{
		writeString(file, toWrite, DEFAULT_STRING_ENCODING);
	}

	@API public static void writeString(File file, String toWrite, Charset encoding)
	{
		try
		{
			FileUtils.writeStringToFile(file, toWrite, encoding);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// RAW
	@API public static byte[] readRaw(File file)
	{
		try
		{
			return Files.readAllBytes(file.toPath());
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void writeRaw(File file, byte[] toWrite)
	{
		try
		{
			Files.write(file.toPath(), toWrite);
		}
		catch(IOException e) // TODO should fail on interrupt exception?
		{
			throw new UncheckedIOException(e);
		}
	}


	// DIRECTORY
	@API public static void deleteFile(File file)
	{
		try
		{
			Files.delete(file.toPath());
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void deleteDirectory(File directory)
	{
		if(!directory.exists())
			return;
		validateIsNotFile(directory);

		deleteDirectoryContents(directory);
		deleteFile(directory);
	}

	@API public static void deleteDirectoryContents(File directory)
	{
		if(!directory.exists())
			return;
		validateIsNotFile(directory);

		for(File file : getDirectoryContents(directory))
			if(file.isFile())
				deleteFile(file);
			else
				deleteDirectory(file);
	}

	private static void validateIsNotFile(File directory)
	{
		if(directory.isFile())
			throw new IllegalArgumentException("given directory is file, not directory");
	}


	@API public static Collection<File> getDirectoryContents(File directory)
	{
		validateIsNotFile(directory);
		Collection<File> directoryContents = new ArrayList<>();

		File[] files = directory.listFiles();
		if(files != null)
			directoryContents.addAll(Arrays.asList(files));

		return directoryContents;
	}


	// TEMP
	@API public static File createTemporaryFile()
	{
		return createTemporaryFile(null);
	}

	@API public static File createTemporaryFile(String extension)
	{
		if(extension != null && !extension.startsWith("."))
			extension = "."+extension;

		try
		{
			File file = File.createTempFile("tempFile", extension);
			file.deleteOnExit();
			return file;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static File createTemporaryDirectory()
	{
		try
		{
			File directory = Files.createTempDirectory("tempDirectory").toFile();
			deleteDirectoryOnShutdown(directory);
			return directory;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	private static void deleteDirectoryOnShutdown(File directory)
	{
		if(temporaryDirectories.isEmpty())
			ThreadUtil.addShutdownHook(()->temporaryDirectories.forEach(FileUtil::deleteDirectory));

		temporaryDirectories.add(directory);
	}

}
