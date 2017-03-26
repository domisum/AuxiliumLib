package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtil
{

	@APIUsage public static byte[] compress(byte[] input, boolean strongCompression)
	{
		Deflater compressor = new Deflater();
		compressor.setLevel(strongCompression ? Deflater.BEST_COMPRESSION : Deflater.BEST_SPEED);
		compressor.setInput(input);
		compressor.finish();

		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length))
		{
			byte[] buffer = new byte[1024];

			while(!compressor.finished())
				bos.write(buffer, 0, compressor.deflate(buffer));

			bos.close();
			return bos.toByteArray();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@APIUsage public static byte[] decompress(byte[] input)
	{
		Inflater decompressor = new Inflater();
		decompressor.setInput(input);

		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length))
		{
			byte[] buffer = new byte[1024];
			while(!decompressor.finished())
				bos.write(buffer, 0, decompressor.inflate(buffer));

			bos.close();
			return bos.toByteArray();
		}
		catch(IOException|DataFormatException e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
