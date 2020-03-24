package io.domisum.lib.auxiliumlib.util.file;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.file.FileUtil.FileType;
import io.domisum.lib.auxiliumlib.util.file.filter.FileFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectoryCopy
{

	// INPUT
	private final File sourceRootDirectory;
	private final File targetRootDirectory;

	// SETTINGS
	private final Collection<FileFilter> filters;


	// INIT
	@API
	public static DirectoryCopy fromTo(File sourceRootDir, File targetRootDir, FileFilter... filters)
	{
		return new DirectoryCopy(sourceRootDir, targetRootDir, Arrays.asList(filters));
	}


	// COPY
	@API
	public void copy()
	{
		if(!sourceRootDirectory.exists())
			return;

		validateInputDirectories(sourceRootDirectory, targetRootDirectory);
		copyDirectoryRecursively(sourceRootDirectory, targetRootDirectory);
	}

	private void copyDirectoryRecursively(File sourceRoot, File targetRoot)
	{
		FileUtil.mkdirs(targetRoot);

		for(var file : FileUtil.listFilesFlat(sourceRoot, FileType.FILE))
			if(shouldCopyFile(file))
				FileUtil.copyFile(file, new File(targetRoot, file.getName()));

		for(var file : FileUtil.listFilesFlat(sourceRoot, FileType.DIRECTORY))
			if(shouldCopyFile(file))
				copyDirectoryRecursively(file, new File(targetRoot, file.getName()));
	}


	// UTIL
	private static void validateInputDirectories(File sourceRootDirectory, File targetRootDirectory)
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
		for(var fileFilter : filters)
			if(fileFilter.shouldFilterOut(file, sourceRootDirectory, targetRootDirectory))
				return false;

		return true;
	}

}
