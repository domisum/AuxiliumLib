package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.ExceptionHandler;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@API
public final class HttpFetchUtil
{


	@API public static Optional<String> fetchString(String url)
	{
		return fetchString(url, ExceptionHandler.noAction());
	}

	@API public static Optional<String> fetchString(String url, ExceptionHandler exceptionHandler)
	{
		try
		{
			return Optional.of(IOUtils.toString(new URL(url), "UTF-8"));
		}
		catch(java.io.IOException e)
		{
			exceptionHandler.handle(e);
			return Optional.empty();
		}
	}


	@API public static Optional<byte[]> fetchRawData(String url)
	{
		return fetchRawData(url, ExceptionHandler.noAction());
	}

	@API public static Optional<byte[]> fetchRawData(String url, ExceptionHandler exceptionHandler)
	{
		File tempFile = null;
		try
		{
			tempFile = File.createTempFile("dl", null);

			FileUtils.copyURLToFile(new URL(url), tempFile);
			return Optional.ofNullable(OldFileUtil.readFileToByteArray(tempFile));
		}
		catch(IOException e)
		{
			exceptionHandler.handle(e);
			return Optional.empty();
		}
		finally
		{
			if(tempFile != null)
				tempFile.delete();
		}
	}

}
