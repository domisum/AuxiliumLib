package io.domisum.lib.auxiliumlib.datacontainers.bound;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import io.domisum.lib.auxiliumlib.datacontainers.math.shape.Square2D;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;

@API
public class DoubleBounds2D
		extends Square2D
{
	
	// INIT
	@API
	public DoubleBounds2D(double x1, double x2, double y1, double y2)
	{
		super(x1, x2, y1, y2);
	}
	
	
	// TRANSFORMS
	@API
	public Vector2D toRelative(Vector2D absolute)
	{
		double rX = MathUtil.remapLinear(getMinX(), getMaxX(), 0, 1, absolute.getX());
		double rY = MathUtil.remapLinear(getMinY(), getMaxY(), 0, 1, absolute.getY());
		
		return new Vector2D(rX, rY);
	}
	
}
