package de.domisum.lib.auxilium.data.container.math.shape;

import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;

public class Square2D implements GeometricShape2D
{

	@API @Getter private final double minX;
	@API @Getter private final double maxX;
	@API @Getter private final double minY;
	@API @Getter private final double maxY;


	// INIT
	@API public Square2D(double x1, double x2, double y1, double y2)
	{
		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}


	// SHAPE
	@Override @API public boolean contains(Vector2D point)
	{
		if(point.x < this.minX || point.x > this.maxX)
			return false;

		if(point.y < this.minY || point.y > this.maxY)
			return false;

		return true;
	}

}
