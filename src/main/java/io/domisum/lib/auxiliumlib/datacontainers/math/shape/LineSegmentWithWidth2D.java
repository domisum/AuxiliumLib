package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.datacontainers.math.LineSegment2D;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LineSegmentWithWidth2D implements GeometricShape2D
{

	private final LineSegment2D lineSegment;
	private final double maxDistance;


	// SHAPE
	@Override
	public boolean contains(Vector2D point)
	{
		return lineSegment.getDistanceTo(point) < maxDistance;
	}

}
