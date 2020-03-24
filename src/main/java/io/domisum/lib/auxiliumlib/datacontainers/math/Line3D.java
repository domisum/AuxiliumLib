package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;

@API
public class Line3D
{
	
	// CONSTANTS
	public static final double THRESHOLD = 1d/(1000d*1000d*1000d);
	
	// PROPERTIES
	@Getter
	private final Vector3D base;
	@Getter
	private final Vector3D direction;
	
	
	// INIT
	@API
	public Line3D(Vector3D base, Vector3D direction)
	{
		this.base = base;
		this.direction = direction.deriveNormalized();
	}
	
	
	// GETTERS
	@API
	public boolean containsPoint(Vector3D point)
	{
		var crossProduct = direction.deriveCrossProduct(point.deriveSubtract(base));
		return crossProduct.lengthSquared()<=THRESHOLD;
	}
	
	@API
	public Vector3D getPointOnLineClosestToPoint(Vector3D point)
	{
		var w = point.deriveSubtract(base);
		
		double wvProduct = w.dotProduct(direction);
		double vvProduct = direction.dotProduct(direction);
		
		double productQuot = wvProduct/vvProduct;
		return base.deriveAdd(direction.deriveMultiply(productQuot));
	}
	
	@API
	public LineSegment3D getShortestConnection(Line3D other)
	{
		// http://geomalgorithms.com/a07-_distance.html#dist3D_Segment_to_Segment
		
		var w0 = base.deriveSubtract(other.base);
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
		
		var point1 = base.deriveAdd(direction.deriveMultiply(xp));
		var point2 = other.base.deriveAdd(other.direction.deriveMultiply(xq));
		return new LineSegment3D(point1, point2);
	}
	
	
	// DISTANCE
	@API
	public double getDistanceTo(Vector3D point)
	{
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
		
		var pToOne = base.deriveSubtract(point);
		var crossProduct = direction.deriveCrossProduct(pToOne);
		return crossProduct.length()/direction.length();
	}
	
	@API
	public double getDistanceTo(Line3D other)
	{
		var lineSegment = getShortestConnection(other);
		return lineSegment.getLength();
	}
	
}
