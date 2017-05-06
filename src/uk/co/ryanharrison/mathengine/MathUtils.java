package uk.co.ryanharrison.mathengine;

import java.math.BigDecimal;
import java.math.RoundingMode;

import uk.co.ryanharrison.mathengine.special.Gamma;

/**
 * Various useful mathematical functions and constants
 * 
 * @author Ryan Harrison
 * 
 */
public final class MathUtils
{
	/** The square root of PI */
	public static final double SQTPI = 2.50662827463100050242E0;

	/** The natural logarithm of PI */
	public static final double LOGPI = 1.14472988584940017414;

	/**
	 * Not permitted to make an instance of this class
	 */
	private MathUtils()
	{
	}

	/**
	 * Calculate the inverse cosecant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse cosecant of the angle
	 */
	public static double acosec(double a)
	{
		return Math.asin(1.0 / a);
	}

	/**
	 * Calculate the inverse hyperbolic cosecant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic cosecant of the angle
	 */
	public static double acosech(double a)
	{
		double sgn = 1.0D;
		if (a < 0.0D)
		{
			sgn = -1.0D;
			a = -a;
		}
		return 0.5D * sgn * Math.log(1.0 / a + Math.sqrt(1.0D / (a * a) + 1.0D));
	}

	/**
	 * Calculate the inverse hyperbolic cosine of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic cosine of the angle
	 */
	public static double acosh(double a)
	{
		return Math.log(a + Math.sqrt(a * a - 1.0D));
	}

	/**
	 * Calculate the inverse cotangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse cotangent of the angle
	 */
	public static double acot(double a)
	{
		return Math.atan(1.0D / a);
	}

	/**
	 * Calculate the inverse hyperbolic cotangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic cotangent of the angle
	 */
	public static double acoth(double a)
	{
		double sgn = 1.0D;
		if (a < 0.0D)
		{
			sgn = -1.0D;
			a = -a;
		}

		return 0.5D * sgn * (Math.log(1.0D + a) - Math.log(a - 1.0D));
	}

	/**
	 * Calculate the inverse secant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse secant of the angle
	 */
	public static double asec(double a)
	{
		return Math.acos(1.0 / a);
	}

	/**
	 * Calculate the inverse hyperbolic secant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic secant of the angle
	 */
	public static double asech(double a)
	{
		//return 0.5D * Math.log(1.0D / a + Math.sqrt(1.0D / (a * a) - 1.0D));
		return Math.log((Math.sqrt(1 - a * a) + 1) / a);
	}

	/**
	 * Calculate the inverse hyperbolic sine of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic sine of the angle
	 */
	public static double asinh(double a)
	{
		double sgn = 1.0D;
		if (a < 0.0D)
		{
			sgn = -1.0D;
			a = -a;
		}
		return sgn * Math.log(a + Math.sqrt(a * a + 1.0D));
	}

	/**
	 * Calculate the inverse hyperbolic tangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The inverse hyperbolic tangent of the angle
	 */
	public static double atanh(double a)
	{
		double sgn = 1.0D;
		if (a < 0.0D)
		{
			sgn = -1.0D;
			a = -a;
		}

		return 0.5D * sgn * (Math.log(1.0D + a) - Math.log(1.0D - a));
	}

