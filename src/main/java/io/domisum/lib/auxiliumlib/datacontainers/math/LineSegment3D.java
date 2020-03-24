package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@API
@RequiredArgsConstructor
public final class LineSegment3D
{
	
	// PROPERTIES
	@Getter
	private final Vector3D pointA;
	@Getter
	private final Vector3D pointB;
	
	
	// CONVERSION
	@API
	public Line3D toLine()
	{
		return new Line3D(pointA, pointB.deriveSubtract(pointA));
	}
	
	@API
	public LineSegment3D deriveShortenedBothEnds(double distance)
	{
		var newA = pointA.deriveMovedTowards(pointB, distance);
		var newB = pointB.deriveMovedTowards(pointA, distance);
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
		
		var other = (LineSegment3D) o;
		boolean defaultWay = pointA.equals(other.pointA) && pointB.equals(other.pointB);
		boolean invertedWay = pointA.equals(other.pointB) && pointB.equals(other.pointA);
		return defaultWay || invertedWay;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(pointA)+Objects.hash(pointB);
	}
	
	
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
	public boolean containsPoint(Vector3D point)
	{
		if(!toLine().containsPoint(point))
			return false;
		double delta = Math.abs((point.distanceTo(pointA)+point.distanceTo(pointB))-getLength());
		return delta<Line3D.THRESHOLD;
	}
	
	
	// DISTANCE
	@API
	public double getDistanceTo(Vector3D point)
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
	public double getDistanceTo(LineSegment3D other)
	{
		var shortestConnection = toLine().getShortestConnection(other.toLine());
		boolean aOnSegment = containsPoint(shortestConnection.pointA);
		boolean bOnSegment = other.containsPoint(shortestConnection.pointB);
		
		// if(aOnLine && bOnLine) else
		if(aOnSegment && !bOnSegment)
		{
			var newB = other.pointA;
			if(shortestConnection.pointA.distanceToSquared(other.pointB)<shortestConnection.pointA.distanceToSquared(newB))
				newB = other.pointB;
			shortestConnection = new LineSegment3D(shortestConnection.pointA, newB);
		}
		else if(!aOnSegment && bOnSegment)
		{
			var newA = pointA;
			if(shortestConnection.pointB.distanceToSquared(pointB)<shortestConnection.pointB.distanceToSquared(newA))
				newA = pointB;
			shortestConnection = new LineSegment3D(newA, shortestConnection.pointB);
		}
		else if(!aOnSegment) // && !bOnSegment
		{
			var newShortestConnection = new LineSegment3D(pointA, other.pointA);
			double shortestDistanceSquared = pointA.distanceToSquared(other.pointA);
			
			if(pointA.distanceToSquared(other.pointB)<shortestDistanceSquared)
			{
				shortestDistanceSquared = pointA.distanceToSquared(other.pointB);
				newShortestConnection = new LineSegment3D(pointA, other.pointB);
			}
			if(pointB.distanceToSquared(other.pointA)<shortestDistanceSquared)
			{
				shortestDistanceSquared = pointB.distanceToSquared(other.pointA);
				newShortestConnection = new LineSegment3D(pointB, other.pointA);
			}
			if(pointB.distanceToSquared(other.pointB)<shortestDistanceSquared)
				newShortestConnection = new LineSegment3D(pointB, other.pointB);
			
			shortestConnection = newShortestConnection;
		}
		
		return shortestConnection.getLength();
	}
	
}
