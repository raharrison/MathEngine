package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Erf;

public final class NormalDistribution extends ContinuousProbabilityDistribution
{
	private double mu, sig;

	public NormalDistribution()
	{
		this(0, 1);
	}

	public NormalDistribution(double mu, double sig)
	{
		if (sig <= 0)
			throw new IllegalArgumentException("Standard deviation must be greater than 0");

		this.mu = mu;
		this.sig = sig;
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x)
	{
		return 0.5 * Erf.erfc(-0.707106781186547524 * (x - mu) / sig);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		return 0.398942280401432678 / sig * Math.exp(-0.5 * Math.pow((x - mu) / sig, 2));
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		return -1.41421356237309505 * sig * Erf.inverfc(2. * p) + mu;
	}
}
