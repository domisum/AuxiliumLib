package de.domisum.auxiliumapi.data.container.math;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

@APIUsage
public class Line3D
{

	// PROPERTIES
	@APIUsage
	public final Vector3D base;
	@APIUsage
	public final Vector3D direction;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public Line3D(Vector3D base, Vector3D direction)
	{
		this.base = base;
		this.direction = direction.normalize();
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Vector3D getPointOnLineClosestToPoint(Vector3D point)
	{
		Vector3D w = point.subtract(this.base);

		double wvProduct = w.dotProduct(this.direction);
		double vvProduct = this.direction.dotProduct(this.direction);

		double productQuot = wvProduct/vvProduct;
		return this.base.add(this.direction.multiply(productQuot));
	}

	@APIUsage
	public LineSegment3D getShortestConnection(Line3D other)
	{
		// http://geomalgorithms.com/a07-_distance.html#dist3D_Segment_to_Segment

		Vector3D w0 = this.base.subtract(other.base);

		double a = this.direction.dotProduct(this.direction);
		double b = this.direction.dotProduct(other.direction);
		double c = other.direction.dotProduct(other.direction);
		double d = this.direction.dotProduct(w0);
		double e = other.direction.dotProduct(w0);

		double denominator = a*c-b*b;
		if(denominator == 0) // lines are parallel, choose an arbitrary connection
			return new LineSegment3D(this.base, other.getPointOnLineClosestToPoint(this.base));

		double xpNominator = b*e-c*d;
		double xqNominator = a*e-b*d;

		double xp = xpNominator/denominator;
		double xq = xqNominator/denominator;

		Vector3D point1 = this.base.add(this.direction.multiply(xp));
		Vector3D point2 = other.base.add(other.direction.multiply(xq));
		return new LineSegment3D(point1, point2);
	}


	// -------
	// DISTANCE
	// -------
	@APIUsage
	public double getDistanceTo(Vector3D point)
	{
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html

		Vector3D pToOne = this.base.subtract(point);

		Vector3D crossProduct = this.direction.crossProduct(pToOne);
		return crossProduct.length()/this.direction.length();
	}

	@APIUsage
	public double getDistanceTo(Line3D other)
	{
		LineSegment3D lineSegment = getShortestConnection(other);
		return lineSegment.getLength();
	}

}
