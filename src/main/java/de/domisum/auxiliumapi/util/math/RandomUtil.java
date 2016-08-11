package de.domisum.auxiliumapi.util.math;

import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

import java.util.Random;

public class RandomUtil
{

	// REFERENCES
	private static Random random;


	// BASIC
	@APIUsage
	public static Random getRandom()
	{
		if(random == null)
			random = new Random();

		return random;
	}

	@APIUsage
	public static int nextInt(int bound)
	{
		return getRandom().nextInt(bound);
	}

	@APIUsage
	public static double nextDouble()
	{
		return getRandom().nextDouble();
	}

	@APIUsage
	public static boolean nextBoolean()
	{
		return getRandom().nextBoolean();
	}


	// COMPLEX
	// number
	@APIUsage
	public static double distribute(double base, double maxDifference)
	{
		return distribute(getRandom(), base, maxDifference);
	}

	@APIUsage
	public static double distribute(Random r, double base, double maxDifference)
	{
		return base+((r.nextBoolean() ? 1 : -1)*r.nextDouble()*maxDifference);
	}

	@APIUsage
	public static int distribute(int base, int maxDifference)
	{
		return distribute(getRandom(), base, maxDifference);
	}

	@APIUsage
	public static int distribute(Random r, int base, int maxDifference)
	{
		return (int) Math.round(distribute(r, (double) base, maxDifference)); // cast to double so it picks the double method
	}

	@APIUsage
	public static int getFromRange(int min, int max)
	{
		return min+(nextInt((max-min)+1));
	}


	// vector
	@APIUsage
	public static Vector3D getRandomUnitVector()
	{
		return getRandomUnitVector(RandomUtil.getRandom());
	}

	@APIUsage
	public static Vector3D getRandomUnitVector(Random random)
	{
		double theta = random.nextDouble()*2*Math.PI;
		double r = (random.nextDouble()*2)-1;

		double rootComponent = Math.sqrt(1-(r*r));

		double x = rootComponent*Math.cos(theta);
		double y = rootComponent*Math.sin(theta);

		return new Vector3D(x, y, r);
	}

}
