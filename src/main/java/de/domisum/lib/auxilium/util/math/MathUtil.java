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


	@APIUsage
	public static double remapLinear(double from1, double from2, double to1, double to2, double value)
	{
		if(from2 < from1)
			throw new IllegalArgumentException("from2 ("+from2+") can't be lower than from1 ("+from1+")");

		if(to2 < to1)
			throw new IllegalArgumentException("to2 ("+to2+") can't be lower than to1 ("+to1+")");

		double distToFrom1 = value-from1;
		double relativeClosenesTo1 = distToFrom1/(from2-from1);

		double distToTo1 = relativeClosenesTo1*(to2-to1);

		return to1+distToTo1;
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
