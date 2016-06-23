package de.domisum.auxiliumapi.data.container.math;

import de.domisum.auxiliumapi.data.container.block.BlockCoordinate;
import de.domisum.auxiliumapi.util.math.MathUtil;

public class Vector3D
{

	public final double x;
	public final double y;
	public final double z;


	// -------
	// CONSTRUCTOR
	// -------
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D()
	{
		this(0, 0, 0);
	}

	@Override
	public Vector3D clone()
	{
		return new Vector3D(this.x, this.y, this.z);
	}

	@Override
	public String toString()
	{
		return "vector[x=" + MathUtil.round(this.x, 3) + ",y=" + MathUtil.round(this.y, 3) + ",z=" + MathUtil.round(this.z, 3)
				+ "]";
	}


	// -------
	// SELF
	// -------
	public double length()
	{
		return Math.sqrt(lengthSquared());
	}

	public double lengthSquared()
	{
		return (this.x * this.x) + (this.y * this.y) + (this.z * this.z);
	}

	public Vector3D normalize()
	{
		double length = length();

		return new Vector3D(this.x / length, this.y / length, this.z / length);
	}

	public Vector3D invert()
	{
		return multiply(-1);
	}

	public Vector3D orthogonal()
	{
		Vector3D independent = null;
		if((this.x == 0) && (this.y == 0))
			independent = new Vector3D(1, 1, this.z);
		else
			independent = new Vector3D(this.x, this.y, this.z + 1);

		return crossProduct(independent);
	}

	public BlockCoordinate getBlockCoordinate()
	{
		return new BlockCoordinate((int) Math.floor(this.x), (int) Math.floor(this.y), (int) Math.floor(this.z));
	}


	// -------
	// INTERACTION
	// -------
	public Vector3D add(Vector3D other)
	{
		return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public Vector3D subtract(Vector3D other)
	{
		return add(other.invert());
	}


	public Vector3D multiply(double factor)
	{
		return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
	}

	public Vector3D divide(double divisor)
	{
		return multiply(1 / divisor);
	}


	public double dotProduct(Vector3D other)
	{
		return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
	}

	public Vector3D crossProduct(Vector3D other)
	{
		double nX = (this.y * other.z) - (this.z * other.y);
		double nY = (this.z * other.x) - (this.x * other.z);
		double nZ = (this.x * other.y) - (this.y * other.x);

		return new Vector3D(nX, nY, nZ);
	}


	public double distanceTo(Vector3D other)
	{
		return Math.sqrt(distanceToSquared(other));
	}

	public double distanceToSquared(Vector3D other)
	{
		return subtract(other).length();
	}


	// -------
	// QUATERNION
	// -------
	public Quaternion getPureQuaternion()
	{
		return new Quaternion(0, this.x, this.y, this.z);
	}

	public Vector3D rotate(Quaternion rotation)
	{
		Quaternion thisAsQuaternion = getPureQuaternion();
		Quaternion resultQuaternion = rotation.conjugate().multiply(thisAsQuaternion).multiply(rotation);
		return resultQuaternion.getVector();
	}

}
