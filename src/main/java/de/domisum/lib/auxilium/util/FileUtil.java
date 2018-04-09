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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

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
			createParentDirectory(file);
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
			createParentDirectory(file);
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
			if(!file.exists())
				throw new IOException("file "+file+" doesn't exist");

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
			createParentDirectory(file);
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
		if(to.exists() && to.isDirectory())
			throw new UncheckedIOException(new IOException(
					"can't copy to file '"+to+"', it is a directory and already "+"exists"));

		try
		{
			to.getAbsoluteFile().getParentFile().mkdirs();
			Files.copy(from.getAbsoluteFile().toPath(), to.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void copyDirectory(File sourceRootDirectory, File targetRootDirectory, FileFilter... filters)
	{
		DirectoryCopy.fromTo(sourceRootDirectory, targetRootDirectory, filters).copy();
	}


	// DIRECTORY
	@API public static File getFileInSameDirectory(File file, String otherName)
	{
		return new File(file.getAbsoluteFile().getParent(), otherName);
	}

	@API public static void createParentDirectory(File file)
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
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

		for(File file : listFilesFlat(directory, FileType.FILE))
			deleteFile(file);

		for(File dir : listFilesFlat(directory, FileType.DIRECTORY))
			deleteDirectory(dir);
	}

	private static void validateIsNotFile(File directory)
	{
		if(directory.isFile())
			throw new IllegalArgumentException("given directory is file, not directory");
	}


	@API public static Collection<File> listFilesFlat(File directory, FileType fileType)
	{
		return listFiles(directory, fileType, false);
	}

	@API public static Collection<File> listFilesRecursively(File directory, FileType fileType)
	{
		return listFiles(directory, fileType, true);
	}

	private static Collection<File> listFiles(File directory, FileType fileType, boolean recursive)
	{
		validateIsNotFile(directory);
		if(!directory.exists())
			return Collections.emptySet();

		Collection<File> directoryContents = new ConcurrentLinkedQueue<>();
		try(Stream<Path> stream = Files.list(directory.toPath()))
		{
			stream.parallel().map(Path::toFile).forEach(f->
			{
				if(fileType.isOfType(f))
					directoryContents.add(f);

				if(recursive && f.isDirectory())
					directoryContents.addAll(listFilesRecursively(f, fileType));
			});
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}

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

	@API public static Instant getLastModified(File file)
	{
		return Instant.ofEpochMilli(file.lastModified());
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
