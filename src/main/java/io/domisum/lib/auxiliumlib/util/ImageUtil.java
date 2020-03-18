package io.domisum.lib.auxiliumlib.util;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageUtil
{

	@API
	public static BufferedImage copy(RenderedImage bufferedImage)
	{
		ColorModel colorModel = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(null);

		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null).getSubimage(0,
				0,
				bufferedImage.getWidth(),
				bufferedImage.getHeight()
		);
	}

	@API
	public static boolean equals(BufferedImage imageA, BufferedImage imageB)
	{
		if(imageA.getWidth() != imageB.getWidth())
			return false;

		if(imageA.getHeight() != imageB.getHeight())
			return false;

		int width = imageA.getWidth();
		int height = imageA.getHeight();

		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++)
				if(imageA.getRGB(x, y) != imageB.getRGB(x, y))
					return false;

		return true;
	}


	// FROM PIXELS
	@API
	public static BufferedImage getImageFromPixels(int[] pixels, int width, int height)
	{
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = (WritableRaster) bi.getData();
		raster.setDataElements(0, 0, width, height, pixels);
		bi.setData(raster);

		return bi;
	}

	@API
	public static BufferedImage getImageFromPixels(int[][] pixels)
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


	// RAW DATA
	@API
	public static byte[] toRaw(RenderedImage image)
	{
		return toRaw(image, "png");
	}

	@API
	public static byte[] toRaw(RenderedImage image, String formatName)
	{
		try
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, formatName, byteArrayOutputStream);

			return byteArrayOutputStream.toByteArray();
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}

	@API
	public static BufferedImage fromRaw(byte[] raw)
	{
		try
		{
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(raw);
			return ImageIO.read(byteArrayInputStream);
		}
		catch(IOException e)
		{
			throw new UncheckedIOException(e);
		}
	}


	// COLOR
	@API
	public static BufferedImage dye(BufferedImage image, Color color)
	{
		BufferedImage graphicsImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = graphicsImage.createGraphics();
		graphics2D.drawImage(image, 0, 0, null);
		graphics2D.setComposite(AlphaComposite.SrcAtop);
		graphics2D.setColor(color);
		graphics2D.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics2D.dispose();

		return graphicsImage;
	}

	@API
	public static void saturize(BufferedImage image, double saturation)
	{
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++)
				image.setRGB(x, y, saturizePixel(image.getRGB(x, y), (float) saturation));
	}

	private static int saturizePixel(int pixel, float saturation)
	{
		int red = 0xff&(pixel >> 16);
		int green = 0xff&(pixel >> 8);
		int blue = 0xff&pixel;
		int alpha = 0xff&(pixel >> 24);

		float[] hsb = Color.RGBtoHSB(red, green, blue, null);
		hsb[1] += saturation;
		if(hsb[1] > 1)
			hsb[1] = 1;

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		int argb = rgb|(alpha<<24);
		return argb;
	}


	// EFFECTS
	@API
	public static void sharpen(BufferedImage image, double sharpness)
	{
		float f = (float) sharpness;
		Kernel kernel = new Kernel(3, 3, new float[] {-1*f, -1*f, -1*f, -1*f, (8*f)+1, -1*f, -1*f, -1*f, -1*f});

		BufferedImageOp convolveOp = new ConvolveOp(kernel);
		BufferedImage imageSharpened = convolveOp.filter(image, null);

		image.createGraphics().drawImage(imageSharpened, 0, 0, null);
	}

	@API
	public static void contrastAndBrightness(BufferedImage image, double dContrast, double dBrightness)
	{
		for(int x = 0; x < image.getWidth(); x++)
			for(int y = 0; y < image.getHeight(); y++)
				image.setRGB(x, y, contrastAndBrightnessPixel(image.getRGB(x, y), dContrast, dBrightness));
	}


	private static int contrastAndBrightnessPixel(int pixel, double dContrast, double dBrightness)
	{
		int red = 0xff&(pixel >> 16);
		int green = 0xff&(pixel >> 8);
		int blue = 0xff&pixel;
		int alpha = 0xff&(pixel >> 24);

		int modifiedRed = contrastAndBrightnessChannel(red, dContrast, dBrightness);
		int modifiedGreen = contrastAndBrightnessChannel(green, dContrast, dBrightness);
		int modifiedBlue = contrastAndBrightnessChannel(blue, dContrast, dBrightness);

		int modifiedRGB = modifiedBlue|(modifiedGreen<<8)|(modifiedRed<<16)|(alpha<<24);
		return modifiedRGB;
	}

	private static int contrastAndBrightnessChannel(int baseValue, double dContrast, double dBrightness)
	{
		double channelRelative = baseValue/255d;
		double newChannelRelative = (channelRelative*(1+dContrast))+dBrightness;
		double newChannelRelativeClamped = MathUtil.clamp(0, 1, newChannelRelative);

		int newChannel = (int) Math.round(newChannelRelativeClamped*255d);
		return newChannel;
	}

}