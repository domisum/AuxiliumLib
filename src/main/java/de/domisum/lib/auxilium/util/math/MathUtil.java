package de.domisum.lib.auxilium.util.math;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;

public class MathUtil
{

	// NUMBERS
	@APIUsage
	public static double getDelta(double a, double b)
	{
		return Math.abs(a-b);
	}

	@APIUsage
	public static double mix(double firstNumber, double firstPart, double secondNumber, double secondPart)
	{
		double firstPercentage = firstPart/(firstPart+secondPart);
		double secondPercentage = 1-firstPercentage;

		return (firstPercentage*firstNumber)+(secondPercentage*secondNumber);
	}

	@APIUsage
	public static double mix(double firstNumber, double firstPart, double secondNumber)
	{
		double secondPercentage = 1-firstPart;

		return (firstPart*firstNumber)+(secondPercentage*secondNumber);
	}

	@APIUsage
	public static double clampAbs(double number, double maximumAbs)
	{
		return (number < 0 ? -1 : 1)*Math.min(Math.abs(number), maximumAbs);
	}


	// ANGLE
	@APIUsage
	public static boolean isAngleNearRad(double a, double b, double maxD)
	{
		return isAngleNearDeg(Math.toDegrees(a), Math.toDegrees(b), maxD);
	}

	@APIUsage
	public static boolean isAngleNearDeg(double a, double b, double maxD)
	{
		double delta = Math.abs(a-b)%360;
		if(delta > 180)
			delta = 360-delta;

		return delta <= maxD;
	}


	// HELPER
	@APIUsage
	public static double round(double number, int digits)
	{
		int factor = 1;
		for(int i = 0; i < digits; i++)
			factor *= 10;

		return (double) Math.round(number*factor)/factor;
	}

}
