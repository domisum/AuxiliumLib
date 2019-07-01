package de.domisum.lib.auxilium.data.container.math;

import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;

@API
public class Plane3D
{

	// ATTRIBUTES
	@Getter
	private final Vector3D point;
	@Getter
	private final Vector3D normal;


	// INIT
	@API
	protected Plane3D(Vector3D point, Vector3D normal)
	{
		this.point = point;
		this.normal = normal.normalize();
	}

	@API
	public static Plane3D fromPointAndNormal(Vector3D point, Vector3D normal)
	{
		return new Plane3D(point, normal);
	}

	@API
	public static Plane3D throughPoints(Vector3D pointA, Vector3D pointB, Vector3D pointC)
	{
		Vector3D aToB = pointB.subtract(pointA);
		Vector3D aToC = pointC.subtract(pointA);
		Vector3D normal = aToB.crossProduct(aToC);

		if(normal.length() == 0)
			throw new IllegalArgumentException(PHR.r("can't create plane from points which are on the same line ({}, {}, {})",
					pointA,
					pointB,
					pointC
			));

		return fromPointAndNormal(pointA, normal);
	}


	// DISTANCE
	public double distanceTo(Vector3D point)
	{
		double distanceSigned = normal.dotProduct(this.point.subtract(point));
		return Math.abs(distanceSigned);
	}

}
