package uk.co.ryanharrison.mathengine.distributions;

public final class LogisticDistribution extends
		ContinuousProbabilityDistribution
{
	private double mu, sig;

	public LogisticDistribution()
	{
		this(0, 1);
	}

	public LogisticDistribution(double mu, double sig)
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
		double e = Math.exp(-Math.abs(1.81379936423421785 * (x - mu) / sig));
		if (x >= mu)
			return 1. / (1. + e);
		return e / (1. + e);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		double e = Math.exp(-Math.abs(1.81379936423421785 * (x - mu) / sig));
		return 1.81379936423421785 * e / (sig * Math.pow(1. + e, 2));
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		return mu + 0.551328895421792049 * sig * Math.log(p / (1. - p));
	}
}
