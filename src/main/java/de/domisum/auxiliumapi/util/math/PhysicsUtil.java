package de.domisum.auxiliumapi.util.math;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;

public class PhysicsUtil
{

	@APIUsage
	public static double getTimeFromDistanceAndAcceleration(double distance, double acceleration)
	{
		// s = 0.5 * a * t^2
		// t^2 = 2s / a
		// t = sqrt(2s/a)

		return Math.sqrt((2*distance)/acceleration);
	}

}
