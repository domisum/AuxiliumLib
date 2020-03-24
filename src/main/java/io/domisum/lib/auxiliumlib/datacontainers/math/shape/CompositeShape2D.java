package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;

@API
public class CompositeShape2D
		implements GeometricShape2D
{
	
	private final GeometricShape2D[] geometricShapes;
	
	
	// INIT
	@API
	public CompositeShape2D(GeometricShape2D... geometricShapes)
	{
		this.geometricShapes = geometricShapes.clone();
	}
	
	
	// SHAPE
	@API
	@Override
	public boolean contains(Vector2D point)
	{
		for(var geometricShape2D : geometricShapes)
			if(geometricShape2D.contains(point))
				return true;
		return false;
	}
	
}
