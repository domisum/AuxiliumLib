package de.domisum.lib.auxilium.data.container.dir;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@APIUsage
public enum Direction2D
{

	// @formatter:off
	NORTH(0, -1),
	SOUTH(0, 1),
	EAST(1, 0),
	WEST(-1, 0);
	// @formatter:on


	public final int dX;
	public final int dZ;


	// -------
	// CONSTRUCTOR
	// -------
	Direction2D(int dX, int dZ)
	{
		this.dX = dX;
		this.dZ = dZ;
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Direction2D getRandomOther(Random r)
	{
		Direction2D dir;
		do
			dir = getRandom(r);
		while(dir == this);

		return dir;
	}

	@APIUsage
	public Direction2D getOpposite()
	{
		if(this == NORTH)
			return SOUTH;
		if(this == SOUTH)
			return NORTH;
		if(this == EAST)
			return WEST;
		if(this == WEST)
			return EAST;

		return null;
	}


	@APIUsage
	public static Direction2D getFromYaw(float yaw)
	{
		if(yaw < -135.0f)
			return NORTH;
		if(yaw < -45.0f)
			return EAST;
		if(yaw < 45.0f)
			return SOUTH;
		if(yaw < 135.0f)
			return WEST;
		//if(yaw >= 135.0f)
		return NORTH;
	}

	@APIUsage
	public static Direction2D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}

	@APIUsage
	public static List<Direction2D> getValuesAsList()
	{
		return new ArrayList<>(Arrays.asList(values()));
	}

}
