package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class Coordinate2DInt
{
	
	// ATTRIBUTES
	private final int x;
	private final int y;
	
	
	// HOUSEKEEPING
	public static Coordinate2DInt zero()
	{
		return new Coordinate2DInt(0, 0);
	}
	
	@Override
	public String toString()
	{
		return PHR.r("[x={},y={}]", x, y);
	}
	
	
	// GETTERS
	public double getLength()
	{
		var xs = getX() * (double) getX();
		var ys = getY() * (double) getY();
		return Math.sqrt(xs + ys);
	}
	
	
	// DERIVE
	@API
	public Coordinate2DInt deriveAdd(Coordinate2DInt other)
	{
		return new Coordinate2DInt(x + other.x, y + other.y);
	}
	
	@API
	public Coordinate2DInt deriveAdd(int dX, int dY)
	{
		return new Coordinate2DInt(x + dX, y + dY);
	}
	
	@API
	public Coordinate2DInt deriveSubtract(Coordinate2DInt other)
	{
		return new Coordinate2DInt(x - other.x, y - other.y);
	}
	
	@API
	public Coordinate2DInt deriveSubtract(int dX, int dY)
	{
		return new Coordinate2DInt(x - dX, y - dY);
	}
	
	@API
	public Coordinate2DInt deriveReversed()
	{
		return new Coordinate2DInt(-x, -y);
	}
	
	@API
	public Coordinate2DInt deriveMergeMin(Coordinate2DInt other)
	{
		return new Coordinate2DInt(Math.min(x, other.x), Math.min(y, other.y));
	}
	
}
