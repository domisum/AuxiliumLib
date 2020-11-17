package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.Validate;

/**
 * Class to describe a Vector in 3 Dimensions.
 * <p>
 * The coordinates are immutable, so every action performed on a object returns a new object with new values,
 * while the Vector3D on which the action was performed remains unchanged.
 */
@API
@EqualsAndHashCode
public class Vector3D
{
	
	@Getter
	private final double x;
	@Getter
	private final double y;
	@Getter
	private final double z;
	
	
	// HOUSEKEEPING
	@API
	public Vector3D(double x, double y, double z)
	{
		Validate.notNaN(x, "x can't be NaN");
		Validate.notNaN(y, "y can't be NaN");
		Validate.notNaN(z, "z can't be NaN");
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString()
	{
		return "Vector3D[x="+MathUtil.round(x, 3)+",y="+MathUtil.round(y, 3)+",z="+MathUtil.round(z, 3)+"]";
	}
	
	
	// SELF
	@API
	public double getLength()
	{
		return Math.sqrt(getLengthSquared());
	}
	
	@API
	public double getLengthSquared()
	{
		return (x*x)+(y*y)+(z*z);
	}
	
	@API
	public double xzLength()
	{
		return Math.sqrt(xzLengthSquared());
	}
	
	@API
	public double xzLengthSquared()
	{
		return (x*x)+(z*z);
	}
	
	@API
	public Vector3D deriveNormalized()
	{
		double length = getLength();
		if(length == 0)
			throw new UnsupportedOperationException("can't normalize a vector of length 0");
		return new Vector3D(x/length, y/length, z/length);
	}
	
	@API
	public Vector3D deriveInverted()
	{
		return deriveMultiplyLength(-1);
	}
	
	/**
	 * Returns a new vector that is orthogonal to this one.
	 * <p>
	 * Since there are infinitely many vectors that fulfill this condition,
	 * the solution could be any one of them. The returned vector is not normalized by default.
	 *
	 * @return vector orthogonal to this vector
	 */
	@API
	public Vector3D deriveOrthogonal()
	{
		var independent = ((x == 0) && (y == 0)) ?
			new Vector3D(1, 1, z) :
			new Vector3D(x, y, z+1);
		return deriveCrossProduct(independent);
	}
	
	// INTERACTION
	@API
	public Vector3D deriveAdd(Vector3D other)
	{
		return new Vector3D(x+other.x, y+other.y, z+other.z);
	}
	
	@API
	public Vector3D deriveAdd(double dX, double dY, double dZ)
	{
		return new Vector3D(x+dX, y+dY, z+dZ);
	}
	
	@API
	public Vector3D deriveSubtract(Vector3D other)
	{
		return deriveAdd(other.deriveInverted());
	}
	
	/**
	 * Returns a new vector, which is a copy of this vector moved towards the vector
	 * supplied through the argument by the distance supplied through the argument.
	 * <p>
	 * Example:
	 * a = new Vector3D(1, 2, 3);
	 * b = new Vector3D(5, 2, 3);
	 * c = a.moveTowards(b, 3);
	 * Then c is (4, 2, 3).
	 */
	@API
	public Vector3D deriveMovedTowards(Vector3D other, double distance)
	{
		var dir = other.deriveSubtract(this).deriveNormalized();
		return deriveAdd(dir.deriveMultiplyLength(distance));
	}
	
	@API
	public Vector3D deriveMultiplyLength(double factor)
	{
		return new Vector3D(x*factor, y*factor, z*factor);
	}
	
	@API
	public Vector3D deriveDivideLength(double divisor)
	{
		return deriveMultiplyLength(1/divisor);
	}
	
	
	@API
	public double dotProduct(Vector3D other)
	{
		return (x*other.x)+(y*other.y)+(z*other.z);
	}
	
	@API
	public Vector3D deriveCrossProduct(Vector3D other)
	{
		double nX = (y*other.z)-(z*other.y);
		double nY = (z*other.x)-(x*other.z);
		double nZ = (x*other.y)-(y*other.x);
		
		return new Vector3D(nX, nY, nZ);
	}
	
	@API
	public double distanceTo(Vector3D other)
	{
		return Math.sqrt(distanceToSquared(other));
	}
	
	/**
	 * This should be used to compare distances because it doesn't use the square-root
	 * and is therefore much faster.
	 */
	@API
	public double distanceToSquared(Vector3D other)
	{
		return deriveSubtract(other).getLengthSquared();
	}
	
	
	// LINE
	@API
	public Line3D getLineTowards(Vector3D other)
	{
		return new Line3D(this, other.deriveSubtract(this));
	}
	
	@API
	public LineSegment3D getLineSegmentBetween(Vector3D other)
	{
		return new LineSegment3D(this, other);
	}
	
	
	// QUATERNION
	@API
	public Quaternion getPureQuaternion()
	{
		return new Quaternion(0, x, y, z);
	}
	
	@API
	public Vector3D deriveRotate(Quaternion rotation)
	{
		var thisAsQuaternion = getPureQuaternion();
		var resultQuaternion = rotation.deriveConjugated().deriveMultiply(thisAsQuaternion).deriveMultiply(rotation);
		return resultQuaternion.getVector();
	}
	
}
