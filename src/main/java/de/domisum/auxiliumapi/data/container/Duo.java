package de.domisum.auxiliumapi.data.container;


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

		boolean aEquals = this.a != null ? this.a.equals(other.a) : this.a == other.a;
		boolean bEquals = this.b != null ? this.b.equals(other.b) : this.b == other.a;

		return aEquals && bEquals;
	}

	@Override
	public int hashCode()
	{
		int aHash = (this.a != null ? this.a.hashCode() : 0);
		int bHash = (this.b != null ? this.b.hashCode() : 0);

		return (aHash*bHash)+(aHash-bHash);
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
	public Duo<U, T> getInverted()
	{
		return new Duo<U, T>(this.b, this.a);
	}

}