package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

public class DoubleBounds2D
{

	@APIUsage public final double minX;
	@APIUsage public final double maxX;
	@APIUsage public final double minY;
	@APIUsage public final double maxY;


	// INIT
	@APIUsage public DoubleBounds2D(double x1, double x2, double y1, double y2)
	{
		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}


	// CHECKS
	@APIUsage public boolean contains(Vector2D point)
	{
		if(point.x < this.minX || point.x > this.maxX)
			return false;

		if(point.y < this.minY || point.y > this.maxY)
			return false;

		return true;
	}

}
