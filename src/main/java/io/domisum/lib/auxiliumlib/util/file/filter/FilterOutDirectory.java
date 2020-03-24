package io.domisum.lib.auxiliumlib.util.file.filter;

import io.domisum.lib.auxiliumlib.util.file.FileUtil;

import java.io.File;

public class FilterOutDirectory implements FileFilter
{

	// ATTRIBUTES
	private final String directoryName;


	// INIT
	public FilterOutDirectory(String directoryName)
	{
		String cleanedDirectoryName = FileUtil.unifyDelimiters(directoryName);
		if(cleanedDirectoryName.endsWith("/"))
			cleanedDirectoryName = cleanedDirectoryName.substring(0, cleanedDirectoryName.length()-1);

		this.directoryName = cleanedDirectoryName;
	}


	// FILTER
	@Override
	public boolean shouldFilterOut(File file, File sourceRootDirectory, File targetRootDirectory)
	{
		if(!file.isDirectory())
			return false;

		String extensionPath = FileFilter.getPathExtension(sourceRootDirectory, file);
		return extensionPath.startsWith(directoryName);
	}

}
