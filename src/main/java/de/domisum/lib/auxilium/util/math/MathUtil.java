package de.domisum.lib.auxilium.util.math;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtil
{

	// NUMBERS
	@API public static double getDelta(double a, double b)
	{
		return Math.abs(a-b);
	}

	@API public static double mix(double firstNumber, double firstPart, double secondNumber, double secondPart)
	{
		double firstPercentage = firstPart/(firstPart+secondPart);
		double secondPercentage = 1-firstPercentage;

		return (firstPercentage*firstNumber)+(secondPercentage*secondNumber);
	}

	@API public static double mix(double firstNumber, double firstPart, double secondNumber)
	{
		double secondPercentage = 1-firstPart;

		return (firstPart*firstNumber)+(secondPercentage*secondNumber);
	}

	@API public static double clampAbs(double number, double maximumAbs)
	{
		return (number < 0 ? -1 : 1)*Math.min(Math.abs(number), maximumAbs);
	}


	@API
	public static double remapLinear(double baseStart, double baseEnd, double targetStart, double targetEnd, double valueToRemap)
	{
		Validate.isTrue(baseStart != baseEnd, "baseStart can't be equal to baseEnd ("+baseStart+")");
		Validate.isTrue(targetStart != targetEnd, "targetStart can't be equal to targetEnd ("+targetStart+")");

		double proportionFrom1To2 = (valueToRemap-baseStart)/(baseEnd-baseStart);
		return targetStart+((targetEnd-targetStart)*proportionFrom1To2);
	}


	// ANGLE
	@API public static boolean isAngleNearRad(double a, double b, double maxD)
	{
		return isAngleNearDeg(Math.toDegrees(a), Math.toDegrees(b), maxD);
	}

	@API public static boolean isAngleNearDeg(double a, double b, double maxD)
	{
		double delta = Math.abs(a-b)%360;
		if(delta > 180)
			delta = 360-delta;

		return delta <= maxD;
	}


	/**
	 * Rounds a double to the specified number of decimal places.
	 * <p>
	 * Rounds values equal to or above .5 up (towards positive infinity), values below down (towards negative infinity). <br/>
	 * Example: Rounds 7.45 with 1 decimal place to 7.5; rounds -7.45 with 1 decimal place to -7.4
	 *
	 * @param numberToRound number to be rounded
	 * @param decimalPlaces the number of decimal places to round
	 * @return the rounded number
	 */
	@API public static double round(double numberToRound, int decimalPlaces)
	{
		int factor = 1;
		for(int i = 0; i < decimalPlaces; i++)
			factor *= 10;

		return (double) Math.round(numberToRound*factor)/factor;
	}


	// FUNCTION
	@API public static double smoothStep(double input)
	{
		// https://en.wikipedia.org/wiki/Smoothstep

		if(input <= 0)
			return 0;

		if(input >= 1)
			return 1;

		return 3*(input*input)-2*(input*input*input);
	}

}
