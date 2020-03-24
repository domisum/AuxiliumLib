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
@EqualsAndHashCode

@API
public class Vector3D
{

	@API
	@Getter
	public final double x;
	@API
	@Getter
	public final double y;
	@API
	@Getter
	public final double z;


	// INIT
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

	/**
	 * Constructs a null-Vector3D, where, x, y and z are set to 0.
	 */
	@API
	public Vector3D()
	{
		this(0, 0, 0);
	}


	// OBJECT

	/**
	 * Combines the coordinates of this object into a string.
	 * <p>
	 * The coordinates are rounded to 3 decimal places to keep the String short and easily readable.
	 *
	 * @return This Vector3D, represented in String form
	 */
	@Override
	public String toString()
	{
		return "Vector3D[x="+MathUtil.round(x, 3)+",y="+MathUtil.round(y, 3)+",z="+MathUtil.round(z, 3)+"]";
	}


	// SELF

	/**
	 * Returns the length of the vector.
	 *
	 * @return the length of the vector
	 */
	@API
	public double length()
	{
		return Math.sqrt(lengthSquared());
	}

	/**
	 * Returns the length of the vector squared.
	 * <p>
	 * This should be used to compare the lengths of two vectors, because it doesn't use the square-root
	 * and is therefore much faster.
	 *
	 * @return the length of the vector squared.
	 */
	@API
	public double lengthSquared()
	{
		return (x*x)+(y*y)+(z*z);
	}

	/**
	 * Returns the length of the vector, ignoring the y-component.
	 *
	 * @return length of the x-z-component of the vector
	 */
	@API
	public double xzLength()
	{
		return Math.sqrt(xzLengthSquared());
	}

	/**
	 * Returns the length of the vector, ignoring the y-component, squared.
	 * <p>
	 * This should be used to compare the length of the x-z-component of two vector, because it doesn't use the square-root
	 * and is therefore much faster.
	 *
	 * @return length of the x-z-component of the vector, squared
	 */
	@API
	public double xzLengthSquared()
	{
		return (x*x)+(z*z);
	}

	/**
	 * Returns a normalized copy of this vector, leaving the vector upon this method was called unchanged.
	 *
	 * @return normalized copy of this vector
	 */
	@API
	public Vector3D normalize()
	{
		double length = length();
		if(length == 0)
			throw new UnsupportedOperationException("can't normalize a vector of length 0");

		return new Vector3D(x/length, y/length, z/length);
	}

	/**
	 * Returns an inverted copy of this vector, this means every component is negated (multiplied by -1)
	 *
	 * @return inverted copy of this vector
	 */
	@API
	public Vector3D invert()
	{
		return multiply(-1);
	}

	/**
	 * Returns a new vector that is orthogonal to this one.
	 * <p>
	 * Since there are infinitely many vectors that fulfill this condition, the solution can vary.
	 * The returned vector is not normalized by default.
	 *
	 * @return vector orthogonal to this vector
	 */
	@API
	public Vector3D orthogonal()
	{
		Vector3D independent = ((x == 0) && (y == 0)) ? new Vector3D(1, 1, z) : new Vector3D(x, y, z+1);
		return crossProduct(independent);
	}

	// INTERACTION

	/**
	 * Returns a new vector that adds the coordinates of this vector to the coordinates of the vector supplied
	 * through the argument.
	 *
	 * @param other the vector to add to this one
	 * @return new vector that is the sum of both vectors
	 */
	@API
	public Vector3D add(Vector3D other)
	{
		return new Vector3D(x+other.x, y+other.y, z+other.z);
	}

	/**
	 * Returns a new vector that adds the coordinates of this vector to
	 * the coordinates supplied through the argument.
	 *
	 * @param dX the x-value to add to this vector
	 * @param dY the y-value to add to this vector
	 * @param dZ the z-value to add to this vector
	 * @return new vector that is the sum of both vectors
	 */
	@API
	public Vector3D add(double dX, double dY, double dZ)
	{
		return new Vector3D(x+dX, y+dY, z+dZ);
	}

