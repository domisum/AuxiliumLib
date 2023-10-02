package io.domisum.lib.auxiliumlib.util.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.exceptions.ProgrammingError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil
{
	
	// REFERENCES
	private static Random random = null;
	
	
	// BASIC
	@API
	public static synchronized Random getRandom()
	{
		if(random == null)
			random = new Random();
		
		return random;
	}
	
	@API
	public static int nextInt(int bound)
	{
		return nextInt(bound, getRandom());
	}
	
	@API
	public static int nextInt(int bound, Random r)
	{
		return r.nextInt(bound);
	}
	
	@API
	public static long nextLong(long bound)
	{
		return nextLong(bound, getRandom());
	}
	
	@API
	public static long nextLong(long bound, Random r)
	{
		// https://stackoverflow.com/a/2546186/4755690
		
		long bits;
		long val;
		do
		{
			bits = (r.nextLong() << 1) >>> 1;
			val = bits % bound;
		}
		while(((bits - val) + (bound - 1)) < 0L);
		return val;
	}
	
	@API
	public static short nextShort()
	{
		return nextShort(getRandom());
	}
	
	@API
	public static short nextShort(Random random)
	{
		return (short) (nextInt((Short.MAX_VALUE - Short.MIN_VALUE) + 1) + Short.MIN_VALUE);
	}
	
	@API
	public static double nextDouble()
	{
		return nextDouble(getRandom());
	}
	
	@API
	public static double nextDouble(Random r)
	{
		return r.nextDouble();
	}
	
	@API
	public static boolean nextBoolean()
	{
		return nextBoolean(getRandom());
	}
	
	@API
	public static boolean nextBoolean(Random r)
	{
		return r.nextBoolean();
	}
	
	@API
	public static byte[] nextBytes(int length)
	{
		return nextBytes(length, getRandom());
	}
	
	@API
	public static byte[] nextBytes(int length, Random r)
	{
		byte[] bytes = new byte[length];
		r.nextBytes(bytes);
		
		return bytes;
	}
	
	
	// RANGE
	@API
	public static long getFromRange(long minIncl, long maxIncl)
	{
		return getFromRange(minIncl, maxIncl, getRandom());
	}
	
	@API
	public static long getFromRange(long minIncl, long maxIncl, Random r)
	{
		return minIncl + nextLong((maxIncl - minIncl) + 1, r);
	}
	
	@API
	public static int getFromRange(int minIncl, int maxIncl)
	{
		return getFromRange(minIncl, maxIncl, getRandom());
	}
	
	@API
	public static int getFromRange(int minIncl, int maxIncl, Random r)
	{
		return minIncl + nextInt((maxIncl - minIncl) + 1, r);
	}
	
	@API
	public static double getFromRange(double min, double max)
	{
		return getFromRange(min, max, getRandom());
	}
	
	@API
	public static double getFromRange(double min, double max, Random r)
	{
		return min + (nextDouble(r) * (max - min));
	}
	
	@API
	public static Duration getFromRange(Duration min, Duration max)
	{
		return getFromRange(min, max, getRandom());
	}
	
	@API
	public static Duration getFromRange(Duration min, Duration max, Random r)
	{
		return Duration.ofMillis(getFromRange(min.toMillis(), max.toMillis(), r));
	}
	
	@API
	public static Duration getFromRangeRel(Duration base, double minRel, double maxRel)
	{
		return getFromRangeRel(base, minRel, maxRel, getRandom());
	}
	
	@API
	public static Duration getFromRangeRel(Duration base, double minRel, double maxRel, Random r)
	{
		return Duration.ofMillis((long) getFromRange(base.toMillis() * minRel, base.toMillis() * maxRel, r));
	}
	
	
	// ELEMENT
	@API
	public static <E> E getElement(List<E> list)
	{
		return getElement(list, getRandom());
	}
	
	@API
	public static <E> E getElement(List<E> list, Random r)
	{
		if(list.isEmpty())
			throw new IllegalArgumentException("The list has to have at least 1 element");
		
		int randomIndex = getFromRange(0, list.size() - 1, r);
		
		return list.get(randomIndex);
	}
	
	@API
	public static <E> E getElement(Collection<E> coll)
	{
		return getElement(coll, getRandom());
	}
	
	@API
	public static <E> E getElement(Collection<E> coll, Random r)
	{
		if(coll.isEmpty())
			throw new IllegalArgumentException("The collection has to have at least 1 element");
		
		int randomIndex = getFromRange(0, coll.size() - 1, r);
		
		Iterator<E> iterator = coll.iterator();
		E latestElement = null;
		for(int i = 0; i <= randomIndex; i++)
			latestElement = iterator.next();
		
		return latestElement;
	}
	
	@API
	public static <E> E getElement(Map<E, Double> elementsWithChance)
	{
		return getElement(elementsWithChance, getRandom());
	}
	
	@API
	public static <E> E getElement(Map<E, Double> elementsWithChance, Random random)
	{
		if(elementsWithChance.isEmpty())
			throw new IllegalArgumentException("The map has to have at least 1 element");
		
		double chanceSum = elementsWithChance.values().stream().reduce(0d, Double::sum);
		double randomSumThreshold = random.nextDouble() * chanceSum;
		
		double chanceRunningSum = 0;
		for(Entry<E, Double> entry : elementsWithChance.entrySet())
		{
			chanceRunningSum += entry.getValue();
			if(chanceRunningSum > randomSumThreshold)
				return entry.getKey();
		}
		
		throw new ProgrammingError();
	}
	
	
	@API
	public static <E> E getElement(E[] array)
	{
		return getElement(array, getRandom());
	}
	
	@API
	public static <E> E getElement(E[] array, Random r)
	{
		if(array.length == 0)
			throw new IllegalArgumentException("The array has to have at least 1 element");
		
		int randomIndex = getFromRange(0, array.length - 1, r);
		return array[randomIndex];
	}
	
	
	// CHANCE
	@API
	public static boolean getByChance(double chance)
	{
		return getByChance(chance, getRandom());
	}
	
	@API
	public static boolean getByChance(double chance, Random random)
	{
		if((chance < 0) || (chance > 1))
			throw new IllegalArgumentException("The chance has to be between 0 and 1");
		
		return nextDouble(random) < chance;
	}
	
	@API
	public static boolean get50PercentChance()
	{
		return get50PercentChance(getRandom());
	}
	
	@API
	public static boolean get50PercentChance(Random random)
	{
		return nextBoolean(random);
	}
	
	
	// DISTRIBUTE
	@API
	public static double distributeAbs(double base, double maxOffsetAbs)
	{
		return distributeAbs(base, maxOffsetAbs, getRandom());
	}
	
	@API
	public static double distributeAbs(double base, double maxOffsetAbs, Random random)
	{
		int sign = get50PercentChance(random) ? 1 : -1;
		double offsetUnsigned = random.nextDouble() * maxOffsetAbs;
		return base + (sign * offsetUnsigned);
	}
	
	@API
	public static int distributeAbs(int base, int maxOffsetAbs)
	{
		return distributeAbs(base, maxOffsetAbs, getRandom());
	}
	
	@API
	public static int distributeAbs(int base, int maxOffsetAbs, Random random)
	{
		int sign = get50PercentChance(random) ? 1 : -1;
		int offsetUnsigned = nextInt(maxOffsetAbs + 1, random);
		return base + sign * offsetUnsigned;
	}
	
	@API
	public static long distributeAbs(long base, long maxOffsetAbs)
	{
		return distributeAbs(base, maxOffsetAbs, getRandom());
	}
	
	@API
	public static long distributeAbs(long base, long maxOffsetAbs, Random random)
	{
		long sign = get50PercentChance(random) ? 1 : -1;
		long offsetUnsigned = nextLong(maxOffsetAbs + 1, random);
		return base + sign * offsetUnsigned;
	}
	
	@API
	public static Duration distributeRel(Duration base, double maxOffsetRel)
	{
		return distributeRel(base, maxOffsetRel, getRandom());
	}
	
	@API
	public static Duration distributeRel(Duration base, double maxOffsetRel, Random random)
	{
		Validate.isTrue(maxOffsetRel > 0, "maxOffsetRel has to be greater than zero, but was " + maxOffsetRel);
		
		long baseMillis = base.toMillis();
		double factor = getFromRange(1 - maxOffsetRel, 1 + maxOffsetRel, random);
		
		long derivedMillis = Math.round(baseMillis * factor);
		return Duration.ofMillis(derivedMillis);
	}
	
}
