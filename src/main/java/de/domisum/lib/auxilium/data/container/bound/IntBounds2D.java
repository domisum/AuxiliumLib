package de.domisum.lib.auxilium.data.container.bound;

import de.domisum.lib.auxilium.util.java.annotations.API;

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
		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}


	// MODIFICATIONS
	@API public IntBounds2D expand(int distance)
	{
		if(distance < 0)
			throw new IllegalArgumentException("The distance can't be negative");

		return new IntBounds2D(this.minX-distance, this.maxX+distance, this.minY-distance, this.maxY+distance);
	}

	@API public IntBounds2D limit(IntBounds2D limit)
	{
		return new IntBounds2D(Math.max(this.minX, limit.minX), Math.min(this.maxX, limit.maxX), Math.max(this.minY, limit.minY),
				Math.min(this.maxY, limit.maxY));
	}

}
