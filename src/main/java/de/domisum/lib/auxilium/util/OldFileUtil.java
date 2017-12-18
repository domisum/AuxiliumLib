package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Deprecated
@API
public class OldFileUtil
{

	// READ
	@API public static String readFileToString(File file)
	{
		byte[] contentBytes = readFileToByteArray(file);
		return new String(contentBytes);
	}

	@API public static String readInputStreamToString(InputStream inputStream)
	{
		StringBuilder sb = new StringBuilder();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF8")))
		{
			String line = br.readLine();
			while(line != null)
			{
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}

			return sb.toString();
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static byte[] readFileToByteArray(File file)
	{
		try
		{
			return Files.readAllBytes(file.toPath());
		}
		catch(IOException e)
		{
			if(e instanceof ClosedByInterruptException)
				return null;

			throw new UncheckedIOException(e);
		}
	}

	@API public static BufferedImage readImage(File file)
	{
		if(!file.exists())
			throw new IllegalArgumentException("The file '"+file.getAbsoluteFile().getPath()+"' does not exits");

		try
		{
			return ImageIO.read(file);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// WRITE
	@API public static void writeStringToFile(File file, String content)
	{
		try
		{
			createParentDirectory(file);
			try(FileWriter fw = new FileWriter(file))
			{
				fw.write(content);
			}
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void writeByteArrayToFile(File file, byte[] data)
	{
		try
		{
			Files.write(file.toPath(), data);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void writeImage(File file, BufferedImage image)
	{
		File parent = file.getAbsoluteFile().getParentFile();
		if(!parent.exists())
			parent.mkdirs();

		if(image == null)
			throw new IllegalArgumentException("The image can't be null");

		try
		{
			ImageIO.write(image, "png", file);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// COPY
	@API public static void copyFile(File origin, File destinationDir)
	{
		copyFile(origin, destinationDir, origin.getName());
	}

	@API public static void copyFile(File origin, File destinationDir, String newFileName)
	{
		if(!origin.exists())
			return;

		File destination = new File(destinationDir, newFileName);
		try
		{
			createDirectory(destinationDir);
			Files.copy(origin.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API public static void copyDirectory(File originFolder, File destinationDir)
	{
		copyDirectory(originFolder, destinationDir, null);
	}

	@API public static void copyDirectory(File originFolder, File destinationDir, FilePathFilter filePathFilter)
	{
		try
		{
			createDirectory(destinationDir);

			for(File file : listFiles(originFolder))
			{
				if(filePathFilter != null && filePathFilter.isFilteredOut(file))
					continue;

				if(file.isFile())
					copyFile(file, destinationDir);
				else if(file.isDirectory())
				{
					File deeperDestination = new File(destinationDir, file.getName());
					createDirectory(deeperDestination);

					copyDirectory(file, deeperDestination, filePathFilter);
				}
			}
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	@API public static void copyDirectory(File originFolder, String newName, File destinationDir)
	{
		try
		{
			createDirectory(destinationDir);

			for(File file : listFiles(originFolder))
			{
				if(file.isFile())
					copyFile(file, destinationDir);
				else if(file.isDirectory())
				{
					File deeperDestination = new File(destinationDir, newName != null ? newName : file.getName());
					createDirectory(deeperDestination);

					copyDirectory(file, null, deeperDestination);
				}
			}
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// DELETE
	@API public static void deleteDirectory(File dir)
	{
		deleteDirectoryContents(dir);
		deleteFile(dir);
	}

	@API public static void deleteDirectoryContents(File dir)
	{
		if(dir == null)
			throw new IllegalArgumentException("The directory can't be null");

		for(File f : listFiles(dir))
		{
			if(f.isFile())
				deleteFile(f);
			else
				deleteDirectory(f);
		}
	}

	private static void deleteFile(File file)
	{
		try
		{
			boolean success = file.delete();
			if(!success)
				throw new IOException("Deleting file/dir '"+file+"' failed");
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// DIRECTORY
	private static void createParentDirectory(File file) throws IOException
	{
		createDirectory(file.getAbsoluteFile().getParentFile());
	}

	private static void createDirectory(File dir) throws IOException
	{
		if(dir.isDirectory())
			return;

		boolean success = dir.mkdirs();
		if(!success)
			throw new IOException("Failed to create directory directory '"+dir+"'");
	}


	// TEMP
	@API public static File createTempFile(String prefix, String fileExtension)
	{
		try
		{
			return File.createTempFile(prefix, "."+fileExtension);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// MISC
	@API public static boolean doesFileExist(String path)
	{
		File file = new File(path);
		return file.exists();
	}

	@API public static boolean isDirectoryEmpty(File dir)
	{
		return listFilesRecursively(dir).size() == 0;
	}


	@API public static File[] listFiles(File dir)
	{
		if(!dir.isDirectory())
			return new File[0];

		return dir.listFiles();
	}


	@API public static List<File> listFilesRecursively(File dir)
	{
		return listFilesRecursively(dir, true);
	}

	@API public static List<File> listFilesRecursively(File dir, boolean includeDirs)
	{
		List<File> files = new ArrayList<>();

		if(!dir.exists())
			return files;

		File[] filesArray = dir.listFiles();
		// noinspection ConstantConditions
		for(File file : filesArray)
		{
			if(file.isDirectory())
			{
				files.addAll(listFilesRecursively(file, includeDirs));

				if(!includeDirs)
					continue;
			}

			files.add(file);
		}

		return files;
	}


	// SPECIFIC
	@API public static String getIdentifier(File baseDir, File file, String fileExtension)
	{
		String id = file.getPath().replaceFirst(baseDir.getPath(), "");
		id = id.replace('\\', '/');
		if(id.startsWith("/"))
			id = id.substring(1, id.length());

		id = StringUtil.replaceLast(id, fileExtension, "");

		return id;
	}


	// FILE FILTER
	@API
	public static class FilePathFilter
	{

		private Set<String> startsWithFilters = new HashSet<>();
		private Set<String> containsFilters = new HashSet<>();
		private Set<String> endsWithFilters = new HashSet<>();


		// GETTERS
		protected boolean isFilteredOut(File file)
		{
			for(String filter : this.startsWithFilters)
				if(file.getAbsolutePath().startsWith(filter))
					return true;

			for(String filter : this.containsFilters)
				if(file.getAbsolutePath().contains(filter))
					return true;

			for(String filter : this.endsWithFilters)
				if(file.getAbsolutePath().endsWith(filter))
					return true;

			return false;
		}


		// CHANGERS
		@API public FilePathFilter addStartsWith(String filter)
		{
			this.startsWithFilters.add(filter);
			return this;
		}

		@API public FilePathFilter addContains(String filter)
		{
			this.containsFilters.add(filter);
			return this;
		}

		@API public FilePathFilter addEndsWith(String filter)
		{
			this.endsWithFilters.add(filter);
			return this;
		}

	}

}
