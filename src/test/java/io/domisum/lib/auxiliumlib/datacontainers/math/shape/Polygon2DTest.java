package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Polygon2DTest
{

	@Test
	public void testSimpleContains()
	{
		Polygon2D polygon2D = new Polygon2D(new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(0, 10));
		Vector2D inside = new Vector2D(5, 5);
		Vector2D outside = new Vector2D(5, 5000);

		Assertions.assertTrue(polygon2D.contains(inside));
		Assertions.assertFalse(polygon2D.contains(outside));
	}

	@Test
	public void testContains2()
	{
		Polygon2D polygon2D = new Polygon2D(new Vector2D(48, -85), new Vector2D(41, -85), new Vector2D(41, -79));
		Vector2D vector2D = new Vector2D(43, -83);

		Assertions.assertTrue(polygon2D.contains(vector2D));
	}

}