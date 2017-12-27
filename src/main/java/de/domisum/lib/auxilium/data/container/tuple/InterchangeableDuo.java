package de.domisum.lib.auxilium.data.container.tuple;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Objects;

@API
public class InterchangeableDuo<T> extends Duo<T>
{

	// INIT
	public InterchangeableDuo(T a, T b)
	{
		super(a, b);
	}


	// OBJECT
	@Override public boolean equals(Object o)
	{
		if(!(o instanceof InterchangeableDuo))
			return false;

		InterchangeableDuo<?> other = (InterchangeableDuo<?>) o;

		boolean defaultOrderEquals = Objects.equals(getA(), other.getA()) && Objects.equals(getB(), other.getB());
		boolean invertedOrderEquals = Objects.equals(getA(), other.getB()) && Objects.equals(getB(), other.getA());

		return defaultOrderEquals || invertedOrderEquals;
	}

	@Override public int hashCode()
	{
		return Objects.hashCode(getA())+Objects.hashCode(getB());
	}

}