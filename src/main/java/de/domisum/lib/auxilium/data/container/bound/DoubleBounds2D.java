package de.domisum.lib.auxilium.data.container.bound;

import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.math.MathUtil;

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


	// TRANSFORMS
	@APIUsage public Vector2D toRelative(Vector2D absolute)
	{
		double rX = MathUtil.remapLinear(this.minX, this.maxX, 0, 1, absolute.x);
		double rY = MathUtil.remapLinear(this.minY, this.maxY, 0, 1, absolute.y);

		return new Vector2D(rX, rY);
	}

}
