package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
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
		this.normal = normal.deriveNormalized();
	}
	
	@API
	public static Plane3D fromPointAndNormal(Vector3D point, Vector3D normal)
	{
		return new Plane3D(point, normal);
	}
	
	@API
	public static Plane3D throughPoints(Vector3D pointA, Vector3D pointB, Vector3D pointC)
	{
		var aToB = pointB.deriveSubtract(pointA);
		var aToC = pointC.deriveSubtract(pointA);
		var normal = aToB.deriveCrossProduct(aToC);
		
		if(normal.getLength() == 0)
			throw new IllegalArgumentException(PHR.r("can't create plane from points which are on the same line ({}, {}, {})",
				pointA,
				pointB,
				pointC
			));
		
		return fromPointAndNormal(pointA, normal);
	}
	
	
	// DISTANCE
	@API
	public double distanceTo(Vector3D point)
	{
		double distanceSigned = normal.dotProduct(this.point.deriveSubtract(point));
		return Math.abs(distanceSigned);
	}
	
}
