package de.domisum.auxiliumapi.data.container.math;

import de.domisum.auxiliumapi.data.container.block.BlockCoordinate;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.math.MathUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

@APIUsage
public class Vector3D
{

	@APIUsage
	public final double x;
	@APIUsage
	public final double y;
	@APIUsage
	public final double z;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@APIUsage
	public Vector3D(Vector bukkitVector)
	{
		this(bukkitVector.getX(), bukkitVector.getY(), bukkitVector.getZ());
	}

	@APIUsage
	public Vector3D(Location location)
	{
		this(location.getX(), location.getY(), location.getZ());
	}

	@APIUsage
	public Vector3D()
	{
		this(0, 0, 0);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Vector3D))
			return false;

		Vector3D other = (Vector3D) o;
		return other.x == this.x && other.y == this.y && other.z == this.z;
	}

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

	@Override
	public String toString()
	{
		return "vector[x="+MathUtil.round(this.x, 3)+",y="+MathUtil.round(this.y, 3)+",z="+MathUtil.round(this.z, 3)+"]";
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
		return (this.x*this.x)+(this.y*this.y)+(this.z*this.z);
	}

	@APIUsage
	public double xzLength()
	{
		return Math.sqrt(xzLengthSquared());
	}

	@APIUsage
	public double xzLengthSquared()
	{
		return (this.x*this.x)+(this.z*this.z);
	}

	@APIUsage
	public Vector3D normalize()
	{
		double length = length();

		return new Vector3D(this.x/length, this.y/length, this.z/length);
	}

	@APIUsage
	public Vector3D invert()
	{
		return multiply(-1);
	}

	@APIUsage
	public Vector3D orthogonal()
	{
		Vector3D independent;
		if((this.x == 0) && (this.y == 0))
			independent = new Vector3D(1, 1, this.z);
		else
			independent = new Vector3D(this.x, this.y, this.z+1);

		return crossProduct(independent);
	}

	@APIUsage
	public BlockCoordinate getBlockCoordinate()
	{
		return new BlockCoordinate((int) Math.floor(this.x), (int) Math.floor(this.y), (int) Math.floor(this.z));
	}

	@APIUsage
	public Vector toBukkit()
	{
		return new Vector(this.x, this.y, this.z);
	}

	public Location toLocation(World world)
	{
		return new Location(world, this.x, this.y, this.z);
	}


	// -------
	// INTERACTION
	// -------
	@APIUsage
	public Vector3D add(Vector3D other)
	{
		return new Vector3D(this.x+other.x, this.y+other.y, this.z+other.z);
	}

	@APIUsage
	public Vector3D add(double dX, double dY, double dZ)
	{
		return new Vector3D(this.x+dX, this.y+dY, this.z+dZ);
	}

	@APIUsage
	public Vector3D subtract(Vector3D other)
	{
		return add(other.invert());
	}


	@APIUsage
	public Vector3D multiply(double factor)
	{
		return new Vector3D(this.x*factor, this.y*factor, this.z*factor);
	}

	@APIUsage
	public Vector3D divide(double divisor)
	{
		return multiply(1/divisor);
	}


	@APIUsage
	public double dotProduct(Vector3D other)
	{
		return (this.x*other.x)+(this.y*other.y)+(this.z*other.z);
	}

	@APIUsage
	public Vector3D crossProduct(Vector3D other)
	{
		double nX = (this.y*other.z)-(this.z*other.y);
		double nY = (this.z*other.x)-(this.x*other.z);
		double nZ = (this.x*other.y)-(this.y*other.x);

		return new Vector3D(nX, nY, nZ);
	}


	@APIUsage
	public double distanceTo(Vector3D other)
	{
		return Math.sqrt(distanceToSquared(other));
	}

	@APIUsage
	public double distanceToSquared(Vector3D other)
	{
		return subtract(other).lengthSquared();
	}


	// -------
	// LINE
	// -------
	@APIUsage
	public Line3D getLineTowards(Vector3D other)
	{
		return new Line3D(this, other.subtract(this));
	}

	@APIUsage
	public LineSegment3D getLineSegmentTowards(Vector3D other)
	{
		return new LineSegment3D(this, other);
	}


	// -------
	// QUATERNION
	// -------
	@APIUsage
	public Quaternion getPureQuaternion()
	{
		return new Quaternion(0, this.x, this.y, this.z);
	}

	@APIUsage
	public Vector3D rotate(Quaternion rotation)
	{
		Quaternion thisAsQuaternion = getPureQuaternion();
		Quaternion resultQuaternion = rotation.conjugate().multiply(thisAsQuaternion).multiply(rotation);
		return resultQuaternion.getVector();
	}

}
