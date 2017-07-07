package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.Objects;

@APIUsage
public class InterchangeableDuo<T, U> extends Duo<T, U>
{

	// CONSTRUCTOR
	public InterchangeableDuo(T a, U b)
	{
		super(a, b);
	}

	@Override public boolean equals(Object o)
	{
		if(!(o instanceof InterchangeableDuo))
			return false;

		InterchangeableDuo<?, ?> other = (InterchangeableDuo<?, ?>) o;

		boolean defaultOrderEquals = Objects.equals(this.a, other.a) && Objects.equals(this.b, other.b);
		boolean invertedOrderEquals = Objects.equals(this.a, other.b) && Objects.equals(this.b, other.a);

		return defaultOrderEquals || invertedOrderEquals;
	}

	@Override public int hashCode()
	{
		// not using default hashCode practices because
		// two InterchangeableDuos with inverted a and b should give the same hashCode

		return Objects.hashCode(this.a)+Objects.hashCode(this.b);
	}

}