	/**
	 * Calculate the number of subsets that can be selected from a set
	 * 
	 * @param z
	 *            The size of the set
	 * @param w
	 *            the size of the subsets to count
	 * @return z choose w
	 */
	public static double combination(double z, double w)
	{
		if (w >= 0)
		{
			if (z >= 0)
			{
				return Gamma.gamma(z + 1) / (Gamma.gamma(w + 1) * Gamma.gamma(z - w + 1));
			}
			else
			{
				if (w % 1 == 0)
				{
					return Math.pow(-1, w)
							* (factorial(w - z - 1) / (factorial(w) * factorial(-z - 1)));
				}
				else
				{
					return Double.POSITIVE_INFINITY;
				}
			}
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Calculate the compound interest of x over a certain numbers of years at a
	 * certain rate
	 * 
	 * @param x
	 *            The amount
	 * @param rate
	 *            The interest rate
	 * @param years
	 *            The number of years
	 * @return The compound interest of the specified number of years
	 */
	public static double compoundInterest(double x, double rate, double years)
	{
		return x * Math.pow(1 + rate / 100, years);
	}

	/**
	 * Calculate the cosecant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The cosecant of the angle
	 */
	public static double cosec(double a)
	{
		return 1.0D / Math.sin(a);
	}

	/**
	 * Calculate the hyperbolic cosecant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic cosecant of the angle
	 */
	public static double cosech(double a)
	{
		return 1.0D / sinh(a);
	}

	/**
	 * Calculate the hyperbolic cosine of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic cosine of the angle
	 */
	public static double cosh(double a)
	{
		return 0.5D * (Math.exp(a) + Math.exp(-a));
	}

	/**
	 * Calculate the cotangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The cotangent of the angle
	 */
	public static double cot(double a)
	{
		return 1.0D / Math.tan(a);
	}

	/**
	 * Calculate the hyperbolic cotangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic cotangent of the angle
	 */
	public static double coth(double a)
	{
		return 1.0D / tanh(a);
	}

	/**
	 * Calculate the double factorial of a double number
	 * 
	 * @param x
	 *            The number to use
	 * @return The double factorial of the double number x
	 */
	public static double doubleFactorial(double x)
	{
		double d = Math.abs(x);
		if (Math.floor(d) == d)
			return doubleFactorial((long) x);
		else
		{
			return Gamma.gamma((x / 2.0) + 1.0) * Math.pow(2, x / 2.0)
					* Math.pow(Math.PI / 2, 0.25 * (-1 + Math.cos(x * Math.PI)));
		}
	}

	/**
	 * Calculate the double factorial of a number
	 * 
	 * @param x
	 *            The number to use
	 * @return The double factorial of the number x
	 */
	public static double doubleFactorial(long x)
	{
		if (x == 0 || x == 1)
			return 1;

		double result = 1;

		for (int i = (int) x; i > 1; i -= 2)
		{
			result *= i;
		}

		return result;
	}

	/**
	 * Calculate the factorial of a double number
	 * 
	 * @param x
	 *            The number to use
	 * @return The factorial of the double number x
	 */
	public static double factorial(double x)
	{
		double d = Math.abs(x);
		if (Math.floor(d) == d)
			return factorial((long) x);
		else
			return Gamma.gamma(x + 1.0);
	}

	/**
	 * Calculate the factorial of a number
	 * 
	 * @param num
	 *            The number to use
	 * @return The factorial of the number x
	 */
	public static long factorial(long num)
	{
		long answer = 1;

		for (int i = 1; i <= num; i++)
		{
			answer *= i;
		}

		return answer;
	}

	/**
	 * Calculate the fractional part of a number
	 * 
	 * @param x
	 *            The number to use
	 * @return The fractional part of the number x
	 */
	public static double fPart(double x)
	{
		if (x <= 0)
		{
			return x - Math.floor(x);
		}
		else
		{
			return x - Math.ceil(x);
		}
	}

	/**
	 * Calculate the greatest common divisor of two numbers
	 * 
	 * @param xval
	 *            The first number
	 * @param yval
	 *            The second number
	 * @return The greatest common divisor of the two numbers
	 */
	public static int gcd(int xval, int yval)
	{
		while (yval != 0)
		{
			int temp = yval;
			yval = xval % yval;
			xval = temp;
		}
		return xval;
	}

	/**
	 * Calculate the greatest common factor of a data set
	 * 
	 * @param data
	 *            The data set to use
	 * @return The greatest common factor of the data set
	 */
	public static long greatestCommonFactor(int[] data)
	{
		int n, i, j, c = 0, minNumber;

		minNumber = data[0];
		n = data.length;

		for (i = 0; i < n; i++)
			if (data[i] < minNumber)
				minNumber = data[i];

		for (i = minNumber; i > 0; i--)
		{
			if (minNumber % i == 0)
			{
				c = 0;
				for (j = 0; j < n; j++)
					if (data[j] % i == 0)
						c += 1;
			}
			if (c == n)
			{
				return i;
			}
		}

		return 0;
	}

	/**
	 * Calculate the hypotenuse of a triangle with sides a and b
	 * 
	 * @param a
	 *            The first side
	 * @param b
	 *            The second side
	 * @return The length of the hypotenuse of a triangle with sides a and b
	 */
	public static double hypot(double a, double b)
	{
		double r;

		if (Math.abs(a) > Math.abs(b))
		{
			r = b / a;
			r = Math.abs(a) * Math.sqrt(1 + r * r);
		}
		else if (b != 0)
		{
			r = a / b;
			r = Math.abs(b) * Math.sqrt(1 + r * r);
		}
		else
		{
			r = 0.0;
		}

		return r;
	}

	/**
	 * Determines whether or not two numbers are equal to each other within a
	 * certain limit
	 * 
	 * @param x
	 *            The first number
	 * @param y
	 *            The second number
	 * @param limit
	 *            The limit to use
	 * @return True if the numbers are equal within the specified limit,
	 *         otherwise false
	 */
	public static boolean isEqualWithinLimits(double x, double y, double limit)
	{
		boolean test = false;
		if (Math.abs(x - y) <= Math.abs(limit))
			test = true;
		return test;
	}

	/**
	 * Determines whether or not two numbers are equal to each other within a
	 * certain percentage limit
	 * 
	 * @param x
	 *            The first number
	 * @param y
	 *            The second number
	 * @param perCent
	 *            The percentage limit to use
	 * @return True if the numbers are equal within the specified percentage
	 *         limit, otherwise false
	 */
	public static boolean isEqualWithinPerCent(double x, double y, double perCent)
	{
		boolean test = false;
		double limit = Math.abs((x + y) * perCent / 200.0D);
		if (Math.abs(x - y) <= limit)
			test = true;
		return test;
	}

	/**
	 * Calculate the least common multiple of a data set
	 * 
	 * @param data
	 *            The data set to use
	 * @return The east common multiple of the data set
	 */
	public static long lowestCommonMultiple(int[] data)
	{
		int n, k, j, c, maxNumber, minNumber;
		long prod;

		maxNumber = data[0];
		n = data.length;

		for (k = 0; k < n; k++)
			if (data[k] >= maxNumber)
				maxNumber = data[k];
		minNumber = data[0];
		for (k = 0; k < n; k++)
			if (data[k] < minNumber)
				minNumber = data[k];

		for (k = 0, prod = 1; k < n; k++)
			prod = prod * data[k];

		for (k = maxNumber; k <= prod; k += maxNumber)
		{

			c = 0;
			for (j = 0; j < n; j++)
				if (k % data[j] == 0)
					c += 1;
			if (c == n)
			{
				return k;
			}
		}
		return 0;
	}

	/**
	 * Calculate the number of subsets that can be selected from a set where
	 * order is important
	 * 
	 * @param n
	 *            The size of the set
	 * @param r
	 *            the size of the subsets to count
	 * @return The number of permuations of a set of size n where r elements are
	 *         chosen
	 */
	public static double permutation(double n, double r)
	{
		double result = 0;
		result = factorial(n) / factorial(n - r);

		return result;
	}

	/**
	 * Calculate the nth root of the number
	 * 
	 * @param num
	 *            The number to use
	 * @param n
	 *            The root to use
	 * @return The nth root of num
	 */
	public static double root(double num, double n)
	{
		return Math.pow(num, 1.0 / n);
	}

	/**
	 * Round a number to a specified number of decimal places
	 * 
	 * @param number
	 *            The number to round
	 * @param places
	 *            The number of decimal places to round to
	 * @return The number rounded to the specified number of decimal places
	 */
	public static double round(double number, int places)
	{
		places = Math.abs(places);
//		if (Math.abs(number)  Math.pow(10, -places))
//			return number;
//		else
		return BigDecimal.valueOf(number).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * Calculate the secant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The secant of the angle
	 */
	public static double sec(double a)
	{
		return 1.0 / Math.cos(a);
	}

	/**
	 * Calculate the hyperbolic secant of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic secant of the angle
	 */
	public static double sech(double a)
	{
		return 1.0D / cosh(a);
	}

	/**
	 * Calculate the sign of a number
	 * 
	 * @param x
	 *            The number to use
	 * @return The sign of the number
	 */
	public static double sign(double x)
	{
		if (x < 0.0)
		{
			return -1.0;
		}
		else
		{
			return 1.0;
		}
	}

	/**
	 * Calculate the hyperbolic sine of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic sine of the angle
	 */
	public static double sinh(double a)
	{
		return 0.5D * (Math.exp(a) - Math.exp(-a));
	}

	/**
	 * Calculate the hyperbolic tangent of an angle
	 * 
	 * @param a
	 *            The angle
	 * @return The hyperbolic tangent of the angle
	 */
	public static double tanh(double a)
	{
		return sinh(a) / cosh(a);
	}
}
