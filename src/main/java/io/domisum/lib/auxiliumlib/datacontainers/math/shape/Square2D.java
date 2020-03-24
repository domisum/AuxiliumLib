package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import lombok.Getter;
import lombok.ToString;

@ToString
public class Square2D
		implements GeometricShape2D
{

	@API
	@Getter
	private final double minX;
	@API
	@Getter
	private final double maxX;
	@API
	@Getter
	private final double minY;
	@API
	@Getter
	private final double maxY;


	// INIT
	@API
	public Square2D(double x1, double x2, double y1, double y2)
	{
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}


	// SHAPE
	@Override
	@API
	public boolean contains(Vector2D point)
	{
		if((point.getX()<minX) || (point.getX()>maxX))
			return false;

		if((point.getY()<minY) || (point.getY()>maxY))
			return false;

		return true;
	}

}
