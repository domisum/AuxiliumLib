package de.domisum.auxiliumapi.util.math;

import java.util.Random;

import de.domisum.auxiliumapi.data.container.math.Vector3D;

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
	// number
	public static double distribute(double base, double maxDifference)
	{
		return distribute(getRandom(), base, maxDifference);
	}

	public static double distribute(Random r, double base, double maxDifference)
	{
		return base + ((r.nextBoolean() ? 1 : -1) * r.nextDouble() * maxDifference);
	}


	public static int distribute(int base, int maxDifference)
	{
		return distribute(getRandom(), base, maxDifference);
	}

	public static int distribute(Random r, int base, int maxDifference)
	{
		return (int) Math.round(distribute(r, (double) base, maxDifference)); // cast to double so it picks the double method
	}


	public static int getFromRange(int min, int max)
	{
		return min + (nextInt((max - min) + 1));
	}


	// vector
	public static Vector3D getRandomUnitVector()
	{
		return getRandomUnitVector(RandomUtil.getRandom());
	}

	public static Vector3D getRandomUnitVector(Random random)
	{
		double theta = random.nextDouble() * 2 * Math.PI;
		double r = (random.nextDouble() * 2) - 1;

		double rootComponent = Math.sqrt(1 - (r * r));

		double x = rootComponent * Math.cos(theta);
		double y = rootComponent * Math.sin(theta);
		double z = r;

		return new Vector3D(x, y, z);
	}

}
