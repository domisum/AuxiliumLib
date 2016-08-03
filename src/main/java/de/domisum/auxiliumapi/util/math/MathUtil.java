package de.domisum.auxiliumapi.util.math;

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


	// HELPER
	public static double round(double number, int digits)
	{
		int factor = 1;
		for(int i = 0; i < digits; i++)
			factor *= 10;

		return (double) Math.round(number * factor) / factor;
	}

}