	/**
	 * Returns a new vector that subtracts the coordinates of the vector supplied
	 * through the argument from the coordinates of this vector.
	 * <p>
	 * This method is the inverse of {@code #add(Vector3D)}
	 *
	 * @param other the vector to subtract from this one
	 * @return new vector that is the difference of both vectors
	 * @see #add(Vector3D)
	 */
	@API
	public Vector3D subtract(Vector3D other)
	{
		return add(other.invert());
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
	 *
	 * @param other    the vector to move towards to
	 * @param distance the distance moved towards the vector
	 * @return the moved copy of this vector
	 */
	@API
	public Vector3D moveTowards(Vector3D other, double distance)
	{
		Vector3D dir = other.subtract(this).normalize();
		return add(dir.multiply(distance));
	}


	/**
	 * Returns a copy of this vector multiplied by a scalar value supplied through the argument.
	 * <p>
	 * This method is the opposite of {@code #divide(double)}
	 *
	 * @param factor the factor to multiply the copy by
	 * @return the multiplied copy of this vector
	 */
	@API
	public Vector3D multiply(double factor)
	{
		return new Vector3D(x*factor, y*factor, z*factor);
	}

	/**
	 * Returns a copy of this vector divided by a scalar value supplied through the argument.
	 * <p>
	 * This method is the opposite of {@code #multiply(double)}
	 * <p>
	 *
	 * @param divisor the value to divide the copy by
	 * @return the divided vector
	 */
	@API
	public Vector3D divide(double divisor)
	{
		return multiply(1/divisor);
	}

	/**
	 * This method calculates the dot product of this vector and the vector supplied through the argument.
	 * The dot product is also known as the scalar product.
	 *
	 * @param other the other vector
	 * @return the dot product
	 */
	@API
	public double dotProduct(Vector3D other)
	{
		return (x*other.x)+(y*other.y)+(z*other.z);
	}

	/**
	 * This method calculates the cross product of this vector and the vector supplied through the argument,
	 * returning a new Vector3D.
	 * The cross product is also known as vector product.
	 *
	 * @param other the other vector
	 * @return the crossProduct of the vectors in a new Vector3D
	 */
	@API
	public Vector3D crossProduct(Vector3D other)
	{
		double nX = (y*other.z)-(z*other.y);
		double nY = (z*other.x)-(x*other.z);
		double nZ = (x*other.y)-(y*other.x);

		return new Vector3D(nX, nY, nZ);
	}

	/**
	 * Calculates the distance from this vector to the vector supplied through the argument.
	 *
	 * @param other the other vector
	 * @return the distance
	 */
	@API
	public double distanceTo(Vector3D other)
	{
		return Math.sqrt(distanceToSquared(other));
	}

	/**
	 * Calculates the distance from this vector to the vector supplied through the argument, squared.
	 * <p>
	 * This should be used to compare distances because it doesn't use the square-root
	 * and is therefore much faster.
	 *
	 * @param other the other vector
	 * @return the distance
	 */
	@API
	public double distanceToSquared(Vector3D other)
	{
		return subtract(other).lengthSquared();
	}


	// LINE

	/**
	 * Creates a line through this point and the point from the argument.
	 *
	 * @param other the other point
	 * @return the line
	 */
	@API
	public Line3D getLineTowards(Vector3D other)
	{
		return new Line3D(this, other.subtract(this));
	}

	/**
	 * Creates a line segment which uses this point as one endpoint and
	 * the point from the argument as the other endpoint.
	 *
	 * @param other the other endpoint
	 * @return the line segment
	 */
	@API
	public LineSegment3D getLineSegmentBetween(Vector3D other)
	{
		return new LineSegment3D(this, other);
	}


	// QUATERNION

	/**
	 * Creates a pure quaternion from this vector.
	 * The quaterion will have set {@code w} to zero, the x-, y- and z-values will be copied from this vector.
	 *
	 * @return the pure quaternion
	 */
	@API
	public Quaternion getPureQuaternion()
	{
		return new Quaternion(0, x, y, z);
	}

	/**
	 * Returns a copy of this vector rotated by the quaternion from the argument.
	 *
	 * @param rotation the rotation quaternion
	 * @return the rotated copy of this vector
	 */
	@API
	public Vector3D rotate(Quaternion rotation)
	{
		Quaternion thisAsQuaternion = getPureQuaternion();
		Quaternion resultQuaternion = rotation.conjugate().multiply(thisAsQuaternion).multiply(rotation);
		return resultQuaternion.getVector();
	}

}
