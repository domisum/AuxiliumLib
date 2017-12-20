package de.domisum.lib.auxilium.data.container.bound;

import de.domisum.lib.auxilium.util.java.annotations.API;
import org.apache.commons.lang3.Validate;

import java.awt.Rectangle;

@API
public class IntBounds2D
{

	@API public final int minX;
	@API public final int maxX;
	@API public final int minY;
	@API public final int maxY;


	// INIT
	@API public IntBounds2D(int x1, int x2, int y1, int y2)
	{
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}


	// GETTERS
	@API public int getWidth()
	{
		return maxX-minX;
	}

	@API public int getHeight()
	{
		return maxY - minY;
	}


	// MODIFICATIONS
	@API public IntBounds2D expand(int distance)
	{
		Validate.isTrue(distance > 0, "The distance has to be positive");
		return new IntBounds2D(minX-distance, maxX+distance, minY-distance, maxY+distance);
	}


	@API public IntBounds2D contractLeft(int distance)
	{
		Validate.isTrue(distance <= maxX-minX, "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX+distance, maxX, minY, maxY);
	}

	@API public IntBounds2D contractRight(int distance)
	{
		Validate.isTrue(distance <= maxX-minX, "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX-distance, minY, maxY);
	}

	@API public IntBounds2D contractLeftAndRight(int distance)
	{
		return contractLeft(distance).contractRight(distance);
	}

	@API public IntBounds2D contractTop(int distance)
	{
		Validate.isTrue(distance <= maxY-minY, "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX, minY+distance, maxY);
	}

	@API public IntBounds2D contractBottom(int distance)
	{
		Validate.isTrue(distance <= maxY-minY, "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX, minY, maxY-distance);
	}

	@API public IntBounds2D contractTopAndBottom(int distance)
	{
		return contractTop(distance).contractBottom(distance);
	}

	@API public IntBounds2D contractOnAllSides(int distance)
	{
		return contractTopAndBottom(distance).contractLeftAndRight(distance);
	}


	@API public IntBounds2D limit(IntBounds2D limit)
	{
		return new IntBounds2D(Math.max(minX, limit.minX),
				Math.min(maxX, limit.maxX),
				Math.max(minY, limit.minY),
				Math.min(maxY, limit.maxY));
	}


	// CONVERSION
	public Rectangle toAwtRectangle()
	{
		return new Rectangle(minX, minY, getWidth(), getHeight());
	}

}
