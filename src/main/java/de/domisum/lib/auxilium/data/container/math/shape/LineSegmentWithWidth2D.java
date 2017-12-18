package de.domisum.lib.auxilium.data.container.math.shape;

import de.domisum.lib.auxilium.data.container.math.LineSegment2D;
import de.domisum.lib.auxilium.data.container.math.Vector2D;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LineSegmentWithWidth2D implements GeometricShape2D
{

	private final LineSegment2D lineSegment;
	private final double maxDistance;


	// SHAPE
	@Override public boolean contains(Vector2D point)
	{
		return lineSegment.getDistanceTo(point) < maxDistance;
	}

}
