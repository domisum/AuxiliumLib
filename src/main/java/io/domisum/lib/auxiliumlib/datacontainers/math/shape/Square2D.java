package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import lombok.Getter;
import lombok.ToString;

@API
@ToString
public class Square2D
	implements GeometricShape2D
{
	
	@Getter
	private final double minX;
	@Getter
	private final double maxX;
	@Getter
	private final double minY;
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
	@API
	@Override
	public boolean contains(Vector2D point)
	{
		if((point.getX() < minX) || (point.getX() > maxX))
			return false;
		
		if((point.getY() < minY) || (point.getY() > maxY))
			return false;
		
		return true;
	}
	
}
