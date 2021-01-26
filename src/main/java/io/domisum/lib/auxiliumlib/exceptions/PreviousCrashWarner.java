package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.util.FileUtil;
import io.domisum.lib.auxiliumlib.util.FileUtil.FileType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PreviousCrashWarner
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviousCrashWarner.class);
	
	
	public static void warnAboutPreviousCrashes()
	{
		var baseDir = new File("dummy.txt").getAbsoluteFile().getParentFile();
		var files = FileUtil.listFilesFlat(baseDir, FileType.FILE);
		
		for(var file : files)
		{
			String extension = FileUtil.getExtension(file);
			if("hprof".equalsIgnoreCase(extension))
				LOGGER.error("Found HPROF file in run directory, indicating previous crash from OomError");
		}
	}
	
}
