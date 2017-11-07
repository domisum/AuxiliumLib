package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@APIUsage
public final class CompressionUtil
{

	// INIT
	private CompressionUtil()
	{
		throw new UnsupportedOperationException();
	}


	// COMPRESSION
	@APIUsage public static byte[] compress(byte[] input, Speed compressionSpeed)
	{
		Validate.notNull(input);
		Validate.notNull(compressionSpeed);

		Deflater compressor = new Deflater();
		compressor.setLevel(compressionSpeed.deflaterLevel);
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
			throw new RuntimeException(e);
		}
	}

	@APIUsage public static byte[] decompress(byte[] input)
	{
		Validate.notNull(input);

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
			throw new RuntimeException(e);
		}
	}


	public enum Speed
	{
		// @formatter:off
		@APIUsage FAST(Deflater.BEST_SPEED),
		@APIUsage BALANCED((Deflater.BEST_COMPRESSION+Deflater.BEST_SPEED)/2),
		@APIUsage QUALITY(Deflater.BEST_COMPRESSION);
		// @formatter:on


		private final int deflaterLevel;

		// INIT
		Speed(int deflaterLevel)
		{
			this.deflaterLevel = deflaterLevel;
		}

	}

}
