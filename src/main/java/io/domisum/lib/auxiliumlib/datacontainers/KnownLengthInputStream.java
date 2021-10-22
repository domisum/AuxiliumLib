package io.domisum.lib.auxiliumlib.datacontainers;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@RequiredArgsConstructor
public class KnownLengthInputStream
{
	
	@Getter
	private final InputStream inputStream;
	@Getter
	private final long length;
	
	
	// INIT
	@API
	public static KnownLengthInputStream ofFile(File file)
	{
		try
		{
			var fis = new FileInputStream(file);
			var bis = new BufferedInputStream(fis);
			return new KnownLengthInputStream(bis, file.length());
		}
		catch(FileNotFoundException e)
		{
			throw new UncheckedIOException(e);
		}
	}
	
}
