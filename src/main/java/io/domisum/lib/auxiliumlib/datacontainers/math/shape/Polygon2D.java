package io.domisum.lib.auxiliumlib.datacontainers.math.shape;

import io.domisum.lib.auxiliumlib.datacontainers.bound.DoubleBounds2D;
import io.domisum.lib.auxiliumlib.datacontainers.math.LineSegment2D;
import io.domisum.lib.auxiliumlib.datacontainers.math.Vector2D;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@API
public class Polygon2D implements GeometricShape2D
{

	// data
	@Getter
	private final List<Vector2D> points;

	// lazy values for better performance
	private List<LineSegment2D> lines;
	private List<PolygonCorner> corners;
	private DoubleBounds2D boundingBox;
	private Vector2D pointCenter;

	private Boolean clockwise; // using Boolean so it can be null before it has been determined


	// INIT
	@API
	public Polygon2D(List<Vector2D> points)
	{
		if(points.size() <= 2)
			throw new IllegalArgumentException("A polygon has to have at least 3 points");

		this.points = Collections.unmodifiableList(points);
	}

	public Polygon2D(Vector2D... points)
	{
		this(Arrays.asList(points));
	}


	// OBJECT
	@Override
	public String toString()
	{
		return "Polygon2D{"+"points="+points+'}';
	}


	// GETTERS
	@API
	public List<LineSegment2D> getLines()
	{
		if(lines == null)
		{
			lines = new ArrayList<>();

			Vector2D last = null;
			for(Vector2D v : points)
			{
				if(last != null)
					lines.add(new LineSegment2D(last, v));

				last = v;
			}
			lines.add(new LineSegment2D(last, points.get(0)));

			lines = Collections.unmodifiableList(lines);
		}

		return Collections.unmodifiableList(lines);
	}

	@API
	public List<PolygonCorner> getCorners()
	{
		if(corners == null)
		{
			corners = new ArrayList<>();

			// start with last linesegment
			LineSegment2D before = getLines().get(getLines().size()-1);
			for(LineSegment2D ls : getLines())
			{
				double angleDeg = before.getDirection().getAngleToDeg(ls.getDirection());

				boolean convex = (angleDeg < 0)^isClockwise();
				PolygonCornerOrientation orientation = convex ?
						PolygonCornerOrientation.CONVEX :
						PolygonCornerOrientation.CONCAVE;
				corners.add(new PolygonCorner(angleDeg, orientation));

				before = ls;
			}

			corners = Collections.unmodifiableList(corners);
		}

		return Collections.unmodifiableList(corners);
	}

	@API
	public DoubleBounds2D getBoundingBox()
	{
		if(boundingBox != null)
			return boundingBox;

		double minX = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;

		for(Vector2D p : points)
		{
			if(p.getX() < minX)
				minX = p.getX();
			if(p.getX() > maxX)
				maxX = p.getX();

			if(p.getY() < minY)
				minY = p.getY();
			if(p.getY() > maxY)
				maxY = p.getY();
		}

		boundingBox = new DoubleBounds2D(minX, maxX, minY, maxY);
		return boundingBox;
	}

	@API
	public Vector2D getPointCenter()
	{
		if(pointCenter == null)
		{
			Vector2D pointSum = new Vector2D();
			for(Vector2D p : points)
				pointSum = pointSum.add(p);

			pointCenter = pointSum.divide(points.size());
		}

		return pointCenter;
	}


	// CHECKS
	@Override
	@API
	public boolean contains(Vector2D point)
	{
		DoubleBounds2D boundingBox = getBoundingBox();
		List<LineSegment2D> lines = getLines();

		// if outside bounding box, cant be inside polygon
		if(!boundingBox.contains(point))
			return false;

		// if the point is on one of the bounding lines, it is contained in the polygon
		for(LineSegment2D ls : lines)
			if(ls.contains(point))
				return true;

		Vector2D pointOutside = new Vector2D(boundingBox.getMinX()-1, boundingBox.getMinY()-1);
		LineSegment2D ray = new LineSegment2D(pointOutside, point);

		int intersections = 0;
		for(LineSegment2D ls : lines)
			if(ray.intersects(ls))
				intersections++;

		for(LineSegment2D line : getLines())
			if(line.isColinear(ray))
				intersections--;

		for(Vector2D vector2D : getPoints())
			if(ray.contains(vector2D))
				intersections--;

		return (intersections%2) == 1;
	}

	@API
	public boolean overlaps(Polygon2D other)
	{
		// do lines intersect? if yes, the polygons overlap
		for(LineSegment2D lineSegment2D : getLines())
			for(LineSegment2D ls : other.getLines())
				if(lineSegment2D.intersects(ls))
					return true;

		// is a point of the other polygon contained in this polygon? if yes, the polygons overlap
		if(contains(other.points.get(0)))
			return true;

		// is a point of this polygon contained in the other polygon? if yes, the polygons overlap
		if(other.contains(points.get(0)))
			return true;

		// if none of the above, the polygons dont overlap
		return false;
	}

	@API
	public boolean isClockwise()
	{
		if(clockwise != null)
			return clockwise;

		// https://stackoverflow.com/a/1165943/4755690

		double sum = 0;
		for(LineSegment2D ls : getLines())
			sum += (ls.b.getX()-ls.a.getX())*(ls.a.getY()+ls.b.getY());

		clockwise = sum > 0;
		return clockwise;
	}


	// CALCULATIONS
	@API
	public double getArea()
	{
		double sum = 0;

		Vector2D last = points.get(points.size()-1);
		for(Vector2D p : points)
		{
			sum += (last.getX()*p.getY())-(p.getX()*last.getY());
			last = p;
		}

		return Math.abs(sum/2);
	}

	@API
	public double getDistanceTo(Vector2D point)
	{
		if(contains(point))
			return 0;

		double minDistance = Double.MAX_VALUE;

		for(LineSegment2D lineSegment2D : getLines())
		{
			double distance = lineSegment2D.getDistanceTo(point);
			if(distance < minDistance)
				minDistance = distance;
		}

		return minDistance;
	}

	@API
	public double getDistanceTo(Polygon2D other)
	{
		if(overlaps(other))
			return 0;

		return Math.min(getPointsDistanceTo(this, other), getPointsDistanceTo(other, this));
	}


	// SELF
	@API
	public Polygon2D move(Vector2D movement)
	{
		List<Vector2D> movedPoints = new ArrayList<>();
		for(Vector2D p : points)
			movedPoints.add(p.add(movement));

		return new Polygon2D(movedPoints);
	}


	// HELPER
	private double getPointsDistanceTo(Polygon2D pointPolygon, Polygon2D polygon)
	{
		//noinspection ConstantConditions
		return pointPolygon.points.stream().mapToDouble(polygon::getDistanceTo).min().getAsDouble();
	}


	// POLYGON CORNER
	@ToString
	public static class PolygonCorner
	{

		public final double angleDegAbs;
		public final PolygonCornerOrientation orientation;


		// INIT
		protected PolygonCorner(double angleDeg, PolygonCornerOrientation orientation)
		{
			angleDegAbs = Math.abs(angleDeg);
			this.orientation = orientation;
		}

	}

	public enum PolygonCornerOrientation
	{
		CONCAVE,
		CONVEX
	}

}
