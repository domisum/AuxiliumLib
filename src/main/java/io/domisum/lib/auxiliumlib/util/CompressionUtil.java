package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.ShouldNeverHappenError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompressionUtil
{
	
	// CONSTANTS
	private static final int BUFFER_SIZE = 1024;
	
	
	// COMPRESSION
	@API
	public static byte[] compress(byte[] input, Speed compressionSpeed)
	{
		Validate.notNull(input);
		Validate.notNull(compressionSpeed);
		
		
		Deflater compressor = new Deflater();
		compressor.setLevel(compressionSpeed.deflaterLevel);
		compressor.setInput(input);
		compressor.finish();
		
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length))
		{
			byte[] buffer = new byte[BUFFER_SIZE];
			
			while(!compressor.finished())
				bos.write(buffer, 0, compressor.deflate(buffer));
			
			return bos.toByteArray();
		}
		catch(IOException e)
		{
			throw new ShouldNeverHappenError(e);
		}
	}
	
	@API
	public static byte[] decompress(byte[] input)
	{
		Validate.notNull(input);
		
		
		Inflater decompressor = new Inflater();
		decompressor.setInput(input);
		
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length))
		{
			byte[] buffer = new byte[BUFFER_SIZE];
			while(!decompressor.finished())
				bos.write(buffer, 0, decompressor.inflate(buffer));
			
			bos.close();
			return bos.toByteArray();
		}
		catch(DataFormatException e)
		{
			throw new IllegalArgumentException("Invalid data provided:", e);
		}
		catch(IOException e)
		{
			throw new ShouldNeverHappenError(e);
		}
	}
	
	
	public enum Speed
	{
		
		@API FAST(Deflater.BEST_SPEED),
		@API BALANCED((Deflater.BEST_COMPRESSION+Deflater.BEST_SPEED)/2),
		@API QUALITY(Deflater.BEST_COMPRESSION);
		
		
		private final int deflaterLevel;
		
		
		// INIT
		Speed(int deflaterLevel)
		{
			this.deflaterLevel = deflaterLevel;
		}
		
	}
	
}
