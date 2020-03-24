package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Polygon2DTest
{
	
	@Test
	public void testSimpleContains()
	{
		var polygon2D = new Polygon2D(new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(0, 10));
		var inside = new Vector2D(5, 5);
		var outside = new Vector2D(5, 5000);
		
		Assertions.assertTrue(polygon2D.contains(inside));
		Assertions.assertFalse(polygon2D.contains(outside));
	}
	
	@Test
	public void testContains2()
	{
		var polygon2D = new Polygon2D(new Vector2D(48, -85), new Vector2D(41, -85), new Vector2D(41, -79));
		var vector2D = new Vector2D(43, -83);
		
		Assertions.assertTrue(polygon2D.contains(vector2D));
	}
	
}
