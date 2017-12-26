package de.domisum.lib.auxilium.data.container.direction;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;

import java.util.Random;

@API
@AllArgsConstructor
public enum DiagonalDirection
{

	// VALUES
	// @formatter:off
	NORTH_EAST(1,-1),
	SOUTH_EAST(1, 1),
	SOUTH_WEST(-1, 1),
	NORTH_WEST(-1 ,-1);
	// @formatter:on


	public final int dX;
	public final int dZ;


	// GETTERS
	@API public Direction2D getRandomStraightChildDirection(Random r)
	{
		String[] straightChildDirectionNames = name().split("_");
		String straightChildDirectionName = straightChildDirectionNames[r.nextInt(straightChildDirectionNames.length)];

		return Direction2D.valueOf(straightChildDirectionName);
	}

	@API public Direction2D getOtherStraightChildDirection(Direction2D dir)
	{
		String[] straightChildDirectionNames = name().split("_");

		if(straightChildDirectionNames[0].equals(dir.name()))
			return Direction2D.valueOf(straightChildDirectionNames[1]);
		else
			return Direction2D.valueOf(straightChildDirectionNames[0]);
	}

	@API public static DiagonalDirection getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}

}
