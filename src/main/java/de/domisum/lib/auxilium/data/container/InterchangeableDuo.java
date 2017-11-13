package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Objects;

@API
public class InterchangeableDuo<T, U> extends Duo<T, U>
{

	// INIT
	public InterchangeableDuo(T a, U b)
	{
		super(a, b);
	}


	// OBJECT
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
		return Objects.hashCode(this.a)+Objects.hashCode(this.b);
	}

}