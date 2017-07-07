package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;

/**
 * Class to describe a Vector in 3 Dimensions.
 * <p>
 * The coordinates are immutable, so every action performed on a object returns a new object with new values,
 * while the Vector3D on which the action was performed remains unchanged.
 */
@APIUsage
public class Vector3D
{

	@APIUsage public final double x;
	@APIUsage public final double y;
	@APIUsage public final double z;


	// -------
	// CONSTRUCTOR
	// -------

	/**
	 * Constructs a Vector3D from the x-, y- and z-coordinate
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	@APIUsage public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a null-Vector3D, where, x, y and z are set to 0.
	 */
	@APIUsage public Vector3D()
	{
		this(0, 0, 0);
	}

	/**
	 * Checks if this Vector equals the supplied object.
	 * <p>
	 * If the given object is not of the type Vector3D, false is returned.
	 * This returns true if and only if
	 * {@code this.x == other.x && this.y == other.y && this.z == other.z}
	 *
	 * @param o The object to compare to
	 * @return whether the object describes the same coordinate
	 */
	@Override public boolean equals(Object o)
	{
		if(!(o instanceof Vector3D))
			return false;

		Vector3D other = (Vector3D) o;
		return other.x == this.x && other.y == this.y && other.z == this.z;
	}

	/**
	 * Hashes the object, conforming with the #equals(Object) method.
	 * <p>
	 * If the equals method between two Vector3Ds returns true,
	 * this method will return the same integer for both of the vectors.
	 *
	 * @return the hash value of the Vector3D
	 */
	public int hashCode()
	{
		int hashCode = 13;

		long xLong = Double.doubleToLongBits(this.x);
		long yLong = Double.doubleToLongBits(this.y);
		long zLong = Double.doubleToLongBits(this.z);
		hashCode = hashCode*31+((int) (xLong^(xLong>>>32)));
		hashCode = hashCode*31+((int) (yLong^(yLong>>>32)));
		hashCode = hashCode*31+((int) (zLong^(zLong>>>32)));

		return hashCode;
	}

	/**
	 * Combines the coordinates of this object into a string.
	 * <p>
	 * The coordinates are rounded to 3 decimal places to keep the String short and easily readable.
	 *
	 * @return This Vector3D, represented in String form
	 */
	@Override public String toString()
	{
		return "vector[x="+MathUtil.round(this.x, 3)+",y="+MathUtil.round(this.y, 3)+",z="+MathUtil.round(this.z, 3)+"]";
	}


	// -------
	// SELF
	// -------

	/**
	 * Returns the length of the vector.
	 *
	 * @return the length of the vector
	 */
	@APIUsage public double length()
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
	@APIUsage public double lengthSquared()
	{
		return (this.x*this.x)+(this.y*this.y)+(this.z*this.z);
	}

	/**
	 * Returns the length of the vector, ignoring the y-component.
	 *
	 * @return length of the x-z-component of the vector
	 */
	@APIUsage public double xzLength()
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
	@APIUsage public double xzLengthSquared()
	{
		return (this.x*this.x)+(this.z*this.z);
	}

	/**
	 * Returns a normalized copy of this vector, leaving the vector upon this method was called unchanged.
	 *
	 * @return normalized copy of this vector
	 */
	@APIUsage public Vector3D normalize()
	{
		double length = length();

		return new Vector3D(this.x/length, this.y/length, this.z/length);
	}

	/**
	 * Returns an inverted copy of this vector, this means every component is negated (multiplied by -1)
	 *
	 * @return inverted copy of this vector
	 */
	@APIUsage public Vector3D invert()
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
	@APIUsage public Vector3D orthogonal()
	{
		Vector3D independent;
		if((this.x == 0) && (this.y == 0))
			independent = new Vector3D(1, 1, this.z);
		else
			independent = new Vector3D(this.x, this.y, this.z+1);

		return crossProduct(independent);
	}

	// -------
	// INTERACTION
	// -------

