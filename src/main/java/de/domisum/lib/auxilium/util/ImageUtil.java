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

	@APIUsage public static BufferedImage loadImage(String path)
	{
		return loadImage(new File(path));
	}

	@APIUsage public static BufferedImage loadImage(File file)
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


	@APIUsage public static void writeImage(File file, BufferedImage image)
	{
		File parent = file.getAbsoluteFile().getParentFile();
		if(!parent.exists())
			parent.mkdirs();

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


	@APIUsage public static int[][] getPixels(BufferedImage image)
	{
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if(hasAlphaChannel)
		{
			final int pixelLength = 4;

			int row = 0;
			int col = 0;
			for(int pixel = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				//argb += ((pixels[pixel]&0xff)<<24); // alpha
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

			int row = 0;
			int col = 0;
			for(int pixel = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				//argb += -16777216; // 255 alpha
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

	@APIUsage public static BufferedImage getImageFromPixels(int[] pixels, int width, int height)
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) bi.getData();
		raster.setDataElements(0, 0, width, height, pixels);
		bi.setData(raster);

		return bi;
	}

	public static BufferedImage getImageFromPixels(int[][] pixels)
	{
		// TODO test this code, idk if the dimensions are proper

		if(pixels.length == 0 || pixels[0].length == 0)
			throw new IllegalArgumentException("The array has to have at least a length of 1 in each direction");

		int height = pixels.length;
		int width = pixels[0].length;

		int[] linearPixels = new int[width*height];
		for(int i = 0; i < linearPixels.length; i++)
		{
			int column = i%width;
			int row = i/width;

			linearPixels[i] = pixels[row][column];
		}

		return getImageFromPixels(linearPixels, width, height);
	}

}
