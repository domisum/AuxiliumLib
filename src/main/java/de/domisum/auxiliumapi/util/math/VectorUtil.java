package de.domisum.auxiliumapi.util.math;

import de.domisum.auxiliumapi.data.container.math.Vector2D;
import de.domisum.auxiliumapi.data.container.math.Vector3D;

public class VectorUtil
{

	// MISC
	public static double getYawFromDirection(Vector3D direction)
	{
		return Math.toDegrees(Math.atan2(direction.z, direction.x))-90;
	}

	public static Vector2D getDirectionVector(double yaw)
	{
		double dX = -Math.sin(yaw);
		double dZ = Math.cos(yaw);

		return new Vector2D(dX, dZ);
	}

	public static Vector3D getCenter(Vector3D vector)
	{
		return new Vector3D(Math.floor(vector.x)+.5, Math.floor(vector.y)+.5, Math.floor(vector.z)+.5);
	}


	// ROTATION
	public static Vector3D rotateOnXZPlane(Vector3D vector, double degrees)
	{
		double rad = Math.toRadians(-degrees);

		double nX = (vector.x*Math.cos(rad))-(vector.z*Math.sin(rad));
		double nZ = (vector.x*Math.sin(rad))+(vector.z*Math.cos(rad));

		return new Vector3D(nX, vector.y, nZ);
	}


	// DISTANCES
	public static Vector3D getPointOnLineClosestToPoint(Vector3D l1, Vector3D l2, Vector3D p)
	{
		Vector3D v = l2.subtract(l1);
		Vector3D w = p.subtract(l1);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);

		double productQuot = wvProduct/vvProduct;
		Vector3D pointOnLine = l1.add(v.multiply(productQuot));
		return pointOnLine;
	}


	public static double getDistanceFromLineToPoint(Vector2D l1, Vector2D l2, Vector2D p)
	{
		// is this even right? may be just for line segment instead of infinite line

		double normalLength = Math.sqrt(((l2.x-l1.x)*(l2.x-l1.x))+((l2.y-l1.y)*(l2.y-l1.y)));
		return Math.abs(((p.x-l1.x)*(l2.y-l1.y))-((p.y-l1.y)*(l2.x-l1.x)))/normalLength;
	}

	public static double getDistanceFromLineToPoint(Vector3D l1, Vector3D l2, Vector3D p)
	{
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html

		Vector3D oneToTwo = l2.subtract(l1);
		Vector3D pToOne = l1.subtract(p);

		Vector3D crossProduct = oneToTwo.crossProduct(pToOne);

		double distance = crossProduct.length()/oneToTwo.length();
		return distance;
	}

	public static double getDistanceFromSegmentToPoint(Vector3D l1, Vector3D l2, Vector3D p)
	{
		// http://geomalgorithms.com/a02-_lines.html

		Vector3D v = l2.subtract(l1);
		Vector3D w = p.subtract(l1);

		double wvProduct = w.dotProduct(v);
		double vvProduct = v.dotProduct(v);
		if(wvProduct <= 0)
			return p.distanceTo(l1);
		if(v.dotProduct(v) <= wvProduct)
			return p.distanceTo(l2);

		double productQuot = wvProduct/vvProduct;
		Vector3D pointOnSegment = l1.add(v.multiply(productQuot));

		return p.distanceTo(pointOnSegment);
	}

}
