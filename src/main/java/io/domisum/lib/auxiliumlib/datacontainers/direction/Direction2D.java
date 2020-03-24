package io.domisum.lib.auxiliumlib.datacontainers.direction;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@API
@RequiredArgsConstructor
public enum Direction2D
{
	
	NORTH(0, -1),
	SOUTH(0, 1),
	EAST(1, 0),
	WEST(-1, 0);
	
	
	// ATTRIBUTES
	@Getter
	private final int dX;
	@Getter
	private final int dZ;
	
	
	// GETTERS
	@API
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
	
	@API
	public Direction2D getRandomOther(Random r)
	{
		Direction2D dir;
		do
			dir = getRandom(r);
		while(dir == this);
		
		return dir;
	}
	
	
	// UTIL
	@API
	public static Direction2D getFromOffset(int dX, int dZ)
	{
		for(var direction2D : Direction2D.values())
			if((direction2D.getDX() == dX) && (direction2D.getDZ() == dZ))
				return direction2D;
		
		return null;
	}
	
	@API
	public static Direction2D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}
	
}
