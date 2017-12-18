package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

@API
public class FileUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;


	// READ STRING
	@API public static Optional<String> readString(File file)
	{
		return readString(file, DEFAULT_ENCODING, ExceptionHandler.noAction());
	}

	@API public static Optional<String> readString(File file, Charset encoding)
	{
		return readString(file, encoding, ExceptionHandler.noAction());
	}

	@API public static Optional<String> readString(File file, ExceptionHandler exceptionHandler)
	{
		return readString(file, DEFAULT_ENCODING, exceptionHandler);
	}

	@API public static Optional<String> readString(File file, Charset encoding, ExceptionHandler exceptionHandler)
	{
		try
		{
			byte[] contentBytes = Files.readAllBytes(file.toPath());
			return Optional.of(new String(contentBytes, encoding));
		}
		catch(IOException e)
		{
			exceptionHandler.handle(e);
			return Optional.empty();
		}
	}


	// WRITE STRING
	@API public static void writeString(File file, String toWrite)
	{
		writeString(file, toWrite, DEFAULT_ENCODING, ExceptionHandler.noAction());
	}

	@API public static void writeString(File file, String toWrite, Charset encoding)
	{
		writeString(file, toWrite, encoding, ExceptionHandler.noAction());
	}

	@API public static void writeString(File file, String toWrite, ExceptionHandler exceptionHandler)
	{
		writeString(file, toWrite, DEFAULT_ENCODING, exceptionHandler);
	}

	@API public static void writeString(File file, String toWrite, Charset encoding, ExceptionHandler exceptionHandler)
	{
		try
		{
			FileUtils.writeStringToFile(file, toWrite, encoding);
		}
		catch(IOException e)
		{
			exceptionHandler.handle(e);
		}
	}

}
