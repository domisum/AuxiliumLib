package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.file.DirectoryCopy;
import de.domisum.lib.auxilium.util.file.FileFilter;
import de.domisum.lib.auxilium.util.java.ThreadUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;

	// TEMP
	private static final Collection<File> temporaryDirectories = new ArrayList<>();


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
		catch(ClosedByInterruptException ignored)
		{
			// ignore this, because the thread was interrupted and no result is expected
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// IMAGE
	@API public static BufferedImage readImage(File file)
	{
		try
		{
			return ImageIO.read(file);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void writeImage(File file, BufferedImage image)
	{
		Validate.notNull(file, "file was null");
		Validate.notNull(image, "image was null");

		try
		{
			ImageIO.write(image, getExtension(file), file);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// COPY
	@API public static void copyFile(File from, File to)
	{
		try
		{
			Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void coypDirectory(File sourceRootDirectory, File targetRootDirectory, FileFilter... filters)
	{
		DirectoryCopy.fromTo(sourceRootDirectory, targetRootDirectory, filters).copy();
	}


	// DIRECTORY
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

		for(File file : listFiles(directory, FileType.FILE))
			deleteFile(file);

		for(File dir : listFiles(directory, FileType.DIRECTORY))
			deleteDirectory(dir);
	}

	private static void validateIsNotFile(File directory)
	{
		if(directory.isFile())
			throw new IllegalArgumentException("given directory is file, not directory");
	}


	@API public static Collection<File> listFiles(File directory, FileType fileType)
	{
		validateIsNotFile(directory);
		Collection<File> directoryContents = new ArrayList<>();

		File[] files = directory.listFiles();
		if(files != null)
			for(File f : files)
				if(fileType.isOfType(f))
					directoryContents.add(f);

		return directoryContents;
	}


	// TEMP
	@API public static File createTemporaryFile()
	{
		return createTemporaryFile(null);
	}

	@API public static File createTemporaryFile(String extension)
	{
		String cleanedExtension = extension;
		if((cleanedExtension != null) && !cleanedExtension.startsWith("."))
			cleanedExtension = "."+cleanedExtension;

		try
		{
			File file = File.createTempFile("tempFile", cleanedExtension);
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
			ThreadUtil.registerShutdownHook(()->temporaryDirectories.forEach(FileUtil::deleteDirectory));

		temporaryDirectories.add(directory);
	}


	// GENERAL FILE
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

	@API public static String getExtension(File file)
	{
		return FilenameUtils.getExtension(file.getName());
	}

	@API public static String getCompositeExtension(File file)
	{
		String fileName = file.getName();
		if(!fileName.contains("."))
			return "";

		return fileName.substring(fileName.indexOf('.'));
	}

	@API public static String getNameWithoutCompositeExtension(File file)
	{
		String compositeFileExtension = getCompositeExtension(file);
		String fileName = file.getName();

		String fileNameWithout = fileName.substring(0, fileName.length()-compositeFileExtension.length());
		return fileNameWithout;
	}


	@API public static String getFilePath(File file)
	{
		String path = file.getAbsoluteFile().getPath();
		path = unifyDelimiters(path);

		return path;
	}

	@API public static String unifyDelimiters(String path)
	{
		return path.replaceAll(StringUtil.escapeStringForRegex("\\"), "/");
	}


	public enum FileType
	{

		FILE,
		DIRECTORY,
		FILE_AND_DIRECTORY;

		public boolean isOfType(File file)
		{
			if(!file.exists())
				throw new IllegalArgumentException("file does not exist: "+file);

			if(this == FILE_AND_DIRECTORY)
				return true;
			else if(this == FILE)
				return file.isFile();
			else if(this == DIRECTORY)
				return file.isDirectory();

			throw new ShouldNeverHappenError("unknown file type: "+this);
		}

	}

}
