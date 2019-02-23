package uk.co.ryanharrison.mathengine.special;

import java.math.BigDecimal;

/**
 * Class representing the value of PI, allowing its evaluation to a certain
 * number of decimal digits
 * 
 * @author Ryan Harrison
 * 
 */
public final class Pi
{
	/** Constant used in the PI computation */
	private static final BigDecimal FOUR = BigDecimal.valueOf(4);

	/** Rounding mode to use during PI computation */
	private static final int roundingMode = BigDecimal.ROUND_HALF_EVEN;

	/** Digits of precision after the decimal point */
	private final int digits;

	/**
	 * Construct a task to calculate pi to the specified precision.
	 */
	/**
	 * Construct a task to calculate PI to the specified precision
	 * 
	 * @param digits
	 *            The number of decimal digits of precision
	 */
	public Pi(int digits)
	{
		this.digits = digits;
	}

	/**
	 * Calculate the value of PI to the specified number of decimal digits
	 */
	public BigDecimal execute()
	{
		return computePi(digits);
	}

	/**
	 *  
	 */

	/**
	 * Compute the value, in radians, of the arctangent of the inverse of the
	 * supplied integer to the specified number of digits after the decimal
	 * point.
	 * 
	 * The value is computed using the power series expansion for the arc
	 * tangent: arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + (x^9)/9 ...
	 * 
	 * @param inverseX
	 *            The value to calculate
	 * @param scale
	 *            The number of digits after the decimal point
	 * @return
	 */
	private static BigDecimal arctan(int inverseX, int scale)
	{
		BigDecimal result, numer, term;
		BigDecimal invX = BigDecimal.valueOf(inverseX);
		BigDecimal invX2 = BigDecimal.valueOf(inverseX * inverseX);

		numer = BigDecimal.ONE.divide(invX, scale, roundingMode);

		result = numer;
		int i = 1;
		do
		{
			numer = numer.divide(invX2, scale, roundingMode);
			int denom = 2 * i + 1;
			term = numer.divide(BigDecimal.valueOf(denom), scale, roundingMode);
			if (i % 2 != 0)
			{
				result = result.subtract(term);
			}
			else
			{
				result = result.add(term);
			}
			i++;
		}
		while (term.compareTo(BigDecimal.ZERO) != 0);
		return result;
	}

	/**

	 */

	/**
	 * Compute the value of pi to the specified number of digits after the
	 * decimal point.
	 * 
	 * The value is computed using Machin's formula: pi/4 = 4*arctan(1/5) -
	 * arctan(1/239) and a power series expansion of arctan(x) to sufficient
	 * precision.
	 * 
	 * @param digits
	 *            The number of digits of precision after the decimal point
	 * @return The value of PI to the specified number of decimal digits
	 */
	public static BigDecimal computePi(int digits)
	{
		int scale = digits + 5;
		BigDecimal arctan1_5 = arctan(5, scale);
		BigDecimal arctan1_239 = arctan(239, scale);
		BigDecimal pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);
		return pi.setScale(digits, BigDecimal.ROUND_HALF_UP);
	}
}
