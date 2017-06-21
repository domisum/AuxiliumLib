package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.data.container.bound.DoubleBounds2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Polygon2D
{

	// data
	public final List<Vector2D> points;

	// helper
	private List<LineSegment2D> lines;
	private DoubleBounds2D boundingBox;


	// INIT
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


	// GETTERS
	public List<LineSegment2D> getLines()
	{
		if(this.lines == null)
			determineLines();

		return new ArrayList<>(this.lines);
	}

	public DoubleBounds2D getBoundingBox()
	{
		if(this.boundingBox == null)
			determineBoundingBox();

		return this.boundingBox;
	}


	// CHECKS
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

		Vector2D pointOutside = new Vector2D(boundingBox.minX-1, point.y);
		LineSegment2D ray = new LineSegment2D(pointOutside, point);

		int intersections = 0;
		for(LineSegment2D ls : lines)
			if(ray.intersects(ls))
				intersections++;

		return intersections%2 == 1;
	}


	// HELPER
	private void determineLines()
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
	}

	private void determineBoundingBox()
	{
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
	}

}
