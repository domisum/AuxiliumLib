package de.domisum.auxiliumapi.data.container.dir;

import java.util.Random;

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


	// -------
	// CONSTRUCTOR
	// -------
	private Direction3D(int dX, int dY, int dZ)
	{
		this.dX = dX;
		this.dY = dY;
		this.dZ = dZ;
	}


	// -------
	// GETTERS
	// -------
	public static Direction3D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}

}
