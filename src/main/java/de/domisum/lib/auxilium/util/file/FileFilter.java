package de.domisum.lib.auxilium.util.file;

import java.io.File;

public interface FileFilter
{

	boolean shouldFilterOut(File file, File sourceRootDirectory, File targetRootDirectory);

}
