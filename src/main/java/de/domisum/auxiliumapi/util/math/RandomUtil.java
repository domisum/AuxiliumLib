package de.domisum.auxiliumapi.util.math;

import java.util.Random;

public class RandomUtil
{

	// REFERENCES
	private static Random random;


	// BASIC
	public static Random getRandom()
	{
		if(random == null)
			random = new Random();

		return random;
	}

	public static int nextInt(int bound)
	{
		return getRandom().nextInt(bound);
	}

	public static double nextDouble()
	{
		return getRandom().nextDouble();
	}

	public static boolean nextBoolean()
	{
		return getRandom().nextBoolean();
	}


	// COMPLEX
	public static double distribute(double base, double maxDifference)
	{
		return base + ((nextBoolean() ? 1 : -1) * nextDouble() * maxDifference);
	}

	public static int distribute(int base, int maxDifference)
	{
		return (int) Math.round(distribute(base + 0.0d, maxDifference)); // + 0.0d so it picks the double method
	}


	public static int getFromRange(int min, int max)
	{
		return min + (nextInt((max - min) + 1));
	}

}
