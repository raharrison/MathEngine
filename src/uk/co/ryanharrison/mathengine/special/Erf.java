package uk.co.ryanharrison.mathengine.special;

/**
 * Class representing the Error function
 * 
 * @author Ryan Harrison
 * 
 */
public final class Erf
{

	/**
	 * Not permitted to instantiate an object of this class
	 */
	private Erf()
	{
	}

	// From
	/**
	 * Calculate the value of the Error function at x
	 * 
	 * @see <a
	 *      href="http://www.johndcook.com/csharp_erf.html">http://www.johndcook.com/csharp_erf.html</a>
	 * @param x
	 *            The parameter to the error function
	 * @return The result of the calculation of Error(x)
	 */
	public static double erf(double x)
	{
		// constants
		double a1 = 0.254829592;
		double a2 = -0.284496736;
		double a3 = 1.421413741;
		double a4 = -1.453152027;
		double a5 = 1.061405429;
		double p = 0.3275911;

		// Save the sign of x
		int sign = 1;
		if (x < 0)
			sign = -1;
		x = Math.abs(x);

		// A&S formula 7.1.26
		double t = 1.0 / (1.0 + p * x);
		double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

		return sign * y;
	}

	/**
	 * Calculate the value of the complementary Error function at x
	 * 
	 * The complementary error function is defined as 1 - {@link #erf(double)}(x)
	 * @param x The parameter to the complementary error function
	 * @return The result of the calculation erfc(x)
	 */
	public static double erfc(double x)
	{
		return 1 - erf(x);
	}
}