package de.domisum.auxiliumapi.data.container.dir;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

import java.util.Random;

@APIUsage
public enum DiagonalDirection
{

	// -------
	// VALUES
	// -------
	// @formatter:off
	NORTH_EAST(1,-1),
	SOUTH_EAST(1, 1),
	SOUTH_WEST(-1, 1),
	NORTH_WEST(-1 ,-1);
	// @formatter:on


	public final int dX;
	public final int dZ;


	// -------
	// CONSTRUCTOR
	// -------
	DiagonalDirection(int dX, int dZ)
	{
		this.dX = dX;
		this.dZ = dZ;
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Direction2D getRandomStraightChildDirection(Random r)
	{
		String[] straightChildDirectionNames = name().split("_");
		String straightChildDirectionName = straightChildDirectionNames[r.nextInt(straightChildDirectionNames.length)];

		return Direction2D.valueOf(straightChildDirectionName);
	}

	@APIUsage
	public Direction2D getOtherStraightChildDirection(Direction2D dir)
	{
		String[] straightChildDirectionNames = name().split("_");

		if(straightChildDirectionNames[0].equals(dir.name()))
			return Direction2D.valueOf(straightChildDirectionNames[1]);
		else
			return Direction2D.valueOf(straightChildDirectionNames[0]);
	}

	@APIUsage
	public static DiagonalDirection getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}

}
