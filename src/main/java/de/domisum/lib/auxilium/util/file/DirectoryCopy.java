package de.domisum.lib.auxilium.util.file;

import de.domisum.lib.auxilium.util.FileUtil;
import de.domisum.lib.auxilium.util.FileUtil.FileType;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DirectoryCopy
{

	// INPUT
	private final File sourceRootDirectory;
	private final File targetRootDirectory;


	// INIT
	@API public static DirectoryCopy fromTo(File sourceRootDir, File targetRootDir)
	{
		DirectoryCopy directoryCopy = new DirectoryCopy(sourceRootDir, targetRootDir);
		return directoryCopy;
	}


	// COPY
	@API public void copy()
	{
		if(!sourceRootDirectory.exists())
			return;

		copyDirectoryRecursively(sourceRootDirectory, targetRootDirectory);
	}

	private void copyDirectoryRecursively(File sourceRoot, File targetRoot)
	{
		validateCopyDirectories(sourceRoot, targetRootDirectory);
		targetRoot.mkdirs();

		for(File f : FileUtil.listFiles(sourceRoot, FileType.FILE))
			FileUtil.copyFile(f, new File(targetRoot, f.getName()));

		for(File f : FileUtil.listFiles(sourceRoot, FileType.DIRECTORY))
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

}
