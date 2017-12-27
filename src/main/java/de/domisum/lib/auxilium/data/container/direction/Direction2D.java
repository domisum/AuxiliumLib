package de.domisum.lib.auxilium.data.container.direction;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@API
@AllArgsConstructor
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


	// GETTERS
	@API public Direction2D getOpposite()
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

	@API public Direction2D getRandomOther(Random r)
	{
		Direction2D dir;
		do
			dir = getRandom(r);
		while(dir == this);

		return dir;
	}


	// UTIL
	@API public static Direction2D getFromOffset(int dX, int dZ)
	{
		for(Direction2D d : Direction2D.values())
			if((d.dX == dX) && (d.dZ == dZ))
				return d;

		return null;
	}


	@API public static Direction2D getFromYaw(float yaw)
	{
		double adjustedYaw = yaw%360;

		while(adjustedYaw < -180)
			adjustedYaw += 360;

		while(adjustedYaw > 180)
			adjustedYaw -= 360;


		if(adjustedYaw < -135.0f)
			return NORTH;
		if(adjustedYaw < -45.0f)
			return EAST;
		if(adjustedYaw < 45.0f)
			return SOUTH;
		if(adjustedYaw < 135.0f)
			return WEST;
		//if(adjustedYaw >= 135.0f)
		return NORTH;
	}

	@API public static Direction2D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}


	@API public static List<Direction2D> getValuesAsList()
	{
		return new ArrayList<>(Arrays.asList(values()));
	}

}
