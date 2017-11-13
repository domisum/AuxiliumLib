package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.data.container.bound.DoubleBounds2D;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@API
public class Polygon2D
{

	// data
	@API public final List<Vector2D> points;

	// temp
	private List<LineSegment2D> lines;
	private List<PolygonCorner> corners;
	private DoubleBounds2D boundingBox;
	private Vector2D pointCenter;

	private Boolean clockwise; // using Boolean so it can be null before it has been determined


	// INIT
	@API public Polygon2D(List<Vector2D> points)
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
	@Override public String toString()
	{
		return "Polygon2D{"+"points="+this.points+'}';
	}


	// GETTERS
	@API public List<LineSegment2D> getLines()
	{
		if(this.lines == null)
		{
			this.lines = new ArrayList<>();

			Vector2D last = null;
			for(Vector2D v : this.points)
			{
				if(last != null)
					this.lines.add(new LineSegment2D(last, v));

				last = v;
			}
			this.lines.add(new LineSegment2D(last, this.points.get(0)));

			this.lines = Collections.unmodifiableList(this.lines);
		}

		return this.lines;
	}

	@API public List<PolygonCorner> getCorners()
	{
		if(this.corners == null)
		{
			this.corners = new ArrayList<>();

			// start with last linesegment
			LineSegment2D before = getLines().get(getLines().size()-1);
			for(LineSegment2D ls : getLines())
			{
				double angleDeg = before.getDirection().getAngleToDeg(ls.getDirection());

				boolean convex = (angleDeg < 0)^isClockwise();
				PolygonCornerOrientation orientation = convex ?
						PolygonCornerOrientation.CONVEX :
						PolygonCornerOrientation.CONCAVE;
				this.corners.add(new PolygonCorner(angleDeg, orientation));

				before = ls;
			}

			this.corners = Collections.unmodifiableList(this.corners);
		}

		return this.corners;
	}

	@API public DoubleBounds2D getBoundingBox()
	{
		if(this.boundingBox != null)
			return this.boundingBox;

		double minX = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;

		for(Vector2D p : this.points)
		{
			if(p.x < minX)
				minX = p.x;
			if(p.x > maxX)
				maxX = p.x;

			if(p.y < minY)
				minY = p.y;
			if(p.y > maxY)
				maxY = p.y;
		}

		this.boundingBox = new DoubleBounds2D(minX, maxX, minY, maxY);
		return this.boundingBox;
	}

	@API public Vector2D getPointCenter()
	{
		if(this.pointCenter == null)
		{
			Vector2D pointSum = new Vector2D();
			for(Vector2D p : this.points)
				pointSum = pointSum.add(p);

			this.pointCenter = pointSum.divide(this.points.size());
		}

		return this.pointCenter;
	}


	// CHECKS
	@API public boolean contains(Vector2D point)
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

		Vector2D pointOutside = new Vector2D(boundingBox.minX-1, boundingBox.minY-1);
		LineSegment2D ray = new LineSegment2D(pointOutside, point);

		int intersections = 0;
		for(LineSegment2D ls : lines)
			if(ray.intersects(ls))
				intersections++;

		for(int i = 0; i < this.points.size(); i++)
		{
			Vector2D p = this.points.get(i);
			LineSegment2D line = getLines().get(i);

			if(ray.contains(p) && ray.isColinear(line))
				intersections--;
		}

		return intersections%2 == 1;
	}

	@API public boolean overlaps(Polygon2D other)
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
		if(other.contains(this.points.get(0)))
			return true;

		// if none of the above, the polygons dont overlap
		return false;
	}

	@API public boolean isClockwise()
	{
		if(this.clockwise != null)
			return this.clockwise;

		// https://stackoverflow.com/a/1165943/4755690

		double sum = 0;
		for(LineSegment2D ls : getLines())
			sum += (ls.b.x-ls.a.x)*(ls.a.y+ls.b.y);

		this.clockwise = sum > 0;
		return this.clockwise;
	}


	// CALCULATIONS
	@API public double getArea()
	{
		double sum = 0;

		Vector2D last = this.points.get(this.points.size()-1);
		for(Vector2D p : this.points)
		{
			sum += last.x*p.y-p.x*last.y;

			last = p;
		}

		return Math.abs(sum/2);
	}

	@API public double getDistanceTo(Vector2D point)
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

	@API public double getDistanceTo(Polygon2D other)
	{
		if(overlaps(other))
			return 0;

		return Math.min(getPointsDistanceTo(this, other), getPointsDistanceTo(other, this));
	}


	// SELF
	@API public Polygon2D move(Vector2D movement)
	{
		List<Vector2D> movedPoints = new ArrayList<>();
		for(Vector2D p : this.points)
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
		public PolygonCorner(double angleDeg, PolygonCornerOrientation orientation)
		{
			this.angleDegAbs = Math.abs(angleDeg);
			this.orientation = orientation;
		}

	}

	public enum PolygonCornerOrientation
	{
		CONCAVE, CONVEX
	}

}
