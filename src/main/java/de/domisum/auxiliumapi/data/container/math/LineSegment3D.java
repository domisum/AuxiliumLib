package de.domisum.auxiliumapi.data.container.math;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

@APIUsage
public class LineSegment3D
{

	// PROPERTIES
	@APIUsage
	public final Vector3D a;
	@APIUsage
	public final Vector3D b;


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public LineSegment3D(Vector3D a, Vector3D b)
	{
		this.a = a;
		this.b = b;
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public double getLength()
	{
		return Math.sqrt(getLengthSquared());
	}

	@APIUsage
	public double getLengthSquared()
	{
		return this.b.subtract(this.a).lengthSquared();
	}


	// CONVERSION
	@APIUsage
	public Line3D toLine()
	{
		return new Line3D(this.a, this.b.subtract(this.a));
	}


	// -------
	// DISTANCE
	// -------
	@APIUsage
	public double getDistanceTo(Vector3D point)
	{
		// http://geomalgorithms.com/a02-_lines.html

		Vector3D v = this.b.subtract(this.a);
		Vector3D w = point.subtract(this.a);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct <= 0)
			return point.distanceTo(this.a);
		if(v.dotProduct(v) <= wvProduct)
			return point.distanceTo(this.b);

		double productQuot = wvProduct/vvProduct;
		Vector3D pointOnSegment = this.a.add(v.multiply(productQuot));

		return point.distanceTo(pointOnSegment);
	}

	@APIUsage
	public double getDistanceTo(LineSegment3D lineSegment)
	{
		// TODO fix for line segments, this is just reusing the infinite line code
		return toLine().getDistanceTo(lineSegment.toLine());
	}

}
