package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.math.RandomUtil;

public class AlwaysUnequalOldDuo<T, U> extends OldDuo<T, U>
{

	private int hashCode;


	// INIT
	public AlwaysUnequalOldDuo(T a, U b)
	{
		super(a, b);

		hashCode = RandomUtil.nextInt(Integer.MAX_VALUE);
	}


	// OBJECT
	@Override public boolean equals(Object o)
	{
		return this == o;
	}

	@Override public int hashCode()
	{
		return hashCode;
	}

}