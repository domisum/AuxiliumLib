package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;

@API
public interface GeometricShape2D
{
	
	@API
	boolean contains(Vector2D point);
	
}
