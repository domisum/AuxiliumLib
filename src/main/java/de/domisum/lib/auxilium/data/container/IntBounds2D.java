package de.domisum.lib.auxilium.data.container;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

@APIUsage
public class IntBounds2D
{

	@APIUsage public final int minX;
	@APIUsage public final int maxX;
	@APIUsage public final int minY;
	@APIUsage public final int maxY;


	// INIT
	@APIUsage public IntBounds2D(int x1, int x2, int y1, int y2)
	{
		this.minX = Math.min(x1, x2);
		this.maxX = Math.max(x1, x2);
		this.minY = Math.min(y1, y2);
		this.maxY = Math.max(y1, y2);
	}


	// MODIFICATIONS
	@APIUsage public IntBounds2D expand(int distance)
	{
		if(distance < 0)
			throw new IllegalArgumentException("The distance can't be negative");

		return new IntBounds2D(this.minX-distance, this.maxX+distance, this.minY-distance, this.maxY+distance);
	}

	@APIUsage public IntBounds2D limit(IntBounds2D limit)
	{
		return new IntBounds2D(Math.max(this.minX, limit.minX), Math.min(this.maxX, limit.maxX), Math.max(this.minY, limit.minY),
				Math.min(this.maxY, limit.maxY));
	}

}
