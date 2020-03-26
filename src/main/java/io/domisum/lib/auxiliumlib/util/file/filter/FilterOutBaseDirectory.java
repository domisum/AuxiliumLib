package io.domisum.lib.auxiliumlib.util.file.filter;

import io.domisum.lib.auxiliumlib.util.file.FileUtil;

import java.io.File;

public class FilterOutBaseDirectory
		implements FileFilter
{
	
	// ATTRIBUTES
	private final String directoryName;
	
	
	// INIT
	public FilterOutBaseDirectory(String directoryName)
	{
		String cleanedDirectoryName = FileUtil.replaceDelimitersWithForwardSlash(directoryName);
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
