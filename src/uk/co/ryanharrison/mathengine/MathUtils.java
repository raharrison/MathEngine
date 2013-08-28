package uk.co.ryanharrison.mathengine;

import java.math.BigDecimal;
import java.math.RoundingMode;

import uk.co.ryanharrison.mathengine.special.Gamma;

public final class MathUtils
{
	public static final double SQTPI = 2.50662827463100050242E0;

	public static final double LOGPI = 1.14472988584940017414;

	private MathUtils()
	{
	}

	// Inverse cosecant
	public static double acosec(double a)
	{
		return Math.asin(1.0 / a);
	}

	// Inverse hyperbolic cosecant of a double number
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

	// Inverse hyperbolic cosine of a double number
	public static double acosh(double a)
	{
		return Math.log(a + Math.sqrt(a * a - 1.0D));
	}

	// Inverse cotangent
	public static double acot(double a)
	{
		return Math.atan(1.0D / a);
	}

	// Inverse hyperbolic cotangent of a double number
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

	// Inverse secant
	public static double asec(double a)
	{
		return Math.acos(1.0 / a);
	}

	// Inverse hyperbolic secant of a double number
	public static double asech(double a)
	{
		return 0.5D * Math.log(1.0D / a + Math.sqrt(1.0D / (a * a) - 1.0D));
	}

	// Inverse hyperbolic sine of a double number
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

	// Inverse hyperbolic tangent of a double number
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

	public static double compoundInterest(double x, double rate, double years)
	{
		return x * Math.pow(1 + rate / 100, years);
	}

	// Cosecant
	public static double cosec(double a)
	{
		return 1.0D / Math.sin(a);
	}

	// Hyperbolic cosecant of a double number
	public static double cosech(double a)
	{
		return 1.0D / sinh(a);
	}

	// Hyperbolic cosine of a double number
	public static double cosh(double a)
	{
		return 0.5D * (Math.exp(a) + Math.exp(-a));
	}

	// Cotangent
	public static double cot(double a)
	{
		return 1.0D / Math.tan(a);
	}

	// Hyperbolic cotangent of a double number
	public static double coth(double a)
	{
		return 1.0D / tanh(a);
	}

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

	// Double factorial of a long
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

	// Factorial of double
	public static double factorial(double x)
	{
		double d = Math.abs(x);
		if (Math.floor(d) == d)
			return factorial((long) x);
		else
			return Gamma.gamma(x + 1.0);
	}

	// Factorial of long
	public static long factorial(long num)
	{
		long answer = 1;

		for (int i = 1; i <= num; i++)
		{
			answer *= i;
		}

		return answer;
	}

	// Fractional part of double value
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

	// GCD
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

	// GCD
	public static long gcd(long xval, long yval)
	{
		while (yval != 0)
		{
			long temp = yval;
			yval = xval % yval;
			xval = temp;
		}
		return xval;
	}

	// GCF
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

	// Is equal within limit
	public static boolean isEqualWithinLimits(double x, double y, double limit)
	{
		boolean test = false;
		if (Math.abs(x - y) <= Math.abs(limit))
			test = true;
		return test;
	}

	// Is equal within a percentage
	public static boolean isEqualWithinPerCent(double x, double y, double perCent)
	{
		boolean test = false;
		double limit = Math.abs((x + y) * perCent / 200.0D);
		if (Math.abs(x - y) <= limit)
			test = true;
		return test;
	}

	// LCM
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

	// Permutations
	public static double permutation(double n, double r)
	{
		double result = 0;
		result = factorial(n) / factorial(n - r);

		return result;
	}

	// Radical Roots
	public static double root(double num, double n)
	{
		return Math.pow(num, 1.0 / n);
	}

	// Round
	public static double round(double number, int places)
	{
		// double multipicationFactor = Math.pow(10, places);
		// double interestedInZeroDPs = number * multipicationFactor;
		// return Math.round(interestedInZeroDPs) / multipicationFactor;

		places = Math.abs(places);
		if (Math.abs(number) < Math.pow(10, -places))
			return number;
		else
			return BigDecimal.valueOf(number).setScale(places, RoundingMode.HALF_UP).doubleValue();
	}

	// Secant
	public static double sec(double a)
	{
		return 1.0 / Math.cos(a);
	}

	// Hyperbolic secant of a double number
	public static double sech(double a)
	{
		return 1.0D / cosh(a);
	}

	// Sign of a double
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

	// Hyperbolic sine of a double number
	public static double sinh(double a)
	{
		return 0.5D * (Math.exp(a) - Math.exp(-a));
	}

	// Hyperbolic tangent of a double number
	public static double tanh(double a)
	{
		return sinh(a) / cosh(a);
	}
}
