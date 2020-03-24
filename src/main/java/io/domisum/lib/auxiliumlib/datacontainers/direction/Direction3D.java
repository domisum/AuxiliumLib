package io.domisum.lib.auxiliumlib.datacontainers.direction;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Random;

@API
@RequiredArgsConstructor
public enum Direction3D
{
	
	NORTH(0, 0, -1),
	SOUTH(0, 0, 1),
	EAST(1, 0, 0),
	WEST(-1, 0, 0),
	UP(0, 1, 0),
	DOWN(0, -1, 0);
	
	
	// ATTRIBUTES
	@Getter
	private final int dX;
	@Getter
	private final int dY;
	@Getter
	private final int dZ;
	
	
	// GETTERS
	@API
	public static Direction3D getRandom(Random r)
	{
		return values()[r.nextInt(values().length)];
	}
	
}
