package io.domisum.lib.auxiliumlib.util.math;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtil
{
	
	// NUMBERS
	@API
	public static double getDelta(double a, double b)
	{
		return Math.abs(a - b);
	}
	
	@API
	public static double mix(double firstNumber, double firstPart, double secondNumber, double secondPart)
	{
		double firstPercentage = firstPart / (firstPart + secondPart);
		double secondPercentage = 1 - firstPercentage;
		
		return (firstPercentage * firstNumber) + (secondPercentage * secondNumber);
	}
	
	@API
	public static double mix(double firstNumber, double firstPart, double secondNumber)
	{
		double secondPercentage = 1 - firstPart;
		
		return (firstPart * firstNumber) + (secondPercentage * secondNumber);
	}
	
	@API
	public static double clampAbs(double number, double maximumAbs)
	{
		return ((number < 0) ?
			-1 :
			1) * Math.min(Math.abs(number), maximumAbs);
	}
	
	@API
	public static double clamp(double min, double max, double value)
	{
		if(min > max)
			throw new IllegalArgumentException(PHR.r("min ({}) was bigger than max ({})", min, max));
		
		if(value < min)
			return min;
		
		if(value > max)
			return max;
		
		return value;
	}
	
	@API
	public static int clamp(int min, int max, int value)
	{
		if(min > max)
			throw new IllegalArgumentException(PHR.r("min ({}) was bigger than max ({})", min, max));
		
		if(value < min)
			return min;
		
		if(value > max)
			return max;
		
		return value;
	}
	
	@API
	public static double clampUpper(double max, double value)
	{
		return Math.min(value, max);
	}
	
	@API
	public static int clampUpper(int max, int value)
	{
		return Math.min(value, max);
	}
	
	@API
	public static double clampLower(double min, double value)
	{
		return Math.max(min, value);
	}
	
	@API
	public static double clampLower(int min, int value)
	{
		return Math.max(min, value);
	}
	
	
	@API
	public static double avg(double... values)
	{
		if(values.length == 0)
			throw new IllegalArgumentException("can't average calculate average of no numbers");
		
		double sum = 0;
		for(double value : values)
			sum += value;
		
		return sum / values.length;
	}
	
	
	@API
	public static double remapLinear(double baseStart, double baseEnd, double targetStart, double targetEnd, double valueToRemap)
	{
		Validate.isTrue(baseStart != baseEnd, "baseStart can't be equal to baseEnd (" + baseStart + ")");
		
		double proportionFrom1To2 = (valueToRemap - baseStart) / (baseEnd - baseStart);
		return targetStart + ((targetEnd - targetStart) * proportionFrom1To2);
	}
	
	
	// ANGLE
	@API
	public static boolean isAngleNearRad(double a, double b, double maxD)
	{
		return isAngleNearDeg(Math.toDegrees(a), Math.toDegrees(b), maxD);
	}
	
	@API
	public static boolean isAngleNearDeg(double a, double b, double maxD)
	{
		return getAngleDistanceDeg(a, b) < maxD;
	}
	
	@API
	public static double getAngleDistanceDeg(double a, double b)
	{
		double delta = Math.abs(a - b) % 360;
		if(delta > 180)
			delta = 360 - delta;
		
		return delta;
	}
	
	
	/**
	 * Rounds a double to the specified number of decimal places.
	 * <p>
	 * Rounds values equal to or above .5 up (towards positive infinity), values below down (towards negative infinity). <br/>
	 * Example: Rounds 7.45 with 1 decimal place to 7.5; rounds -7.45 with 1 decimal place to -7.4
	 *
	 * @param numberToRound number to be rounded
	 * @param decimalPlaces the number of decimal places to round to
	 * @return the rounded number
	 */
	@API
	public static double round(double numberToRound, int decimalPlaces)
	{
		if(Double.isNaN(numberToRound))
			return Double.NaN;
		
		double factor = 1;
		for(int i = 0; i < Math.abs(decimalPlaces); i++)
			if(decimalPlaces > 0)
				factor *= 10;
			else
				factor /= 10;
		
		return (double) Math.round(numberToRound * factor) / factor;
	}
	
	@API
	public static String roundString(double number, int precision)
	{
		if(precision <= 0)
			return Math.round(number) + "";
		
		String display = round(number, precision) + "";
		if(display.contains(".") && !display.contains("E"))
			while(display.length() - display.lastIndexOf(".") - 1 < precision)
				display += "0";
		
		return display;
	}
	
	@API
	public static String percentage(double fraction, int decimalPlaces)
	{
		return round(fraction * 100, decimalPlaces) + "%";
	}
	
	
	// FUNCTION
	@API
	public static double smoothStep(double input)
	{
		// https://en.wikipedia.org/wiki/Smoothstep
		
		if(input <= 0)
			return 0;
		
		if(input >= 1)
			return 1;
		
		// 3*x^2 - 2*x^3
		double a = 3 * (input * input);
		double b = 2 * (input * input * input);
		return a - b;
	}
	
	@API
	public static double smootherStep(double input)
	{
		// https://en.wikipedia.org/wiki/Smoothstep#Variations
		
		if(input <= 0)
			return 0;
		
		if(input >= 1)
			return 1;
		
		// 6*x^5 - 15*x^4 + 10*x^3
		double a = 6 * (input * input * input * input * input);
		double b = 15 * (input * input * input * input);
		double c = 10 * (input * input * input);
		return a - b + c;
	}
	
}
