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
	@APIUsage public IntBounds2D(int minX, int maxX, int minY, int maxY)
	{
		if(minX > maxX)
			throw new IllegalArgumentException("minX can't be greater than maxX ("+minX+","+maxX+")");

		if(minY > maxY)
			throw new IllegalArgumentException("minY can't be greater than maxY ("+minY+","+maxY+")");

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
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
