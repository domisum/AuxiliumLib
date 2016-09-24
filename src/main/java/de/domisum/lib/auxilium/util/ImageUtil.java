package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

@APIUsage
public class ImageUtil
{

	@APIUsage
	public static BufferedImage loadImage(String path)
	{
		return loadImage(new File(path));
	}

	@APIUsage
	public static BufferedImage loadImage(File file)
	{
		try
		{
			return ImageIO.read(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}


	@APIUsage
	public static void writeImage(File file, BufferedImage image)
	{
		if(image == null)
			throw new IllegalArgumentException("The image can't be null");

		try
		{
			ImageIO.write(image, "png", file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	@APIUsage
	public static int[][] getPixels(BufferedImage image)
	{
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if(hasAlphaChannel)
		{
			final int pixelLength = 4;
			for(int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += ((pixels[pixel]&0xff)<<24); // alpha
				argb += (pixels[pixel+1]&0xff); // blue
				argb += ((pixels[pixel+2]&0xff)<<8); // green
				argb += ((pixels[pixel+3]&0xff)<<16); // red
				result[row][col] = argb;
				col++;

				if(col == width)
				{
					col = 0;
					row++;
				}
			}
		}
		else
		{
			final int pixelLength = 3;
			for(int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += (pixels[pixel]&0xff); // blue
				argb += ((pixels[pixel+1]&0xff)<<8); // green
				argb += ((pixels[pixel+2]&0xff)<<16); // red
				result[row][col] = argb;
				col++;

				if(col == width)
				{
					col = 0;
					row++;
				}
			}
		}

		return result;
	}

	@APIUsage
	public static BufferedImage getImageFromPixels(int[] pixels, int width, int height)
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) bi.getData();
		raster.setDataElements(0, 0, width, height, pixels);
		bi.setData(raster);

		return bi;
	}

}