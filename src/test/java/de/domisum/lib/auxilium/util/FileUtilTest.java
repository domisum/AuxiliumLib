package de.domisum.lib.auxilium.util;

import com.google.common.io.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtilTest
{

	private List<File> filesToTearDown = new ArrayList<>();


	@AfterEach public void tearDown()
	{
		for(File f : filesToTearDown)
			if(f.isFile())
				FileUtil.deleteFileOrDirectory(f);
			else
				FileUtil.deleteDirectory(f);
	}


	// READ STRING
	@Test public void testSimpleReadAndWrite()
	{
		String text = "Hello world";
		File tempFile = createTempFile();

		writeReadAssertEquals(text, tempFile);
	}


	// WRITE STRING
	@Test public void testWriteInNotExistingFolder()
	{
		String text = "meme\nasdf";
		File tempDir = createTempDirectory();
		File deeperInTempDir = new File(tempDir, "folder1/folder2");

		writeReadAssertEquals(text, deeperInTempDir);
	}


	// DELETE DIR
	@Test public void testDeleteNestedDir()
	{
		String sampleText = "asdf\nwow";

		File tempDir = createTempDirectory();

		File textFileInDir = new File(tempDir, "test.txt");
		FileUtil.writeStringOrException(textFileInDir, sampleText);
		File fileDeeper = new File(tempDir, "dir/test2.txt");
		FileUtil.writeStringOrException(fileDeeper, sampleText);


		FileUtil.deleteDirectory(tempDir);
		Assertions.assertTrue(!tempDir.exists(), "directory wasn't deleted");
	}


	// ARRANGE
	private File createTempFile()
	{
		try
		{
			File tempFile = File.createTempFile("test", "test");
			filesToTearDown.add(tempFile);
			return tempFile;
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	private File createTempDirectory()
	{
		File tempDir = Files.createTempDir();
		filesToTearDown.add(tempDir);
		return tempDir;
	}


	// ACT ASSERT
	private void writeReadAssertEquals(String text, File file)
	{
		FileUtil.writeStringOrException(file, text);
		String read = FileUtil.readStringOrException(file);

		Assertions.assertEquals(text, read, "text was different after write/read");
	}

}
