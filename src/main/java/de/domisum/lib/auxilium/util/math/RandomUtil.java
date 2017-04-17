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


	// basic
	@APIUsage public static Random getRandom()
	{
		if(random == null)
			random = new Random();

		return random;
	}

	@APIUsage public static int nextInt(int bound)
	{
		return nextInt(bound, getRandom());
	}

	@APIUsage public static int nextInt(int bound, Random r)
	{
		return r.nextInt(bound);
	}

	@APIUsage public static double nextDouble()
	{
		return nextDouble(getRandom());
	}

	@APIUsage public static double nextDouble(Random r)
	{
		return r.nextDouble();
	}

	@APIUsage public static boolean nextBoolean()
	{
		return nextBoolean(getRandom());
	}

	@APIUsage public static boolean nextBoolean(Random r)
	{
		return r.nextBoolean();
	}


	// number
	@APIUsage public static double distribute(double base, double maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@APIUsage public static double distribute(double base, double maxDifference, Random r)
	{
		return base+((r.nextBoolean() ? 1 : -1)*r.nextDouble()*maxDifference);
	}

	@APIUsage public static int distribute(int base, int maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@APIUsage public static int distribute(int base, int maxDifference, Random r)
	{
		return (int) Math.round(distribute((double) base, maxDifference, r)); // cast to double so it picks the double method
	}

	@APIUsage public static int getFromRange(int min, int max)
	{
		return getFromRange(min, max, getRandom());
	}

	@APIUsage public static int getFromRange(int min, int max, Random r)
	{
		return min+(nextInt((max-min)+1, r));
	}

	@APIUsage public static double getFromRange(double min, double max)
	{
		return getFromRange(min, max, getRandom());
	}

	@APIUsage public static double getFromRange(double min, double max, Random r)
	{
		return min+nextDouble(r)*(max-min);
	}


	// vector
	@APIUsage public static Vector3D getUnitVector()
	{
		return getUnitVector(RandomUtil.getRandom());
	}

	@APIUsage public static Vector3D getUnitVector(Random random)
	{
		double theta = random.nextDouble()*2*Math.PI;
		double r = (random.nextDouble()*2)-1;

		double rootComponent = Math.sqrt(1-(r*r));

		double x = rootComponent*Math.cos(theta);
		double y = rootComponent*Math.sin(theta);

		return new Vector3D(x, y, r);
	}


	// random element
	@APIUsage public static <E> E getElement(List<E> list)
	{
		return getElement(list, getRandom());
	}

	@APIUsage public static <E> E getElement(List<E> list, Random r)
	{
		if(list.size() == 0)
			throw new IllegalArgumentException("The list has to have at least 1 element");

		int randomIndex = getFromRange(0, list.size()-1, r);

		return list.get(randomIndex);
	}

	@APIUsage public static <E> E getElement(Collection<E> coll)
	{
		return getElement(coll, getRandom());
	}

	@APIUsage public static <E> E getElement(Collection<E> coll, Random r)
	{
		if(coll.size() == 0)
			throw new IllegalArgumentException("The collection has to have at least 1 element");

		int randomIndex = getFromRange(0, coll.size()-1, r);

		Iterator<E> iterator = coll.iterator();
		E latestElement = null;
		for(int i = 0; i <= randomIndex; i++)
			latestElement = iterator.next();

		return latestElement;
	}


	@APIUsage public static <E> E getElement(E[] array)
	{
		return getElement(array, getRandom());
	}

	@APIUsage public static <E> E getElement(E[] array, Random r)
	{
		if(array.length == 0)
			throw new IllegalArgumentException("The array has to have at least 1 element");

		int randomIndex = getFromRange(0, array.length-1, r);
		return array[randomIndex];
	}

}
