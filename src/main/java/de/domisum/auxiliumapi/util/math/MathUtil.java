package de.domisum.auxiliumapi.util.math;

public class MathUtil
{

	public static double round(double number, int digits)
	{
		int factor = 1;
		for(int i = 0; i < digits; i++)
			factor *= 10;

		return (double) Math.round(number * factor) / factor;
	}

}
