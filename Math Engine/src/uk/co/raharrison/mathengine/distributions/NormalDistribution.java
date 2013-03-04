package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Erf;

public final class NormalDistribution extends ContinuousProbabilityDistribution
{
	private static final double SQRT_ONE_HALF = 0.707106781186547524;
	private static final double ONE_OVER_SQRT_2PI = 0.398942280401432678;
	private double mu, sig;

	public NormalDistribution()
	{
		this(0, 1);
	}

	public NormalDistribution(double mu, double sig)
	{
		if (sig <= 0)
			throw new IllegalArgumentException(
					"Standard deviation must be greater than 0");

		this.mu = mu;
		this.sig = sig;
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x)
	{
		return 0.5 * Erf.erfc(-SQRT_ONE_HALF * (x - mu) / sig);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		return ONE_OVER_SQRT_2PI / sig
				* Math.exp(-0.5 * Math.pow((x - mu) / sig, 2));
	}

	// From http://www.johndcook.com/csharp_phi_inverse.html
	private static double rationalApproximation(double t)
	{
		// Abramowitz and Stegun formula 26.2.23.
		// The absolute value of the error should be less than 4.5 e-4.
		double[] c = { 2.515517, 0.802853, 0.010328 };
		double[] d = { 1.432788, 0.189269, 0.001308 };
		return t - ((c[2] * t + c[1]) * t + c[0])
				/ (((d[2] * t + d[1]) * t + d[0]) * t + 1.0);
	}

	// Return inverse cumulative distribution function.
	// From http://www.johndcook.com/csharp_phi_inverse.html
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
