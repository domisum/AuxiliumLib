package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Coordinate2DInt
{
	
	// ATTRIBUTES
	@Getter
	private final int x;
	@Getter
	private final int y;
	
	
	// DERIVE
	@API
	public Coordinate2DInt deriveAdd(Coordinate2DInt other)
	{
		return new Coordinate2DInt(x+other.x, y+other.y);
	}
	
	@API
	public Coordinate2DInt deriveAdd(int dX, int dY)
	{
		return new Coordinate2DInt(x+dX, y+dY);
	}
	
}
