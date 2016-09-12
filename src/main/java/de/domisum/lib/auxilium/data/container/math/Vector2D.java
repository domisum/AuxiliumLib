package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;

@APIUsage
public class Vector2D
{

	@APIUsage
	public final double x;
	@APIUsage
	public final double y;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@APIUsage
	public Vector2D()
	{
		this(0, 0);
	}

	@Deprecated
	@APIUsage
	@Override
	public Vector2D clone()
	{
		return new Vector2D(this.x, this.y);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Vector2D))
			return false;

		Vector2D other = (Vector2D) o;

		return (this.x == other.x) && (this.y == other.y);
	}

	@Override
	public int hashCode()
	{
		return (int) Math.round((this.x*this.y)+((this.x-this.y)*1000));
	}

	@Override
	public String toString()
	{
		return "vector[x="+MathUtil.round(this.x, 3)+",y="+MathUtil.round(this.y, 3)+"]";
	}


	// -------
	// SELF
	// -------
	@APIUsage
	public double length()
	{
		return Math.sqrt(lengthSquared());
	}

	@APIUsage
	public double lengthSquared()
	{
		return (this.x*this.x)+(this.y*this.y);
	}

	@APIUsage
	public Vector2D normalize()
	{
		double length = length();

		return new Vector2D(this.x/length, this.y/length);
	}

	@APIUsage
	public Vector2D invert()
	{
		return new Vector2D(-this.x, -this.y);
	}


	@APIUsage
	public Vector2D multiply(double factor)
	{
		return new Vector2D(this.x*factor, this.y*factor);
	}

	@APIUsage
	public Vector2D divide(double divisor)
	{
		return multiply(1/divisor);
	}


	// -------
	// INTERACTION
	// -------
	@APIUsage
	public Vector2D add(Vector2D other)
	{
		return new Vector2D(this.x+other.x, this.y+other.y);
	}

	@APIUsage
	public Vector2D subtract(Vector2D other)
	{
		return add(other.invert());
	}

	@APIUsage
	public double dotProduct(Vector2D other)
	{
		return (this.x*other.x)+(this.y*other.y);
	}

	@APIUsage
	public double distanceTo(Vector2D other)
	{
		return subtract(other).length();
	}

	@APIUsage
	public double distanceToSquared(Vector2D other)
	{
		return subtract(other).lengthSquared();
	}

}
