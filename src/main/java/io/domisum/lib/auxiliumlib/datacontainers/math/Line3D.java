package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;

@API
public class Line3D
{

	// CONSTANTS
	public static final double THRESHOLD = 1d/(1000d*1000d*1000d);

	// PROPERTIES
	@API
	public final Vector3D base;
	@API
	public final Vector3D direction;


	// INIT
	@API
	public Line3D(Vector3D base, Vector3D direction)
	{
		this.base = base;
		this.direction = direction.normalize();
	}


	// GETTERS
	@API
	public boolean containsPoint(Vector3D point)
	{
		Vector3D crossProduct = direction.crossProduct(point.subtract(base));
		return crossProduct.lengthSquared() <= THRESHOLD;
	}

	@API
	public Vector3D getPointOnLineClosestToPoint(Vector3D point)
	{
		Vector3D w = point.subtract(base);

		double wvProduct = w.dotProduct(direction);
		double vvProduct = direction.dotProduct(direction);

		double productQuot = wvProduct/vvProduct;
		return base.add(direction.multiply(productQuot));
	}

	@API
	public LineSegment3D getShortestConnection(Line3D other)
	{
		// http://geomalgorithms.com/a07-_distance.html#dist3D_Segment_to_Segment

		Vector3D w0 = base.subtract(other.base);

		double a = direction.dotProduct(direction);
		double b = direction.dotProduct(other.direction);
		double c = other.direction.dotProduct(other.direction);
		double d = direction.dotProduct(w0);
		double e = other.direction.dotProduct(w0);

		double denominator = (a*c)-(b*b);
		if(denominator == 0) // lines are parallel, choose an arbitrary connection
			return new LineSegment3D(base, other.getPointOnLineClosestToPoint(base));

		double xpNominator = (b*e)-(c*d);
		double xqNominator = (a*e)-(b*d);

		double xp = xpNominator/denominator;
		double xq = xqNominator/denominator;

		Vector3D point1 = base.add(direction.multiply(xp));
		Vector3D point2 = other.base.add(other.direction.multiply(xq));
		return new LineSegment3D(point1, point2);
	}


	// DISTANCE
	@API
	public double getDistanceTo(Vector3D point)
	{
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html

		Vector3D pToOne = base.subtract(point);

		Vector3D crossProduct = direction.crossProduct(pToOne);
		return crossProduct.length()/direction.length();
	}

	@API
	public double getDistanceTo(Line3D other)
	{
		LineSegment3D lineSegment = getShortestConnection(other);
		return lineSegment.getLength();
	}

}
