package de.domisum.lib.auxilium.util.file.filter;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.file.FileFilter;

import java.io.File;

public class FilterOutDirectory implements FileFilter
{

	// ATTRIBUTES
	private final String directoryName;


	// INIT
	public FilterOutDirectory(String directoryName)
	{
		String cleanedDirectoryName = FileUtil.replaceDelimiters(directoryName);
		if(!cleanedDirectoryName.endsWith("/"))
			cleanedDirectoryName += "/";

		this.directoryName = cleanedDirectoryName;
	}


	// FILTER
	@Override public boolean shouldFilterOut(File file, File sourceRootDirectory, File targetRootDirectory)
	{
		if(!file.isDirectory())
			return false;

		String extension = FileFilter.getPathExtension(sourceRootDirectory, file);
		return extension.startsWith(directoryName);
	}

}
