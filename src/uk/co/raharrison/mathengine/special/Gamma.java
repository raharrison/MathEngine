package uk.co.raharrison.mathengine.special;

public class Gamma
{
	private static final double HALF_LOG_TWOPI = 0.91893853320467274178032973640562;

	private Gamma()
	{}

	public static double gamma(double x)
	{
		return Math.exp(gammaLn(x));
	}

	// Natural logarithm of the gamma function
	// From http://www.johndcook.com/Gamma.cs
	public static double gammaLn(double x)
	{
		if (x <= 0.0)
		{
			String msg = String.format(
					"Invalid input argument %s. Argument must be positive.", x);
			throw new IllegalArgumentException(msg);
		}

		// Abramowitz and Stegun 6.1.41
		// Asymptotic series should be good to at least 11 or 12 figures
		// For error analysis, see Whittiker and Watson
		// A Course in Modern Analysis (1927), page 252

		double[] c = { 1.0 / 12.0, -1.0 / 360.0, 1.0 / 1260.0, -1.0 / 1680.0,
				1.0 / 1188.0, -691.0 / 360360.0, 1.0 / 156.0,
				-3617.0 / 122400.0 };
		double z = 1.0 / (x * x);
		double sum = c[7];
		for (int i = 6; i >= 0; i--)
		{
			sum *= z;
			sum += c[i];
		}
		double series = sum / x;

		double logGamma = (x - 0.5) * Math.log(x) - x + HALF_LOG_TWOPI + series;
		return logGamma;
	}
}