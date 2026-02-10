package io.domisum.lib.auxiliumlib.exceptions;

import io.domisum.lib.auxiliumlib.time.TimeUtil;
import io.domisum.lib.auxiliumlib.util.FileUtil;
import io.domisum.lib.auxiliumlib.util.FileUtil.FileType;
import io.domisum.lib.auxiliumlib.util.LoggerUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.File;
import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PreviousCrashWarner
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PreviousCrashWarner.class);
	
	
	// INTERFACE
	public static void warnAboutPreviousCrashes()
	{
		var baseDir = new File("dummy.txt").getAbsoluteFile().getParentFile();
		var files = FileUtil.listFilesFlat(baseDir, FileType.FILE);
		for(var file : files)
		{
			String extension = FileUtil.getExtension(file);
			if("hprof".equalsIgnoreCase(extension))
				handleHprofFile(file);
		}
	}
	
	
	// INTERNAL
	private static void handleHprofFile(File file)
	{
		var lastModified = FileUtil.getLastModified(file);
		
		if(TimeUtil.isOlderThan(lastModified, Duration.ofDays(14)))
		{
			LOGGER.warn("Deleting old .hprof file in run directory: {}", file.getName());
			FileUtil.delete(file);
			return;
		}
		
		var level = TimeUtil.isOlderThan(lastModified, Duration.ofDays(1)) ? Level.WARN : Level.ERROR;
		LoggerUtil.log(LOGGER, level, "Found .hprof file in run directory, OomError");
	}
	
}
