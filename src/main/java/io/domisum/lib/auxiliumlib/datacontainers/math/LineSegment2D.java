package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.datacontainers.bound.DoubleBounds2D;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@API
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LineSegment2D
{

	// PROPERTIES
	public final Vector2D a;
	public final Vector2D b;


	// GETTERS
	@API
	public double getLength()
	{
		return a.distanceTo(b);
	}

	@API
	public double getLengthSquared()
	{
		return a.distanceToSquared(b);
	}

	@API
	public Vector2D getDirection()
	{
		return b.subtract(a);
	}


	// DISTANCE
	@API
	public double getDistanceTo(Vector2D point)
	{
		// http://geomalgorithms.com/a02-_lines.html

		Vector2D v = b.subtract(a);
		Vector2D w = point.subtract(a);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct <= 0)
			return point.distanceTo(a);
		if(v.dotProduct(v) <= wvProduct)
			return point.distanceTo(b);

		double productQuot = wvProduct/vvProduct;
		Vector2D pointOnSegment = a.add(v.multiply(productQuot));

		return point.distanceTo(pointOnSegment);
	}

	@API
	public double getDistanceTo(LineSegment2D other)
	{
		if(intersects(other))
			return 0;

		double minDistance = Double.MAX_VALUE;
		minDistance = Math.min(minDistance, getDistanceTo(other.a));
		minDistance = Math.min(minDistance, getDistanceTo(other.b));
		minDistance = Math.min(minDistance, other.getDistanceTo(a));
		minDistance = Math.min(minDistance, other.getDistanceTo(b));

		return minDistance;
	}


	// CHECKS
	@API
	public boolean intersects(LineSegment2D other)
	{
		// http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/

		PointArrangement thisWithOtherP1 = getPointArrangement(a, b, other.a);
		PointArrangement thisWithOtherP2 = getPointArrangement(a, b, other.b);

		// special case: all points are colinear
		if((thisWithOtherP1 == PointArrangement.COLINEAR) && (thisWithOtherP2 == PointArrangement.COLINEAR))
		{
			DoubleBounds2D thisBounds = new DoubleBounds2D(a.getX(), b.getX(), a.getY(), b.getY());

			return thisBounds.contains(other.a) || thisBounds.contains(other.b);
		}

		// default case: points are not all colinear

		if(thisWithOtherP1 == thisWithOtherP2)
			return false;

		PointArrangement otherWithThisP1 = getPointArrangement(other.a, other.b, a);
		PointArrangement otherWithThisP2 = getPointArrangement(other.a, other.b, b);

		if(otherWithThisP1 == otherWithThisP2)
			return false;

		return true;
	}

	@API
	public boolean isColinear(LineSegment2D other)
	{
		return (getPointArrangement(a, b, other.a) == PointArrangement.COLINEAR) && (getPointArrangement(a, b, other.b)
				== PointArrangement.COLINEAR);
	}

	@API
	public boolean contains(Vector2D point)
	{
		double distance = getDistanceTo(point);

		return distance < Line3D.THRESHOLD;
	}


	// HELPER
	private static PointArrangement getPointArrangement(Vector2D p1, Vector2D p2, Vector2D p3)
	{
		// http://www.geeksforgeeks.org/orientation-3-ordered-points/

		Vector2D oneToTwo = p2.subtract(p1);
		Vector2D twoToThree = p3.subtract(p2);

		double rot = (oneToTwo.getY()*twoToThree.getX())-(twoToThree.getY()*oneToTwo.getX());
		if(Math.abs(rot) < Line3D.THRESHOLD)
			return PointArrangement.COLINEAR;

		return (rot < 0) ? PointArrangement.COUNTERCLOCKWISE : PointArrangement.CLOCKWISE;
	}

	private enum PointArrangement
	{
		// @formatter:off
		COLINEAR,
		CLOCKWISE,
		COUNTERCLOCKWISE
		// @formatter:on
	}

}
