package de.domisum.lib.auxilium.util.file;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.FileUtil.FileType;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectoryCopy
{

	// INPUT
	private final File sourceRootDirectory;
	private final File targetRootDirectory;

	// SETTINGS
	private final Collection<FileFilter> filters = new ArrayList<>();


	// INIT
	@API
	public static DirectoryCopy fromTo(File sourceRootDir, File targetRootDir, FileFilter... filters)
	{
		DirectoryCopy directoryCopy = new DirectoryCopy(sourceRootDir, targetRootDir);
		for(FileFilter ff : filters)
			directoryCopy.addFilter(ff);

		return directoryCopy;
	}


	// SETTINGS
	@API
	public void addFilter(FileFilter filter)
	{
		filters.add(filter);
	}


	// COPY
	@API
	public void copy()
	{
		if(!sourceRootDirectory.exists())
			return;

		copyDirectoryRecursively(sourceRootDirectory, targetRootDirectory);
	}

	private void copyDirectoryRecursively(File sourceRoot, File targetRoot)
	{
		validateCopyDirectories(sourceRoot, targetRootDirectory);
		FileUtil.mkdirs(targetRoot);

		for(File f : FileUtil.listFilesFlat(sourceRoot, FileType.FILE))
			if(shouldCopyFile(f))
				FileUtil.copyFile(f, new File(targetRoot, f.getName()));

		for(File f : FileUtil.listFilesFlat(sourceRoot, FileType.DIRECTORY))
			if(shouldCopyFile(f))
				copyDirectoryRecursively(f, new File(targetRoot, f.getName()));
	}


	// UTIL
	private static void validateCopyDirectories(File sourceRootDirectory, File targetRootDirectory)
	{
		if(sourceRootDirectory.isFile())
			throw new UncheckedIOException(new IOException(
					"can't copy directory '"+sourceRootDirectory+"', it is actually a file"));

		if(targetRootDirectory.isFile())
			throw new UncheckedIOException(new IOException(
					"can't copy into directory '"+targetRootDirectory+"', it is actually a file"));
	}

	private boolean shouldCopyFile(File file)
	{
		for(FileFilter ff : filters)
			if(ff.shouldFilterOut(file, sourceRootDirectory, targetRootDirectory))
				return false;

		return true;
	}

}
