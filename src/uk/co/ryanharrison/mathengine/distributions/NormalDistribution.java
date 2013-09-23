package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Erf;

/**
 * Class representing the Normal {@link ProbabilityDistribution}
 * 
 * @author Ryan Harrison
 * 
 */
public final class NormalDistribution extends ContinuousProbabilityDistribution
{
	/** The square root of one half */
	private static final double SQRT_ONE_HALF = 0.707106781186547524;

	/** One over the square root of two PI */
	private static final double ONE_OVER_SQRT_2PI = 0.398942280401432678;

	/** The values of mu and sigma (the mean and standard deviation */
	private double mu, sig;

	/**
	 * Construct a new {@link NormalDistribution} instance.
	 * 
	 * This constructor defaults the mean to zero and standard deviation to one
	 */
	public NormalDistribution()
	{
		this(0.0, 1.0);
	}

	/**
	 * Construct a new {@link NormalDistribution} instance with the specified
	 * mean and standard deviation
	 * 
	 * @param mu
	 *            The mean
	 * @param sig
	 *            The standard deviation
	 * @exception IllegalArgumentException
	 *                If the standard deviation is less than or equal to zero
	 */
	public NormalDistribution(double mu, double sig)
	{
		if (sig <= 0.0)
			throw new IllegalArgumentException("Standard deviation must be greater than 0");

		this.mu = mu;
		this.sig = sig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double cumulative(double x)
	{
		return 0.5 * Erf.erfc(-SQRT_ONE_HALF * (x - mu) / sig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double density(double x)
	{
		return ONE_OVER_SQRT_2PI / sig * Math.exp(-0.5 * Math.pow((x - mu) / sig, 2.0));
	}

	/**
	 * A rational approximation of the ratio of polynomials
	 * 
	 * @param t
	 *            The value to approximate at
	 * @return The ratio
	 * @see <a
	 *      href="http://www.johndcook.com/csharp_phi_inverse.html">http://www.johndcook.com/csharp_phi_inverse.html</a>
	 */
	private static double rationalApproximation(double t)
	{
		// Abramowitz and Stegun formula 26.2.23.
		// The absolute value of the error should be less than 4.5 e-4.
		double[] c = { 2.515517, 0.802853, 0.010328 };
		double[] d = { 1.432788, 0.189269, 0.001308 };
		return t - ((c[2] * t + c[1]) * t + c[0]) / (((d[2] * t + d[1]) * t + d[0]) * t + 1.0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If p is not between zero an one
	 * 
	 * @see <a
	 *      href="http://www.johndcook.com/csharp_phi_inverse.html">http://www.johndcook.com/csharp_phi_inverse.html</a>
	 */
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0.0 || p >= 1.0)
			throw new IllegalArgumentException("p must be between 0 and 1");

		// See article above for explanation of this section.
		double result = 0.0;
		if (p < 0.5)
		{
			// F^-1(p) = - G^-1(p)
			result = -rationalApproximation(Math.sqrt(-2.0 * Math.log(p)));
		}
		else
		{
			// F^-1(p) = G^-1(1-p)
			result = rationalApproximation(Math.sqrt(-2.0 * Math.log(1.0 - p)));
		}

		// z = (x-u) / sigma

		return (result * sig) + mu;
	}
}
