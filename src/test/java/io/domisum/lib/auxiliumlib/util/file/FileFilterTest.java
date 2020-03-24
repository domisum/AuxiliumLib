package io.domisum.lib.auxiliumlib.util.file;

import io.domisum.lib.auxiliumlib.util.file.filter.FileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileFilterTest
{

	@Test
	void testPathExtension()
	{
		Assertions.assertEquals("files", FileFilter.getPathExtension(new File("C:/something"), new File("C:/something/files")));
	}

}
