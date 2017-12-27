package de.domisum.lib.auxilium.util.math;

import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil
{

	// REFERENCES
	private static Random random = null;


	// basic
	@API public static synchronized Random getRandom()
	{
		if(random == null)
			random = new Random();

		return random;
	}

	@API public static int nextInt(int bound)
	{
		return nextInt(bound, getRandom());
	}

	@API public static int nextInt(int bound, Random r)
	{
		return r.nextInt(bound);
	}

	@API public static double nextDouble()
	{
		return nextDouble(getRandom());
	}

	@API public static double nextDouble(Random r)
	{
		return r.nextDouble();
	}

	@API public static boolean nextBoolean()
	{
		return nextBoolean(getRandom());
	}

	@API public static boolean nextBoolean(Random r)
	{
		return r.nextBoolean();
	}


	// number
	@API public static double distribute(double base, double maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@API public static double distribute(double base, double maxDifference, Random r)
	{
		return base+((r.nextBoolean() ? 1 : -1)*r.nextDouble()*maxDifference);
	}

	@API public static int distribute(int base, int maxDifference)
	{
		return distribute(base, maxDifference, getRandom());
	}

	@API public static int distribute(int base, int maxDifference, Random r)
	{
		return (int) Math.round(distribute((double) base, maxDifference, r)); // cast to double so it picks the double method
	}

	@API public static int getFromRange(int min, int max)
	{
		return getFromRange(min, max, getRandom());
	}

	@API public static int getFromRange(int min, int max, Random r)
	{
		return min+nextInt((max-min)+1, r);
	}

	@API public static double getFromRange(double min, double max)
	{
		return getFromRange(min, max, getRandom());
	}

	@API public static double getFromRange(double min, double max, Random r)
	{
		return min+(nextDouble(r)*(max-min));
	}


	// vector
	@API public static Vector3D getUnitVector3D()
	{
		return getUnitVector3D(getRandom());
	}

	@API public static Vector3D getUnitVector3D(Random random)
	{
		double theta = random.nextDouble()*2*Math.PI;
		double r = (random.nextDouble()*2)-1;

		double rootComponent = Math.sqrt(1-(r*r));

		double x = rootComponent*Math.cos(theta);
		double y = rootComponent*Math.sin(theta);

		return new Vector3D(x, y, r);
	}

	@API public static Vector2D getUnitVector2D()
	{
		return getUnitVector2D(getRandom());
	}

	@API public static Vector2D getUnitVector2D(Random random)
	{
		double angleRad = getFromRange(0, 2*Math.PI, random);
		double x = Math.cos(angleRad);
		double y = Math.sin(angleRad);

		return new Vector2D(x, y);
	}


	// random element
	@API public static <E> E getElement(List<E> list)
	{
		return getElement(list, getRandom());
	}

	@API public static <E> E getElement(List<E> list, Random r)
	{
		if(list.isEmpty())
			throw new IllegalArgumentException("The list has to have at least 1 element");

		int randomIndex = getFromRange(0, list.size()-1, r);

		return list.get(randomIndex);
	}

	@API public static <E> E getElement(Collection<E> coll)
	{
		return getElement(coll, getRandom());
	}

	@API public static <E> E getElement(Collection<E> coll, Random r)
	{
		if(coll.isEmpty())
			throw new IllegalArgumentException("The collection has to have at least 1 element");

		int randomIndex = getFromRange(0, coll.size()-1, r);

		Iterator<E> iterator = coll.iterator();
		E latestElement = null;
		for(int i = 0; i <= randomIndex; i++)
			latestElement = iterator.next();

		return latestElement;
	}


	@API public static <E> E getElement(E[] array)
	{
		return getElement(array, getRandom());
	}

	@API public static <E> E getElement(E[] array, Random r)
	{
		if(array.length == 0)
			throw new IllegalArgumentException("The array has to have at least 1 element");

		int randomIndex = getFromRange(0, array.length-1, r);
		return array[randomIndex];
	}


	// chance
	@API public static boolean getByChance(double chance)
	{
		return getByChance(chance, getRandom());
	}

	@API public static boolean getByChance(double chance, Random random)
	{
		if((chance < 0) || (chance > 1))
			throw new IllegalArgumentException("The chance has to be between 0 and 1");

		return nextDouble(random) < chance;
	}

}
