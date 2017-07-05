package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.data.container.bound.DoubleBounds2D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@APIUsage
public class LineSegment2D
{

	// PROPERTIES
	public final Vector2D a;
	public final Vector2D b;


	// INIT
	@APIUsage public LineSegment2D(Vector2D a, Vector2D b)
	{
		this.a = a;
		this.b = b;
	}


	// OBJECT
	@Override public String toString()
	{
		return "LineSegment2D{"+"a="+this.a+", b="+this.b+'}';
	}


	// GETTERS
	@APIUsage public double getLength()
	{
		return this.a.distanceTo(this.b);
	}

	@APIUsage public double getLengthSquared()
	{
		return this.a.distanceToSquared(this.b);
	}

	@APIUsage public Vector2D getDirection()
	{
		return this.b.subtract(this.a);
	}


	// DISTANCE
	@APIUsage public double getDistanceTo(Vector2D point)
	{
		// http://geomalgorithms.com/a02-_lines.html

		Vector2D v = this.b.subtract(this.a);
		Vector2D w = point.subtract(this.a);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct <= 0)
			return point.distanceTo(this.a);
		if(v.dotProduct(v) <= wvProduct)
			return point.distanceTo(this.b);

		double productQuot = wvProduct/vvProduct;
		Vector2D pointOnSegment = this.a.add(v.multiply(productQuot));

		return point.distanceTo(pointOnSegment);
	}


	// CHECKS
	@APIUsage public boolean intersects(LineSegment2D other)
	{
		// http://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/

		PointArrangement thisWithOtherP1 = getPointArrangement(this.a, this.b, other.a);
		PointArrangement thisWithOtherP2 = getPointArrangement(this.a, this.b, other.b);

		// special case: all points are colinear
		if(thisWithOtherP1 == PointArrangement.COLINEAR && thisWithOtherP2 == PointArrangement.COLINEAR)
		{
			DoubleBounds2D thisBounds = new DoubleBounds2D(this.a.x, this.b.x, this.a.y, this.b.y);

			return thisBounds.contains(other.a) || thisBounds.contains(other.b);
		}

		// default case: points are not all colinear

		if(thisWithOtherP1 == thisWithOtherP2)
			return false;

		PointArrangement otherWithThisP1 = getPointArrangement(other.a, other.b, this.a);
		PointArrangement otherWithThisP2 = getPointArrangement(other.a, other.b, this.b);

		if(otherWithThisP1 == otherWithThisP2)
			return false;

		return true;
	}

	@APIUsage public boolean isColinear(LineSegment2D other)
	{
		return getPointArrangement(this.a, this.b, other.a) == PointArrangement.COLINEAR
				&& getPointArrangement(this.a, this.b, other.b) == PointArrangement.COLINEAR;
	}

	@APIUsage public boolean contains(Vector2D point)
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

		double rot = oneToTwo.y*twoToThree.x-twoToThree.y*oneToTwo.x;
		if(Math.abs(rot) < Line3D.THRESHOLD)
			return PointArrangement.COLINEAR;

		return rot < 0 ? PointArrangement.COUNTERCLOCKWISE : PointArrangement.CLOCKWISE;
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
