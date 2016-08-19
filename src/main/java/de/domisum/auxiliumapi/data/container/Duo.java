package de.domisum.auxiliumapi.data.container;


import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

public class Duo<T, U>
{

	// VALUES
	public final T a;
	public final U b;


	// -------
	// CONSTRUCTOR
	// -------
	public Duo(T a, U b)
	{
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Duo))
			return false;

		Duo<?, ?> other = (Duo<?, ?>) o;

		boolean aEquals = this.a != null ? this.a.equals(other.a) : other.a == null;
		boolean bEquals = this.b != null ? this.b.equals(other.b) : other.b == null;

		return aEquals && bEquals;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 13;

		hashCode = hashCode*37+(this.a != null ? this.a.hashCode() : 0);
		hashCode = hashCode*37+(this.b != null ? this.b.hashCode() : 0);

		return hashCode;
	}

	@Override
	public String toString()
	{
		String aString = this.a != null ? this.a.toString() : "null";
		String bString = this.b != null ? this.b.toString() : "null";

		return "Duo[a='"+aString+"',b='"+bString+"']";
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Duo<U, T> getInverted()
	{
		return new Duo<>(this.b, this.a);
	}

}