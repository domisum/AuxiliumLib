package de.domisum.lib.auxilium.util.math;

import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public class PhysicsUtil
{

	@API public static double getTimeFromDistanceAndAcceleration(double distance, double acceleration)
	{
		// s = 0.5 * a * t^2
		// t^2 = 2s / a
		// t = sqrt(2s/a)

		return Math.sqrt((2*distance)/acceleration);
	}

}
