package de.domisum.auxiliumapi.util.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		try
		{
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("\\Z");

			if(!scanner.hasNext())
			{
				scanner.close();
				return null;
			}

			String string = scanner.next();
			scanner.close();
			return string;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
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


	// DELETING
	public static void deleteDirectory(File file)
	{
		File[] files = file.listFiles();
		for(File f : files)
		{
			if(f.isFile())
				f.delete();
			else
				deleteDirectory(f);
		}

		file.delete();
	}


	// MISC
	public static boolean doesFileExist(String path)
	{
		File file = new File(path);

		return file.exists();
	}

	public static List<File> listFilesRecursively(File dir)
	{
		List<File> files = new ArrayList<File>();

		for(File file : dir.listFiles())
		{
			files.add(file);

			if(file.isDirectory())
				files.addAll(listFilesRecursively(file));
		}

		return files;
	}


	public static String getIdentifier(File baseDir, File file, String fileExtension)
	{
		String id = file.getPath().replaceFirst(baseDir.getPath(), "");
		id = id.replace('\\', '/');
		if(id.startsWith("/"))
			id = id.substring(1, id.length());

		id = TextUtil.replaceLast(id, fileExtension, "");

		return id;
	}

}
