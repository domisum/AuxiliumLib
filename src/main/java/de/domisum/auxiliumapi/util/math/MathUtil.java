package de.domisum.auxiliumapi.util.math;

import de.domisum.auxiliumapi.data.container.math.Vector2D;
import de.domisum.auxiliumapi.data.container.math.Vector3D;

public class MathUtil
{

	// NUMBERS
	public static double getDelta(double a, double b)
	{
		return Math.abs(a - b);
	}

	public static double mix(double firstNumber, double firstPart, double secondNumber, double secondPart)
	{
		double firstPercentage = firstPart / (firstPart + secondPart);
		double secondPercentage = 1 - firstPercentage;

		return (firstPercentage * firstNumber) + (secondPercentage * secondNumber);
	}

	public static double mix(double firstNumber, double firstPart, double secondNumber)
	{
		double firstPercentage = firstPart;
		double secondPercentage = 1 - firstPercentage;

		return (firstPercentage * firstNumber) + (secondPercentage * secondNumber);
	}


	// ANGLE
	public static boolean isAngleNear(double a, double b, double maxD)
	{
		double delta = Math.abs(a - b) % (2 * Math.PI);
		if(delta > Math.PI)
			delta = (2 * Math.PI) - delta;

		return delta <= maxD;
	}


	// VECTOR
	public static Vector2D getDirectionVector(double yaw)
	{
		double dX = -Math.sin(yaw);
		double dZ = Math.cos(yaw);

		return new Vector2D(dX, dZ);
	}

	public static Vector3D getCenter(Vector3D vector)
	{
		return new Vector3D(Math.floor(vector.x) + .5, Math.floor(vector.y) + .5, Math.floor(vector.z) + .5);
	}


	public static double getDistanceFromLineToPoint(Vector2D l1, Vector2D l2, Vector2D p)
	{
		// is this even right? may be just for line segment instead of infinite line

		double normalLength = Math.sqrt(((l2.x - l1.x) * (l2.x - l1.x)) + ((l2.y - l1.y) * (l2.y - l1.y)));
		return Math.abs(((p.x - l1.x) * (l2.y - l1.y)) - ((p.y - l1.y) * (l2.x - l1.x))) / normalLength;
	}

	public static double getDistanceFromLineToPoint(Vector3D l1, Vector3D l2, Vector3D p)
	{
		// http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html

		Vector3D oneToTwo = l2.subtract(l1);
		Vector3D pToOne = l1.subtract(p);

		Vector3D crossProduct = oneToTwo.crossProduct(pToOne);

		double distance = crossProduct.length() / oneToTwo.length();
		return distance;
	}


	// HELPER
	public static double round(double number, int digits)
	{
		int factor = 1;
		for(int i = 0; i < digits; i++)
			factor *= 10;

		return (double) Math.round(number * factor) / factor;
	}

}
