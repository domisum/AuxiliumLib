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
				FileUtil.deleteFile(f);
			else
				FileUtil.deleteDirectory(f);
	}


	// STRING
	@Test public void testSimpleReadAndWrite()
	{
		String text = "Hello world";
		File tempFile = createTempFile();

		writeReadAssertEquals(tempFile, text);
	}

	@Test public void testWriteInNotExistingFolder()
	{
		String text = "meme\nasdf";
		File tempDir = createTempDirectory();
		File deeperInTempDir = new File(tempDir, "folder1/folder2");

		writeReadAssertEquals(deeperInTempDir, text);
	}


	// RAW
	@Test public void testSimpleWriteReadRaw()
	{
		byte[] testData = new byte[] {0, 8, -3, 127};
		File tempFile = createTempFile();

		writeReadAssertEquals(tempFile, testData);
	}

	@Test public void testOverwriteRaw()
	{
		byte[] testData = new byte[] {-1, -29, 88, 18};
		byte[] testData2 = new byte[] {0, 8, -3, 127};
		File tempFile = createTempFile();

		FileUtil.writeRaw(tempFile, testData);
		writeReadAssertEquals(tempFile, testData2);
	}


	// DELETE DIR
	@Test public void testDeleteNestedDir()
	{
		String sampleText = "asdf\nwow";

		File tempDir = createTempDirectory();

		File textFileInDir = new File(tempDir, "test.txt");
		FileUtil.writeString(textFileInDir, sampleText);
		File fileDeeper = new File(tempDir, "dir/test2.txt");
		FileUtil.writeString(fileDeeper, sampleText);


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
	private void writeReadAssertEquals(File file, String text)
	{
		FileUtil.writeString(file, text);
		String read = FileUtil.readString(file);

		Assertions.assertEquals(text, read, "text was different after write/read");
	}

	private void writeReadAssertEquals(File file, byte[] raw)
	{
		FileUtil.writeRaw(file, raw);
		byte[] read = FileUtil.readRaw(file);

		Assertions.assertArrayEquals(raw, read, "raw data was different after write/read");
	}

}
