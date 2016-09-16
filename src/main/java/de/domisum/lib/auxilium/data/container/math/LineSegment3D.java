package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

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


	// CONVERSION
	@APIUsage
	public Line3D toLine()
	{
		return new Line3D(this.a, this.b.subtract(this.a));
	}

	@APIUsage
	public LineSegment3D getShortenedBothEnds(double distance)
	{
		Vector3D newA = this.a.moveTowards(this.b, distance);
		Vector3D newB = this.b.moveTowards(this.a, distance);

		return new LineSegment3D(newA, newB);
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public double getLength()
	{
		return this.a.distanceTo(this.b);
	}

	@APIUsage
	public double getLengthSquared()
	{
		return this.a.distanceToSquared(this.b);
	}

	@APIUsage
	public boolean containsPoint(Vector3D point)
	{
		if(!toLine().containsPoint(point))
			return false;

		double delta = Math.abs(point.distanceTo(this.a)+point.distanceTo(this.b)-getLength());
		return delta < Line3D.THRESHOLD;
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
	public double getDistanceTo(LineSegment3D other)
	{
		LineSegment3D shortestConnection = toLine().getShortestConnection(other.toLine());
		boolean aOnSegment = containsPoint(shortestConnection.a);
		boolean bOnSegment = other.containsPoint(shortestConnection.b);

		// if(aOnLine && bOnLine) else
		if(aOnSegment && !bOnSegment)
		{
			Vector3D newB = other.a;
			if(shortestConnection.a.distanceToSquared(other.b) < shortestConnection.a.distanceToSquared(newB))
				newB = other.b;

			shortestConnection = new LineSegment3D(shortestConnection.a, newB);
		}
		else if(!aOnSegment && bOnSegment)
		{
			Vector3D newA = this.a;
			if(shortestConnection.b.distanceToSquared(this.b) < shortestConnection.b.distanceToSquared(newA))
				newA = this.b;

			shortestConnection = new LineSegment3D(newA, shortestConnection.b);
		}
		else if(!aOnSegment && !bOnSegment)
		{
			LineSegment3D newShortestConnection = new LineSegment3D(this.a, other.a);
			double shortestDistanceSquared = this.a.distanceToSquared(other.a);

			if(this.a.distanceToSquared(other.b) < shortestDistanceSquared)
			{
				shortestDistanceSquared = this.a.distanceToSquared(other.b);
				newShortestConnection = new LineSegment3D(this.a, other.b);
			}
			if(this.b.distanceToSquared(other.a) < shortestDistanceSquared)
			{
				shortestDistanceSquared = this.b.distanceToSquared(other.a);
				newShortestConnection = new LineSegment3D(this.b, other.a);
			}
			if(this.b.distanceToSquared(other.b) < shortestDistanceSquared)
				newShortestConnection = new LineSegment3D(this.b, other.b);

			shortestConnection = newShortestConnection;
		}

		return shortestConnection.getLength();
	}

}
