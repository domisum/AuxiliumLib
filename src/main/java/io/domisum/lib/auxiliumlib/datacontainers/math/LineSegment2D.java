package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.bound.DoubleBounds2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@API
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class LineSegment2D
{
	
	// PROPERTIES
	@Getter
	private final Vector2D pointA;
	@Getter
	private final Vector2D pointB;
	
	
	// GETTERS
	@API
	public double getLength()
	{
		return pointA.distanceTo(pointB);
	}
	
	@API
	public double getLengthSquared()
	{
		return pointA.distanceToSquared(pointB);
	}
	
	@API
	public Vector2D getDirection()
	{
		return pointB.deriveSubtract(pointA);
	}
	
	
	// DISTANCE
	@API
	public double getDistanceTo(Vector2D point)
	{
		// http://geomalgorithms.com/a02-_lines.html
		
		var v = pointB.deriveSubtract(pointA);
		var w = point.deriveSubtract(pointA);
		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct<=0)
			return point.distanceTo(pointA);
		if(v.dotProduct(v)<=wvProduct)
			return point.distanceTo(pointB);
		double productQuot = wvProduct/vvProduct;
		var pointOnSegment = pointA.deriveAdd(v.deriveMultiply(productQuot));
		double distance = point.distanceTo(pointOnSegment);
		
		return distance;
	}
	
	@API
	public double getDistanceTo(LineSegment2D other)
	{
		if(intersects(other))
			return 0;
		
		double minDistance = Double.MAX_VALUE;
		minDistance = Math.min(minDistance, getDistanceTo(other.pointA));
		minDistance = Math.min(minDistance, getDistanceTo(other.pointB));
		minDistance = Math.min(minDistance, other.getDistanceTo(pointA));
		minDistance = Math.min(minDistance, other.getDistanceTo(pointB));
		return minDistance;
	}
	
	
	// CHECKS
	@API
	public boolean intersects(LineSegment2D other)
	{
		// http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
		
		var thisWithOtherP1 = getPointArrangement(pointA, pointB, other.pointA);
		var thisWithOtherP2 = getPointArrangement(pointA, pointB, other.pointB);
		
		// special case: all points are colinear
		if((thisWithOtherP1 == PointArrangement.COLINEAR) && (thisWithOtherP2 == PointArrangement.COLINEAR))
		{
			var thisBounds = new DoubleBounds2D(pointA.getX(), pointB.getX(), pointA.getY(), pointB.getY());
			return thisBounds.contains(other.pointA) || thisBounds.contains(other.pointB);
		}
		
		// default case: points are not all colinear
		if(thisWithOtherP1 == thisWithOtherP2)
			return false;
		var otherWithThisP1 = getPointArrangement(other.pointA, other.pointB, pointA);
		var otherWithThisP2 = getPointArrangement(other.pointA, other.pointB, pointB);
		if(otherWithThisP1 == otherWithThisP2)
			return false;
		
		return true;
	}
	
	@API
	public boolean isColinear(LineSegment2D other)
	{
		return (getPointArrangement(pointA, pointB, other.pointA) == PointArrangement.COLINEAR) &&
				(getPointArrangement(pointA, pointB, other.pointB) == PointArrangement.COLINEAR);
	}
	
	@API
	public boolean contains(Vector2D point)
	{
		double distance = getDistanceTo(point);
		return distance<Line3D.THRESHOLD;
	}
	
	
	// HELPER
	private static PointArrangement getPointArrangement(Vector2D p1, Vector2D p2, Vector2D p3)
	{
		// http://www.geeksforgeeks.org/orientation-3-ordered-points/
		
		var oneToTwo = p2.deriveSubtract(p1);
		var twoToThree = p3.deriveSubtract(p2);
		double rot = (oneToTwo.getY()*twoToThree.getX())-(twoToThree.getY()*oneToTwo.getX());
		if(Math.abs(rot)<Line3D.THRESHOLD)
			return PointArrangement.COLINEAR;
		var pointArrangement = (rot<0) ?
				PointArrangement.COUNTERCLOCKWISE :
				PointArrangement.CLOCKWISE;
		
		return pointArrangement;
	}
	
	private enum PointArrangement
	{
		
		COLINEAR,
		CLOCKWISE,
		COUNTERCLOCKWISE
		
	}
	
}
