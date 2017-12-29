package de.domisum.lib.auxilium.util.file;

import de.domisum.lib.auxilium.util.PHR;

import java.io.File;

public interface FileFilter
{

	boolean shouldFilterOut(File file, File sourceRootDirectory, File targetRootDirectory);


	static String getPathExtension(File base, File sub)
	{
		String basePath = base.getAbsoluteFile().getAbsolutePath();
		String subPath = sub.getAbsoluteFile().getPath();

		if(!subPath.startsWith(basePath))
			throw new IllegalArgumentException(PHR.r("sub path '{}' doesn't start with base path '{}'", basePath, subPath));

		String extension = subPath.substring(basePath.length());
		if(extension.startsWith("/") || extension.startsWith("\\"))
			return extension.substring(1);

		return extension;
	}

}
