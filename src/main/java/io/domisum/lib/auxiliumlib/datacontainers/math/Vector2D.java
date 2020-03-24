package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
public class Vector2D
{
	
	@Getter
	private final double x;
	@Getter
	private final double y;
	
	
	// INIT
	@API
	public Vector2D()
	{
		this(0, 0);
	}
	
	
	// OBJECT
	@Override
	public String toString()
	{
		return "Vector[x="+MathUtil.round(x, 5)+",y="+MathUtil.round(y, 5)+"]";
	}
	
	
	// SELF
	@API
	public double length()
	{
		return Math.sqrt(lengthSquared());
	}
	
	@API
	public double lengthSquared()
	{
		return (x*x)+(y*y);
	}
	
	@API
	public Vector2D deriveNormalized()
	{
		double length = length();
		return new Vector2D(x/length, y/length);
	}
	
	@API
	public Vector2D deriveInverted()
	{
		return new Vector2D(-x, -y);
	}
	
	@API
	public Vector2D deriveOrthogonal()
	{
		var this3D = new Vector3D(x, y, 0);
		var upright = new Vector3D(0, 0, 1);
		var orthogonal3D = this3D.deriveCrossProduct(upright);
		var orthogonal = new Vector2D(orthogonal3D.getX(), orthogonal3D.getY()).deriveNormalized();
		
		return orthogonal;
	}
	
	
	// INTERACTION
	@API
	public Vector2D deriveMultiply(double factor)
	{
		return new Vector2D(x*factor, y*factor);
	}
	
	@API
	public Vector2D deriveDivide(double divisor)
	{
		return deriveMultiply(1/divisor);
	}
	
	@API
	public Vector2D deriveAdd(Vector2D other)
	{
		return new Vector2D(x+other.x, y+other.y);
	}
	
	@API
	public Vector2D deriveSubtract(Vector2D other)
	{
		return deriveAdd(other.deriveInverted());
	}
	
	@API
	public double dotProduct(Vector2D other)
	{
		return (x*other.x)+(y*other.y);
	}
	
	@API
	public double distanceTo(Vector2D other)
	{
		return deriveSubtract(other).length();
	}
	
	@API
	public double distanceToSquared(Vector2D other)
	{
		return deriveSubtract(other).lengthSquared();
	}
	
	/**
	 * Returns the angleDegAbs between this and the other vector in the range of 0 - pi (0 - 180 degree) in radians.
	 *
	 * @param other the other vector
	 * @return the angleDegAbs in radians
	 */
	@API
	public double getAngleToRad(Vector2D other)
	{
		// https://software.intel.com/en-us/forums/intel-visual-fortran-compiler-for-windows/topic/515013
		return (((Math.atan2(other.y, other.x)-Math.atan2(y, x))+(2*Math.PI))%(2*Math.PI))-Math.PI;
	}
	
	/**
	 * @param other the other vector
	 * @return the angleDegAbs in degrees
	 * @see #getAngleToRad(Vector2D)
	 */
	@API
	public double getAngleToDeg(Vector2D other)
	{
		return Math.toDegrees(getAngleToRad(other));
	}
	
}

