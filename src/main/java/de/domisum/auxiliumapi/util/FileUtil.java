package de.domisum.auxiliumapi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtil
{

	// READING
	public static String readFileToString(String path)
	{
		File file = new File(path);
		return readFileToString(file);
	}

	public static String readFileToString(File file)
	{
		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

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
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(br != null)
				try
				{
					br.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
		}
	}


	// WRITING
	public static void writeStringToFile(String path, String content)
	{
		writeStringToFile(new File(path), content);
	}

	public static void writeStringToFile(File file, String content)
	{
		file.getParentFile().mkdirs();

		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	// COPY
	public static void copyFile(File origin, File destinationDir)
	{
		if(!origin.exists())
			return;

		File destination = new File(destinationDir, origin.getName());

		try
		{
			Files.copy(origin.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void copyDirectory(File originFolder, File destinationDir)
	{
		copyDirectory(originFolder, destinationDir, null);
	}

	public static void copyDirectory(File originFolder, File destinationDir, FileFilter fileFilter)
	{
		destinationDir.mkdirs();

		for(File file : originFolder.listFiles())
		{
			if(fileFilter != null)
				if(fileFilter.isFiltered(file))
					continue;

			if(file.isFile())
				copyFile(file, destinationDir);
			else if(file.isDirectory())
			{
				File deeperDestination = new File(destinationDir, file.getName());
				deeperDestination.mkdirs();

				copyDirectory(file, deeperDestination, fileFilter);
			}
		}
	}


	public static void copyDirectory(File originFolder, String newName, File destinationDir)
	{
		destinationDir.mkdirs();

		for(File file : originFolder.listFiles())
		{
			if(file.isFile())
				copyFile(file, destinationDir);
			else if(file.isDirectory())
			{
				File deeperDestination = new File(destinationDir, newName != null ? newName : file.getName());
				deeperDestination.mkdirs();

				copyDirectory(file, null, deeperDestination);
			}
		}
	}


	// DELETING
	public static void deleteDirectory(File dir)
	{
		deleteDirectoryContents(dir);
		dir.delete();
	}

	public static void deleteDirectoryContents(File dir)
	{
		File[] files = dir.listFiles();
		for(File f : files)
		{
			if(f.isFile())
				f.delete();
			else
				deleteDirectory(f);
		}
	}


	// MISC
	public static boolean doesFileExist(String path)
	{
		File file = new File(path);

		return file.exists();
	}

	public static List<File> listFilesRecursively(File dir)
	{
		List<File> files = new ArrayList<>();

		for(File file : dir.listFiles())
		{
			files.add(file);

			if(file.isDirectory())
				files.addAll(listFilesRecursively(file));
		}

		return files;
	}

	public static boolean isDirectoryEmpty(File dir)
	{
		return listFilesRecursively(dir).size() == 0;
	}


	// SPECIFIC
	public static String getIdentifier(File baseDir, File file, String fileExtension)
	{
		String id = file.getPath().replaceFirst(baseDir.getPath(), "");
		id = id.replace('\\', '/');
		if(id.startsWith("/"))
			id = id.substring(1, id.length());

		id = TextUtil.replaceLast(id, fileExtension, "");

		return id;
	}


	// -------
	// FILE FILTER
	// -------
	public static class FileFilter
	{

		private List<String> containsFilters = new ArrayList<>();
		private List<String> endsWithFilters = new ArrayList<>();


		// -------
		// CONSTRUCTOR
		// -------
		public FileFilter()
		{

		}


		// -------
		// GETTERS
		// -------
		public boolean isFiltered(File file)
		{
			for(String filter : this.containsFilters)
				if(file.getPath().contains(filter))
					return true;

			for(String filter : this.endsWithFilters)
				if(file.getPath().endsWith(filter))
					return true;

			return false;
		}


		// -------
		// CHANGERS
		// -------
		public FileFilter addContains(String filter)
		{
			this.containsFilters.add(filter);

			return this;
		}

		public FileFilter addEndsWith(String filter)
		{
			this.endsWithFilters.add(filter);

			return this;
		}

	}

}
