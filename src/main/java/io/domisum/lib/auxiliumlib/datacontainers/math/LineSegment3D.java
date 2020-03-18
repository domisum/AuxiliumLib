package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@API
@AllArgsConstructor
public final class LineSegment3D
{

	// PROPERTIES
	@API
	@Getter
	private final Vector3D a;
	@API
	@Getter
	private final Vector3D b;


	// CONVERSION
	@API
	public Line3D toLine()
	{
		return new Line3D(a, b.subtract(a));
	}

	@API
	public LineSegment3D getShortenedBothEnds(double distance)
	{
		Vector3D newA = a.moveTowards(b, distance);
		Vector3D newB = b.moveTowards(a, distance);

		return new LineSegment3D(newA, newB);
	}


	// OBJECT
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if((o == null) || (getClass() != o.getClass()))
			return false;

		LineSegment3D that = (LineSegment3D) o;
		boolean defaultWay = a.equals(that.a) && b.equals(that.b);
		boolean invertedWay = a.equals(that.b) && b.equals(that.a);
		return defaultWay || invertedWay;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(a)+Objects.hash(b);
	}


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
	public boolean containsPoint(Vector3D point)
	{
		if(!toLine().containsPoint(point))
			return false;

		double delta = Math.abs((point.distanceTo(a)+point.distanceTo(b))-getLength());
		return delta < Line3D.THRESHOLD;
	}


	// DISTANCE
	@API
	public double getDistanceTo(Vector3D point)
	{
		// http://geomalgorithms.com/a02-_lines.html

		Vector3D v = b.subtract(a);
		Vector3D w = point.subtract(a);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct <= 0)
			return point.distanceTo(a);
		if(v.dotProduct(v) <= wvProduct)
			return point.distanceTo(b);

		double productQuot = wvProduct/vvProduct;
		Vector3D pointOnSegment = a.add(v.multiply(productQuot));

		return point.distanceTo(pointOnSegment);
	}

	@API
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
			Vector3D newA = a;
			if(shortestConnection.b.distanceToSquared(b) < shortestConnection.b.distanceToSquared(newA))
				newA = b;

			shortestConnection = new LineSegment3D(newA, shortestConnection.b);
		}
		else if(!aOnSegment) // && !bOnSegment
		{
			LineSegment3D newShortestConnection = new LineSegment3D(a, other.a);
			double shortestDistanceSquared = a.distanceToSquared(other.a);

			if(a.distanceToSquared(other.b) < shortestDistanceSquared)
			{
				shortestDistanceSquared = a.distanceToSquared(other.b);
				newShortestConnection = new LineSegment3D(a, other.b);
			}
			if(b.distanceToSquared(other.a) < shortestDistanceSquared)
			{
				shortestDistanceSquared = b.distanceToSquared(other.a);
				newShortestConnection = new LineSegment3D(b, other.a);
			}
			if(b.distanceToSquared(other.b) < shortestDistanceSquared)
				newShortestConnection = new LineSegment3D(b, other.b);

			shortestConnection = newShortestConnection;
		}

		return shortestConnection.getLength();
	}

}