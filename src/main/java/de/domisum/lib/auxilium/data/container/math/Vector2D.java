package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.MathUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
public class Vector2D
{

	@API @Getter private final double x;
	@API @Getter private final double y;


	// INIT
	@API public Vector2D()
	{
		this(0, 0);
	}


	// OBJECT
	@Override public String toString()
	{
		return "vector[x="+MathUtil.round(x, 3)+",y="+MathUtil.round(y, 3)+"]";
	}


	// SELF
	@API public double length()
	{
		return Math.sqrt(lengthSquared());
	}

	@API public double lengthSquared()
	{
		return (x*x)+(y*y);
	}

	@API public Vector2D normalize()
	{
		double length = length();

		return new Vector2D(x/length, y/length);
	}

	@API public Vector2D invert()
	{
		return new Vector2D(-x, -y);
	}

	@API public Vector2D orthogonal()
	{
		Vector3D this3D = new Vector3D(x, y, 0);
		Vector3D upright = new Vector3D(0, 0, 1);

		Vector3D orthogonal3D = this3D.crossProduct(upright);
		Vector2D orthogonal = new Vector2D(orthogonal3D.x, orthogonal3D.y).normalize();

		return orthogonal;
	}


	@API public Vector2D multiply(double factor)
	{
		return new Vector2D(x*factor, y*factor);
	}

	@API public Vector2D divide(double divisor)
	{
		return multiply(1/divisor);
	}


	// INTERACTION
	@API public Vector2D add(Vector2D other)
	{
		return new Vector2D(x+other.x, y+other.y);
	}

	@API public Vector2D subtract(Vector2D other)
	{
		return add(other.invert());
	}

	@API public double dotProduct(Vector2D other)
	{
		return (x*other.x)+(y*other.y);
	}

	@API public double distanceTo(Vector2D other)
	{
		return subtract(other).length();
	}

	@API public double distanceToSquared(Vector2D other)
	{
		return subtract(other).lengthSquared();
	}

	/**
	 * Returns the angleDegAbs between this and the other vector in the range of 0 - pi (0 - 180 degree) in radians.
	 *
	 * @param other the other vector
	 * @return the angleDegAbs in radians
	 */
	@API public double getAngleToRad(Vector2D other)
	{
		// https://software.intel.com/en-us/forums/intel-visual-fortran-compiler-for-windows/topic/515013

		return (((Math.atan2(other.y, other.x)-Math.atan2(y, x))+(2*Math.PI))%(2*Math.PI))-Math.PI;
	}

	/**
	 * @param other the other vector
	 * @return the angleDegAbs in degrees
	 * @see #getAngleToRad(Vector2D)
	 */
	@API public double getAngleToDeg(Vector2D other)
	{
		return Math.toDegrees(getAngleToRad(other));
	}

}

