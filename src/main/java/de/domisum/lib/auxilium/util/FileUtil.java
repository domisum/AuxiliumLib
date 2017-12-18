package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.IOExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil
{

	// CONSTANTS
	@API public static final Charset DEFAULT_STRING_ENCODING = StandardCharsets.UTF_8;


	// READ STRING
	@API public static Optional<String> readString(File file)
	{
		return readString(file, DEFAULT_STRING_ENCODING, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> readString(File file, Charset encoding)
	{
		return readString(file, encoding, IOExceptionHandler.noAction());
	}

	@API public static Optional<String> readString(File file, IOExceptionHandler onFail)
	{
		return readString(file, DEFAULT_STRING_ENCODING, onFail);
	}

	@API public static Optional<String> readString(File file, Charset encoding, IOExceptionHandler onFail)
	{
		try
		{
			byte[] contentBytes = Files.readAllBytes(file.toPath());
			return Optional.of(new String(contentBytes, encoding));
		}
		catch(IOException e)
		{
			onFail.handle(e);
			return Optional.empty();
		}
	}


	@API public static String readStringOrException(File file)
	{
		return readStringOrException(file, DEFAULT_STRING_ENCODING);
	}

	@API public static String readStringOrException(File file, Charset encoding)
	{
		return IOExceptionHandler.getOrException(onFail->readString(file, encoding, onFail));
	}


	// WRITE STRING
	@API public static void writeString(File file, String toWrite, IOExceptionHandler onFail)
	{
		writeString(file, toWrite, DEFAULT_STRING_ENCODING, onFail);
	}

	@API public static void writeString(File file, String toWrite, Charset encoding, IOExceptionHandler onFail)
	{
		try
		{
			FileUtils.writeStringToFile(file, toWrite, encoding);
		}
		catch(IOException e)
		{
			onFail.handle(e);
		}
	}


	@API public static void writeStringOrException(File file, String toWrite)
	{
		writeStringOrException(file, toWrite, DEFAULT_STRING_ENCODING);
	}

	@API public static void writeStringOrException(File file, String toWrite, Charset encoding)
	{
		IOExceptionHandler.executeOrException(onFail->writeString(file, toWrite, encoding, onFail));
	}

}
