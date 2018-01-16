package de.domisum.lib.auxilium.util;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
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


	@API public static BufferedImage copy(RenderedImage bufferedImage)
	{
		ColorModel colorModel = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(null);

		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null).getSubimage(0,
				0,
				bufferedImage.getWidth(),
				bufferedImage.getHeight());
	}


	// COLOR
	@API public static BufferedImage dye(BufferedImage image, Color color)
	{
		BufferedImage graphicsImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = graphicsImage.createGraphics();
		graphics2D.drawImage(image, 0, 0, null);
		graphics2D.setComposite(AlphaComposite.SrcAtop);
		graphics2D.setColor(color);
		graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());

		return graphicsImage;
	}

	@API public static void saturize(BufferedImage image, double saturation)
	{
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++)
				image.setRGB(x, y, processPixel(image.getRGB(x, y), (float) saturation));
	}

	private static int processPixel(int pixel, float saturation)
	{
		int red = 0xff&(pixel >> 16);
		int green = 0xff&(pixel >> 8);
		int blue = 0xff&pixel;

		float[] hsb = Color.RGBtoHSB(red, green, blue, null);
		hsb[1] += saturation;
		if(hsb[1] > 1)
			hsb[1] = 1;

		int newPixel = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		int newRed = 0xff&(newPixel >> 16);
		int newGreen = 0xff&(newPixel >> 8);
		int newBlue = 0xff&newPixel;

		if(newRed > 255)
			newRed = 255;
		if(newRed < 0)
			newRed = 0;
		if(newGreen > 255)
			newGreen = 255;
		if(newGreen < 0)
			newGreen = 0;
		if(newBlue > 255)
			newBlue = 255;
		if(newBlue < 0)
			newBlue = 0;

		return 0xff000000|(newRed<<16)|(newGreen<<8)|newBlue;
	}

}
