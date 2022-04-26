package io.domisum.lib.auxiliumlib.datacontainers.bound;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

@API
@ToString
public final class IntBounds2D
{
	
	@Getter
	private final int minX;
	@Getter
	private final int maxX;
	@Getter
	private final int minY;
	@Getter
	private final int maxY;
	
	
	// INIT
	@API
	public static IntBounds2D fromBounds(int x1, int x2, int y1, int y2)
	{
		return new IntBounds2D(x1, x2, y1, y2);
	}
	
	@API
	public static IntBounds2D fromPosAndSize(int x, int y, int width, int height)
	{
		return new IntBounds2D(x, (x+width)-1, y, (y+height)-1);
	}
	
	@API
	private IntBounds2D(int x1, int x2, int y1, int y2)
	{
		minX = Math.min(x1, x2);
		maxX = Math.max(x1, x2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}
	
	
	// GETTERS
	@API
	public int getWidth()
	{
		return (maxX-minX)+1;
	}
	
	@API
	public int getHeight()
	{
		return (maxY-minY)+1;
	}
	
	@API
	public boolean contain(int x, int y)
	{
		if((x < minX) || (x > maxX))
			return false;
		
		if((y < minY) || (y > maxY))
			return false;
		
		return true;
	}
	
	
	// MODIFICATIONS
	@API
	public IntBounds2D expand(int distance)
	{
		Validate.isTrue(distance > 0, "The distance has to be positive");
		return new IntBounds2D(minX-distance, maxX+distance, minY-distance, maxY+distance);
	}
	
	@API
	public IntBounds2D expandLeft(int distance)
	{
		return new IntBounds2D(minX-distance, maxX, minY, maxY);
	}
	
	@API
	public IntBounds2D expandRight(int distance)
	{
		return new IntBounds2D(minX, maxX+distance, minY, maxY);
	}
	
	@API
	public IntBounds2D expandTop(int distance)
	{
		return new IntBounds2D(minX, maxX, minY-distance, maxY);
	}
	
	@API
	public IntBounds2D expandBottom(int distance)
	{
		return new IntBounds2D(minX, maxX, minY, maxY+distance);
	}
	
	
	@API
	public IntBounds2D contractLeft(int distance)
	{
		Validate.isTrue(distance < getWidth(), "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX+distance, maxX, minY, maxY);
	}
	
	@API
	public IntBounds2D contractRight(int distance)
	{
		Validate.isTrue(distance < getWidth(), "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX-distance, minY, maxY);
	}
	
	@API
	public IntBounds2D contractLeftAndRight(int distance)
	{
		return contractLeft(distance).contractRight(distance);
	}
	
	@API
	public IntBounds2D contractTop(int distance)
	{
		Validate.isTrue(distance < getHeight(), "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX, minY+distance, maxY);
	}
	
	@API
	public IntBounds2D contractBottom(int distance)
	{
		Validate.isTrue(distance < getHeight(), "distance has to be smaller than size of bounds");
		return new IntBounds2D(minX, maxX, minY, maxY-distance);
	}
	
	@API
	public IntBounds2D contractTopAndBottom(int distance)
	{
		return contractTop(distance).contractBottom(distance);
	}
	
	@API
	public IntBounds2D contractOnAllSides(int distance)
	{
		return contractTopAndBottom(distance).contractLeftAndRight(distance);
	}
	
	
	@API
	public IntBounds2D limit(IntBounds2D limit)
	{
		return new IntBounds2D(
			Math.max(minX, limit.minX),
			Math.min(maxX, limit.maxX),
			Math.max(minY, limit.minY),
			Math.min(maxY, limit.maxY));
	}
	
}
