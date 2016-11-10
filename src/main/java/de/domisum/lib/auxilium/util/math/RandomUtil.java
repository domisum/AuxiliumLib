package de.domisum.lib.auxilium.util.math;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@APIUsage
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
		return nextInt(bound, getRandom());
	}

	@APIUsage
	public static int nextInt(int bound, Random r)
	{
		return r.nextInt(bound);
	}

	@APIUsage
	public static double nextDouble(int bound)
	{
		return nextDouble(bound, getRandom());
	}

	@APIUsage
	public static double nextDouble(int bound, Random r)
	{
		return r.nextDouble();
	}

	@APIUsage
	public static boolean nextBoolean()
	{
		return nextBoolean(getRandom());
	}

	@APIUsage
	public static boolean nextBoolean(Random r)
	{
		return r.nextBoolean();
	}


	// COMPLEX
	// number
	@APIUsage
	public static double distribute(double base, double maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@APIUsage
	public static double distribute(double base, double maxDifference, Random r)
	{
		return base+((r.nextBoolean() ? 1 : -1)*r.nextDouble()*maxDifference);
	}

	@APIUsage
	public static int distribute(int base, int maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@APIUsage
	public static int distribute(int base, int maxDifference, Random r)
	{
		return (int) Math.round(distribute((double) base, maxDifference, r)); // cast to double so it picks the double method
	}

	@APIUsage
	public static int getFromRange(int min, int max)
	{
		return getFromRange(min, max, getRandom());
	}

	@APIUsage
	public static int getFromRange(int min, int max, Random r)
	{
		return min+(nextInt((max-min)+1, r));
	}


	// vector
	@APIUsage
	public static Vector3D getUnitVector()
	{
		return getUnitVector(RandomUtil.getRandom());
	}

	@APIUsage
	public static Vector3D getUnitVector(Random random)
	{
		double theta = random.nextDouble()*2*Math.PI;
		double r = (random.nextDouble()*2)-1;

		double rootComponent = Math.sqrt(1-(r*r));

		double x = rootComponent*Math.cos(theta);
		double y = rootComponent*Math.sin(theta);

		return new Vector3D(x, y, r);
	}


	// collection
	public static <E> E getElement(List<E> list)
	{
		return getElement(list, getRandom());
	}

	public static <E> E getElement(List<E> list, Random r)
	{
		int randomIndex = nextInt(list.size()-1, r);
		return list.get(randomIndex);
	}

	public static <E> E getElement(Collection<E> coll)
	{
		return getElement(coll, getRandom());
	}

	public static <E> E getElement(Collection<E> coll, Random r)
	{
		int randomIndex = nextInt(coll.size()-1, r);

		Iterator<E> iterator = coll.iterator();
		E latestElement = null;
		for(int i = 0; i <= randomIndex; i++)
			latestElement = iterator.next();

		return latestElement;
	}

}
