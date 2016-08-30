package de.domisum.auxiliumapi.util.math;

import de.domisum.auxiliumapi.data.container.math.Vector2D;
import de.domisum.auxiliumapi.data.container.math.Vector3D;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

@APIUsage
public class VectorUtil
{

	// MISC
	@APIUsage
	public static Vector2D getDirectionVector(double yaw)
	{
		double dX = -Math.sin(yaw);
		double dZ = Math.cos(yaw);

		return new Vector2D(dX, dZ);
	}


	// MINECRAFT
	@APIUsage
	public static double getYawFromDirection(Vector3D direction)
	{
		return Math.toDegrees(Math.atan2(direction.z, direction.x))-90;
	}

	@APIUsage
	public static Vector3D getCenter(Vector3D vector)
	{
		return new Vector3D(Math.floor(vector.x)+.5, Math.floor(vector.y)+.5, Math.floor(vector.z)+.5);
	}

	@APIUsage
	public static Vector3D convertOffsetToMinecraftCoordinates(Vector3D offset)
	{
		return new Vector3D(-offset.x, offset.y, -offset.z);
	}


	// ROTATION
	@APIUsage
	public static Vector3D rotateOnXZPlane(Vector3D vector, double degrees)
	{
		double rad = Math.toRadians(-degrees);

		double nX = (vector.x*Math.cos(rad))-(vector.z*Math.sin(rad));
		double nZ = (vector.x*Math.sin(rad))+(vector.z*Math.cos(rad));

		return new Vector3D(nX, vector.y, nZ);
	}


	// DISTANCES
	@APIUsage
	public static double getDistanceFromLineToPoint(Vector2D l1, Vector2D l2, Vector2D p)
	{
		// is this even right? may be just for line segment instead of infinite line

		double normalLength = Math.sqrt(((l2.x-l1.x)*(l2.x-l1.x))+((l2.y-l1.y)*(l2.y-l1.y)));
		return Math.abs(((p.x-l1.x)*(l2.y-l1.y))-((p.y-l1.y)*(l2.x-l1.x)))/normalLength;
	}

}
