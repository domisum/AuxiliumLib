package de.domisum.auxiliumapi.data.container.block;

import de.domisum.auxiliumapi.data.container.dir.Direction2D;

public class BlockCoordinate implements Comparable<BlockCoordinate>
{

	// PROPERTIES
	public final int x;
	public final int y;
	public final int z;


	// -------
	// CONSTRUCTOR
	// -------
	public BlockCoordinate(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockCoordinate(int x, int z)
	{
		this(x, 0, z);
	}


	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof BlockCoordinate))
			return false;

		BlockCoordinate other = (BlockCoordinate) o;
		return (other.x == this.x) && (other.y == this.y) && (other.z == this.z);
	}

	@Override
	public int hashCode()
	{
		return (this.x*503)+(this.y*1000003)+this.z;
	}

	@Override
	public String toString()
	{
		return "BlockCoordinate[x="+this.x+",y="+this.y+",z="+this.z+"]";
	}


	@Override
	public int compareTo(BlockCoordinate other)
	{
		int dX = other.x-this.x;
		if(dX != 0)
			return dX;

		int dZ = other.z-this.z;
		if(dZ != 0)
			return dZ;

		int dY = other.y-this.y;
		if(dY != 0)
			return dY;

		return 0;
	}


	// -------
	// INTERACTION
	// -------
	public BlockCoordinate add(int dX, int dY, int dZ)
	{
		return new BlockCoordinate(this.x+dX, this.y+dY, this.z+dZ);
	}

	public BlockCoordinate add(Direction2D dir2d)
	{
		return add(dir2d.dX, 0, dir2d.dZ);
	}


	public double getDistance(BlockCoordinate other)
	{
		return Math.sqrt(getDistanceSquared(other));
	}

	public double getDistanceSquared(BlockCoordinate other)
	{
		int dX = other.x-this.x;
		int dY = other.y-this.y;
		int dZ = other.z-this.z;

		return (dX*dX)+(dY*dY)+(dZ*dZ);
	}

}
