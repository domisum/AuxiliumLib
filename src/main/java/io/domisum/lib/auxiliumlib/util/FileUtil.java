package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.IncompleteCodeError;
import io.domisum.lib.auxiliumlib.time.TimeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.ClosedByInterruptException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil
{
	
	// CONSTANTS
	@API
	public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;
	
	// TEMP
	private static final Collection<File> temporaryDirectories = new ArrayList<>();
	
	
	//  STRING
	@API
	public static String readString(File file)
	{
		return readString(file, DEFAULT_STRING_ENCODING);
	}
	
	@API
	public static String readString(File file, Charset encoding)
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
	
	
	@API
	public static void writeString(File file, String toWrite)
	{
		writeString(file, toWrite, DEFAULT_STRING_ENCODING);
	}
	
	@API
	public static void writeString(File file, String toWrite, Charset encoding)
	{
		try
		{
			createParentDirectory(file);
			Files.writeString(file.toPath(), toWrite, encoding);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	
	// RAW
	@API
	public static byte[] readRaw(File file)
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
	
	@API
	public static void writeRaw(File file, byte[] toWrite)
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
	
	
	// STREAM
	@API
	public static void writeStream(File file, InputStream inputStream)
	{
		try
		{
			writeStreamUncaught(file, inputStream);
		}
		catch(IOException e)
		{
			deleteFile(file);
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static void writeStreamUncaught(File file, InputStream inputStream)
		throws IOException
	{
		createParentDirectory(file);
		Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	
	// IMAGE
	@API
	public static BufferedImage readImage(File file)
	{
		try
		{
			return readImageUncaught(file);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException("Failed to read image " + file, e);
		}
	}
	
	@API
	public static BufferedImage readImageUncaught(File file)
		throws IOException
	{
		if(!file.exists())
			throw new FileNotFoundException("File doesn't exist: " + file);
		
		return ImageIO.read(file);
	}
	
	@API
	public static void writeImage(File file, BufferedImage image)
	{
		Validate.notNull(file, "file was null");
		Validate.notNull(image, "image was null");
		
		try
		{
			createParentDirectory(file);
			String extension = getExtension(file);
			if(extension == null || extension.isBlank())
				throw new IllegalArgumentException(PHR.r("Unsupported file extension in file name '{}'", file.getName()));
			
			boolean writerFound = ImageIO.write(image, extension, file);
			if(!writerFound)
				throw new IOException(PHR.r("Found no compatible image writer for format '{}' " +
					"and BufferedImage type {}", extension, image.getType()));
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	
	// COPY
	@API
	public static void copyFile(File from, File to)
	{
		if(to.exists() && to.isDirectory())
			throw new UncheckedIOException(new IOException("can't copy to file '" + to + "', it is a directory and already exists"));
		
		try
		{
			createParentDirectory(to);
			Files.copy(from.getAbsoluteFile().toPath(), to.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static void copyDirectory(File sourceRootDirectory, File targetRootDirectory, FileFilter... filters)
	{
		FileFilter combinedFilter = f ->
		{
			for(var filter : filters)
				if(!filter.accept(f))
					return false;
			return true;
		};
		
		try
		{
			FileUtils.copyDirectory(sourceRootDirectory, targetRootDirectory, combinedFilter);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	
	// MOVE
	@API
	public static void moveFile(File from, File to)
	{
		createParentDirectory(to);
		try
		{
			Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static void moveDirectory(File oldDirLocation, File newDirLocation)
	{
		mkdirs(newDirLocation);
		
		for(var file : oldDirLocation.listFiles())
		{
			var targetFile = new File(newDirLocation, file.getName());
			if(file.isDirectory())
				moveDirectory(file, targetFile);
			else
				moveFile(file, targetFile);
		}
		
		deleteDirectory(oldDirLocation);
	}
	
	
	// DIRECTORY
	@API
	public static void mkdirs(File dir)
	{
		if(dir.exists())
			return;
		
		dir.mkdirs();
		if(!dir.exists())
			throw new UncheckedIOException(new IOException("Failed to create directory '" + dir + "'"));
	}
	
	@API
	public static File getFileInSameDirectory(File file, String otherName)
	{
		return new File(file.getAbsoluteFile().getParent(), otherName);
	}
	
	@API
	public static void createParentDirectory(File file)
	{
		mkdirs(file.getAbsoluteFile().getParentFile());
	}
	
	@API
	public static void deleteDirectory(File directory)
	{
		try
		{
			FileUtils.deleteDirectory(directory);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static void deleteDirectoryContents(File directory)
	{
		try
		{
			FileUtils.cleanDirectory(directory);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	private static void validateIsNotFile(File directory)
	{
		if(directory.isFile())
			throw new IllegalArgumentException("given directory is file, not directory");
	}
	
	@API
	public static boolean isInDirectory(File directory, File file)
	{
		if(Objects.equals(directory, file))
			return true;
		
		try
		{
			String dirPath = directory.getCanonicalPath();
			if(!dirPath.endsWith(File.pathSeparator))
				dirPath += File.pathSeparator;
			
			String filePath = file.getCanonicalPath();
			if(!filePath.endsWith(File.pathSeparator))
				filePath += File.pathSeparator;
			
			return filePath.startsWith(dirPath);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static int deleteDirectoryContentsOlderThan(File directory, Duration maxAge)
	{
		var oldestAllowedFileDate = DateUtils.addMinutes(new Date(), -(int) TimeUtil.getMinutesDecimal(maxAge));
		var filesToDelete = FileUtils.iterateFiles(directory, new AgeFileFilter(oldestAllowedFileDate), null);
		
		int deletedCount = 0;
		while(filesToDelete.hasNext())
		{
			FileUtils.deleteQuietly(filesToDelete.next());
			deletedCount++;
		}
		return deletedCount;
	}
	
	
	// LIST FILES
	@API
	public static Collection<File> listFilesFlat(File directory, FileType fileType)
	{
		return listFiles(directory, fileType, false);
	}
	
	@API
	public static Collection<File> listFilesRecursively(File directory, FileType fileType)
	{
		return listFiles(directory, fileType, true);
	}
	
	private static Collection<File> listFiles(File directory, FileType fileType, boolean recursive)
	{
		validateIsNotFile(directory);
		if(!directory.exists())
			return Collections.emptySet();
		
		var directoryContents = new ConcurrentLinkedQueue<File>();
		try(var stream = Files.list(directory.toPath()))
		{
			stream.parallel().map(Path::toFile).forEach(f ->
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
	
	@API
	public static boolean isDirectoryEmpty(File directory)
	{
		try(var dirContents = Files.list(directory.toPath()))
		{
			return dirContents.findAny().isEmpty();
		}
		catch(IOException e)
		{
			throw new UncheckedIOException("Failed to check if directory is empty: '" + directory + "'", e);
		}
	}
	
	
	// TEMP
	@API
	public static File getNonExistentTemporaryFile()
	{
		return getNonExistentTemporaryFile(null);
	}
	
	@API
	public static File getNonExistentTemporaryFile(String extension)
	{
		File temporaryFile = createTemporaryFile(extension);
		String path = temporaryFile.getPath();
		
		deleteFile(temporaryFile);
		return new File(path);
	}
	
	@API
	public static File createTemporaryFile()
	{
		return createTemporaryFile(null);
	}
	
	@API
	public static File createTemporaryFile(String extension)
	{
		String cleanedExtension = extension;
		
		if(cleanedExtension == null)
			cleanedExtension = ".tmp";
		
		if(!cleanedExtension.startsWith("."))
			cleanedExtension = "." + cleanedExtension;
		
		
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
	
	@API
	public static File createTemporaryDirectory()
	{
		try
		{
			var directory = Files.createTempDirectory("tempDirectory").toFile();
			deleteDirectoryOnShutdown(directory);
			return directory;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	private static synchronized void deleteDirectoryOnShutdown(File directory)
	{
		if(temporaryDirectories.isEmpty())
			ThreadUtil.registerShutdownHook(() -> temporaryDirectories.forEach(FileUtil::deleteDirectory), "deleteTempDirs");
		
		temporaryDirectories.add(directory);
	}
	
	@API
	public static File getTempDirectory()
	{
		return getNonExistentTemporaryFile("shouldBeDeletedInstantly").getAbsoluteFile().getParentFile();
	}
	
	
	// LAST MODIFIED
	@API
	public static Instant getContentLastModified(File file)
	{
		if(!file.isDirectory())
			return getLastModified(file);
		
		Collection<File> files = listFilesFlat(file, FileType.FILE_AND_DIRECTORY);
		if(files.isEmpty())
			return getLastModified(file);
		
		Instant mostRecentModified = Instant.MIN;
		for(File f : files)
		{
			Instant fLastModified = getContentLastModified(f);
			if(fLastModified.compareTo(mostRecentModified) > 0)
				mostRecentModified = fLastModified;
		}
		
		return mostRecentModified;
	}
	
	@API
	public static Instant getLastModified(File file)
	{
		return Instant.ofEpochMilli(file.lastModified());
	}
	
	@API
	public static void markAsModified(File file)
	{
		file.setLastModified(Instant.now().toEpochMilli());
	}
	
	
	// GENERAL FILE
	@API
	public static void createEmptyFile(File file)
	{
		try
		{
			file.createNewFile();
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static void delete(File file)
	{
		if(file.isDirectory())
			deleteDirectory(file);
		else
			deleteFile(file);
	}
	
	@API
	public static void deleteFile(File file)
	{
		if(!file.exists())
			return;
		
		try
		{
			Files.delete(file.toPath());
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	
	// EXTENSION
	@API
	public static String getExtension(File file)
	{
		return getExtension(file.getName());
	}
	
	/**
	 * Returns the exension of the file without a preceding dot.
	 *
	 * @param fileName the name of the file of which to determine the extension
	 * @return file extension without dot
	 */
	@API
	public static String getExtension(String fileName)
	{
		return FilenameUtils.getExtension(fileName);
	}
	
	/**
	 * Returns the composite exension of the file without a preceding dot.
	 * <p>
	 * Examples:
	 * <ul>
	 *     <li>folder/data.json -> 'json'</li>
	 *     <li>test.xd/archive.tar.gz -> 'tar.gz'</li>
	 *     <li>a/b/c/name.very.long.extension.ok -> 'very.long.extension.ok'</li>
	 * </ul>
	 *
	 * @param file the file of which to determine the extension
	 * @return composite file extension without dot
	 */
	@API
	public static String getCompositeExtension(File file)
	{
		String fileName = file.getName();
		if(!fileName.contains("."))
			return "";
		
		return fileName.substring(fileName.indexOf('.') + 1);
	}
	
	@API
	public static String getNameWithoutCompositeExtension(File file)
	{
		String compositeFileExtension = getCompositeExtension(file);
		String fileName = file.getName();
		
		if(compositeFileExtension.length() == 0)
			return fileName;
		
		String fileNameWithout = fileName.substring(0, fileName.length() - compositeFileExtension.length() - 1);
		return fileNameWithout;
	}
	
	
	// PATH
	@API
	public static String replaceDelimitersWithForwardSlash(String path)
	{
		return path.replaceAll(StringUtil.escapeStringForRegex("\\"), "/");
	}
	
	
	// RESOURCE
	@API
	public static String readStringResource(String resourcePath)
	{
		try
		{
			return readStringResourceRaw(resourcePath);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
	@API
	public static String readStringResourceRaw(String resourcePath)
		throws IOException
	{
		var resourceStream = FileUtil.class.getClassLoader().getResourceAsStream(resourcePath);
		return IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
	}
	
	
	// UTIL
	@API
	public enum FileType
	{
		
		FILE,
		DIRECTORY,
		FILE_AND_DIRECTORY;
		
		public boolean isOfType(File file)
		{
			if(!file.exists())
				return false;
			
			if(this == FILE_AND_DIRECTORY)
				return true;
			else if(this == FILE)
				return file.isFile();
			else if(this == DIRECTORY)
				return file.isDirectory();
			
			throw new IncompleteCodeError("unknown file type: " + this);
		}
		
	}
	
}
