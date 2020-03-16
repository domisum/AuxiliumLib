package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;

public class CompositeShape2D implements GeometricShape2D
{

	private final GeometricShape2D[] geometricShapes;


	// INIT
	public CompositeShape2D(GeometricShape2D... geometricShapes)
	{
		this.geometricShapes = geometricShapes.clone();
	}


	// SHAPE
	@Override
	public boolean contains(Vector2D point)
	{
		for(GeometricShape2D s : geometricShapes)
			if(s.contains(point))
				return true;

		return false;
	}

}
