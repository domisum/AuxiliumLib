package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@API
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Coordinate2DInt
{

	@Getter
	public final int x;
	@Getter
	public final int y;


	public Coordinate2DInt add(Coordinate2DInt other)
	{
		return new Coordinate2DInt(x+other.x, y+other.y);
	}

	public Coordinate2DInt add(int dX, int dY)
	{
		return new Coordinate2DInt(x+dX, y+dY);
	}

}
