package de.domisum.lib.auxilium.data.container.direction;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AllArgsConstructor;

import java.util.Random;

@API
@AllArgsConstructor
public enum Direction3D
{

	// @formatter:off
	NORTH(0, 0, -1),
	SOUTH(0,0,  1),
	EAST(1, 0, 0),
	WEST(-1, 0, 0),
	UP(0, 1, 0),
	DOWN(0, -1, 0);
	// @formatter:on


	public final int dX;
	public final int dY;
	public final int dZ;


	// GETTERS
	@API
	public static Direction3D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}

}
