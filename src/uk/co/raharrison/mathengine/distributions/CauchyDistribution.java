package uk.co.raharrison.mathengine.distributions;

public final class CauchyDistribution extends ContinuousProbabilityDistribution
{
	private double mu, sig;

	public CauchyDistribution()
	{
		this(0, 1);
	}

	public CauchyDistribution(double mu, double sig)
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
		return 0.5 + 0.318309886183790671 * Math.atan2(x - mu, sig);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		return 0.318309886183790671 / (sig * (1. + Math.pow((x - mu) / sig, 2)));
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		return mu + sig * Math.tan(3.14159265358979324 * (p - 0.5));
	}
}
