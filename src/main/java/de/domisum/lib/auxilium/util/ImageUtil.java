package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

@API
public final class ImageUtil
{

	// INIT
	private ImageUtil()
	{
		throw new UnsupportedOperationException();
	}


	// TO PIXELS
	@API public static int[][] getPixels(BufferedImage image)
	{
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int width = image.getWidth();
		int height = image.getHeight();
		boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if(hasAlphaChannel)
		{
			int pixelLength = 4;

			int row = 0;
			int col = 0;
			for(int pixel = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				//argb += ((pixels[pixel]&0xff)<<24); // alpha
				argb += pixels[pixel+1]&0xff; // blue
				argb += (pixels[pixel+2]&0xff)<<8; // green
				argb += (pixels[pixel+3]&0xff)<<16; // red
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
			int pixelLength = 3;

			int row = 0;
			int col = 0;
			for(int pixel = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				//argb += -16777216; // 255 alpha
				argb += pixels[pixel]&0xff; // blue
				argb += (pixels[pixel+1]&0xff)<<8; // green
				argb += (pixels[pixel+2]&0xff)<<16; // red
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


	// FROM PIXELS
	@API public static BufferedImage getImageFromPixels(int[] pixels, int width, int height)
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) bi.getData();
		raster.setDataElements(0, 0, width, height, pixels);
		bi.setData(raster);

		return bi;
	}

	@API public static BufferedImage getImageFromPixels(int[][] pixels)
	{
		if((pixels.length == 0) || (pixels[0].length == 0))
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


	// COLOR
	public static BufferedImage dye(BufferedImage image, Color color)
	{
		BufferedImage graphicsImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = graphicsImage.createGraphics();
		graphics2D.drawImage(image, 0, 0, null);
		graphics2D.setComposite(AlphaComposite.SrcAtop);
		graphics2D.setColor(color);
		graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

		return graphicsImage;
	}

}
