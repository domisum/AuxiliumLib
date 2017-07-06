package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.data.container.bound.DoubleBounds2D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@APIUsage
public class Polygon2D
{

	// data
	@APIUsage public final List<Vector2D> points;

	private List<LineSegment2D> lines;
	private DoubleBounds2D boundingBox;
	private Vector2D pointCenter;


	// INIT
	@APIUsage public Polygon2D(List<Vector2D> points)
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
	@APIUsage public List<LineSegment2D> getLines()
	{
		if(this.lines != null)
			return this.lines;

		this.lines = new ArrayList<>();

		Vector2D last = null;
		for(Vector2D v : this.points)
		{
			if(last != null)
				this.lines.add(new LineSegment2D(last, v));

			last = v;
		}
		this.lines.add(new LineSegment2D(last, this.points.get(0)));

		return this.lines;
	}

	@APIUsage public DoubleBounds2D getBoundingBox()
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

	@APIUsage public Vector2D getPointCenter()
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
	@APIUsage public boolean contains(Vector2D point)
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

			if(ray.contains(p))
				if(ray.isColinear(line))
					intersections--;
		}

		return intersections%2 == 1;
	}

	@APIUsage public boolean overlaps(Polygon2D other)
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


	// CALCULATIONS
	@APIUsage public double getArea()
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

	@APIUsage public double getDistanceTo(Vector2D point)
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

	@APIUsage public double getDistanceTo(Polygon2D other)
	{
		if(overlaps(other))
			return 0;

		return Math.min(getPointsDistanceTo(this, other), getPointsDistanceTo(other, this));
	}


	// HELPER
	private double getPointsDistanceTo(Polygon2D pointPolygon, Polygon2D polygon)
	{
		//noinspection ConstantConditions
		return pointPolygon.points.stream().mapToDouble(polygon::getDistanceTo).min().getAsDouble();
	}

}