	/**
	 * Returns a new vector that adds the coordinates of this vector to the coordinates of the vector supplied
	 * through the argument.
	 *
	 * @param other the vector to add to this one
	 * @return new vector that is the sum of both vectors
	 */
	@APIUsage public Vector3D add(Vector3D other)
	{
		return new Vector3D(this.x+other.x, this.y+other.y, this.z+other.z);
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
	@APIUsage public Vector3D add(double dX, double dY, double dZ)
	{
		return new Vector3D(this.x+dX, this.y+dY, this.z+dZ);
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
	@APIUsage public Vector3D subtract(Vector3D other)
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
	@APIUsage public Vector3D moveTowards(Vector3D other, double distance)
	{
		Vector3D dir = other.subtract(this).normalize();
		return this.add(dir.multiply(distance));
	}


	/**
	 * Returns a copy of this vector multiplied by a scalar value supplied through the argument.
	 * <p>
	 * This method is the opposite of {@code #divide(double)}
	 *
	 * @param factor the factor to multiply the copy by
	 * @return the multiplied copy of this vector
	 * @see #multiply(double)
	 */
	@APIUsage public Vector3D multiply(double factor)
	{
		return new Vector3D(this.x*factor, this.y*factor, this.z*factor);
	}

	/**
	 * Returns a copy of this vector divided by a scalar value supplied through the argument.
	 * <p>
	 * This method is the opposite of {@code #multiply(double)}
	 * <p>
	 *
	 * @param divisor the value to divide the copy by
	 * @return the divided vector
	 * @see #divide(double)
	 */
	@APIUsage public Vector3D divide(double divisor)
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
	@APIUsage public double dotProduct(Vector3D other)
	{
		return (this.x*other.x)+(this.y*other.y)+(this.z*other.z);
	}

	/**
	 * This method calculates the cross product of this vector and the vector supplied through the argument,
	 * returning a new Vector3D.
	 * The cross product is also known as vector product.
	 *
	 * @param other the other vector
	 * @return the crossProduct of the vectors in a new Vector3D
	 */
	@APIUsage public Vector3D crossProduct(Vector3D other)
	{
		double nX = (this.y*other.z)-(this.z*other.y);
		double nY = (this.z*other.x)-(this.x*other.z);
		double nZ = (this.x*other.y)-(this.y*other.x);

		return new Vector3D(nX, nY, nZ);
	}

	/**
	 * Calculates the distance from this vector to the vector supplied through the argument.
	 *
	 * @param other the other vector
	 * @return the distance
	 */
	@APIUsage public double distanceTo(Vector3D other)
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
	@APIUsage public double distanceToSquared(Vector3D other)
	{
		return subtract(other).lengthSquared();
	}


	// -------
	// LINE
	// -------

	/**
	 * Creates a line through this point and the point from the argument.
	 *
	 * @param other the other point
	 * @return the line
	 */
	@APIUsage public Line3D getLineTowards(Vector3D other)
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
	@APIUsage public LineSegment3D getLineSegmentBetween(Vector3D other)
	{
		return new LineSegment3D(this, other);
	}


	// -------
	// QUATERNION
	// -------

	/**
	 * Creates a pure quaternion from this vector.
	 * The quaterion will have set {@code w} to zero, the x-, y- and z-values will be copied from this vector.
	 *
	 * @return the pure quaternion
	 */
	@APIUsage public Quaternion getPureQuaternion()
	{
		return new Quaternion(0, this.x, this.y, this.z);
	}

	/**
	 * Returns a copy of this vector rotated by the quaternion from the argument.
	 *
	 * @param rotation the rotation quaternion
	 * @return the rotated copy of this vector
	 */
	@APIUsage public Vector3D rotate(Quaternion rotation)
	{
		Quaternion thisAsQuaternion = getPureQuaternion();
		Quaternion resultQuaternion = rotation.conjugate().multiply(thisAsQuaternion).multiply(rotation);
		return resultQuaternion.getVector();
	}

}
