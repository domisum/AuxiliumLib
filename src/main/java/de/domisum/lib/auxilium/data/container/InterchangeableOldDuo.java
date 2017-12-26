package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Objects;

@API
public class InterchangeableOldDuo<T, U> extends OldDuo<T, U>
{

	// INIT
	public InterchangeableOldDuo(T a, U b)
	{
		super(a, b);
	}


	// OBJECT
	@Override public boolean equals(Object o)
	{
		if(!(o instanceof InterchangeableOldDuo))
			return false;

		InterchangeableOldDuo<?, ?> other = (InterchangeableOldDuo<?, ?>) o;

		boolean defaultOrderEquals = Objects.equals(a, other.a) && Objects.equals(b, other.b);
		boolean invertedOrderEquals = Objects.equals(a, other.b) && Objects.equals(b, other.a);

		return defaultOrderEquals || invertedOrderEquals;
	}

	@Override public int hashCode()
	{
		return Objects.hashCode(a)+Objects.hashCode(b);
	}

}