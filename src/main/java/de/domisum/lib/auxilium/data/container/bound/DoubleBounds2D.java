package de.domisum.lib.auxilium.data.container.bound;

import de.domisum.lib.auxilium.data.container.math.Vector2D;
import de.domisum.lib.auxilium.data.container.math.shape.Square2D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import de.domisum.lib.auxilium.util.math.MathUtil;

public class DoubleBounds2D extends Square2D
{

	// INIT
	public DoubleBounds2D(double x1, double x2, double y1, double y2)
	{
		super(x1, x2, y1, y2);
	}


	// TRANSFORMS
	@API public Vector2D toRelative(Vector2D absolute)
	{
		double rX = MathUtil.remapLinear(getMinX(), getMaxX(), 0, 1, absolute.x);
		double rY = MathUtil.remapLinear(getMinY(), getMaxY(), 0, 1, absolute.y);

		return new Vector2D(rX, rY);
	}

}
