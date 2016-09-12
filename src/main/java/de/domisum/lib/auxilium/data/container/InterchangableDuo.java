package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

@APIUsage
public class InterchangableDuo<T, U> extends Duo<T, U>
{

	// -------
	// CONSTRUCTOR
	// -------
	public InterchangableDuo(T a, U b)
	{
		super(a, b);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof InterchangableDuo))
			return false;

		InterchangableDuo<?, ?> other = (InterchangableDuo<?, ?>) o;

		boolean aEquals = this.a != null ? this.a.equals(other.a) : other.a == null;
		boolean bEquals = this.b != null ? this.b.equals(other.b) : other.b == null;
		boolean rightOrderEquals = aEquals && bEquals;

		boolean aEqualsB = this.a != null ? this.a.equals(other.b) : other.b == null;
		boolean bEqualsA = this.b != null ? this.b.equals(other.a) : other.a == null;
		boolean invertedOrderEquals = aEqualsB && bEqualsA;

		return rightOrderEquals || invertedOrderEquals;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 13;

		hashCode += this.a != null ? this.a.hashCode() : 0;
		hashCode += this.b != null ? this.b.hashCode() : 0;

		return hashCode;
	}

}