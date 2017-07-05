package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;
import lombok.EqualsAndHashCode;

@APIUsage
@EqualsAndHashCode
public class Vector2D
{

	@APIUsage public final double x;
	@APIUsage public final double y;


	// CONSTRUCTOR
	@APIUsage public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@APIUsage public Vector2D()
	{
		this(0, 0);
	}


	// OBJECT
	@Override public String toString()
	{
		return "vector[x="+MathUtil.round(this.x, 3)+",y="+MathUtil.round(this.y, 3)+"]";
	}


	// SELF
	@APIUsage public double length()
	{
		return Math.sqrt(lengthSquared());
	}

	@APIUsage public double lengthSquared()
	{
		return (this.x*this.x)+(this.y*this.y);
	}

	@APIUsage public Vector2D normalize()
	{
		double length = length();

		return new Vector2D(this.x/length, this.y/length);
	}

	@APIUsage public Vector2D invert()
	{
		return new Vector2D(-this.x, -this.y);
	}


	@APIUsage public Vector2D multiply(double factor)
	{
		return new Vector2D(this.x*factor, this.y*factor);
	}

	@APIUsage public Vector2D divide(double divisor)
	{
		return multiply(1/divisor);
	}


	// INTERACTION
	@APIUsage public Vector2D add(Vector2D other)
	{
		return new Vector2D(this.x+other.x, this.y+other.y);
	}

	@APIUsage public Vector2D subtract(Vector2D other)
	{
		return add(other.invert());
	}

	@APIUsage public double dotProduct(Vector2D other)
	{
		return (this.x*other.x)+(this.y*other.y);
	}

	@APIUsage public double distanceTo(Vector2D other)
	{
		return subtract(other).length();
	}

	@APIUsage public double distanceToSquared(Vector2D other)
	{
		return subtract(other).lengthSquared();
	}

	/**
	 * Calculates the angle between this vector and the vector passed by the arguments and returns it in radians.
	 *
	 * @return the angle in radians
	 */
	@APIUsage public double getAngleTo(Vector2D other)
	{
		// https://software.intel.com/en-us/forums/intel-visual-fortran-compiler-for-windows/topic/515013

		double angle = Math.atan2(other.y, other.x)-Math.atan2(this.y, this.x);
		if(angle < 0)
			angle += 2*Math.PI;

		return angle;
	}

}
