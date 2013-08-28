package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

public final class StudentTDistribution extends
		ContinuousProbabilityDistribution
{
	private double nu, mu, sig, np, fac;

	public StudentTDistribution(double nu)
	{
		this(nu, 0, 1);
	}

	public StudentTDistribution(double nu, double mu, double sig)
	{
		if (sig <= 0 || nu <= 0)
			throw new IllegalArgumentException(
					"Standard deviation and nu must be greater than 0");

		this.nu = nu;
		this.mu = mu;
		this.sig = sig;

		np = 0.5 * (nu + 1.);
		fac = Gamma.gammaLn(np) - Gamma.gammaLn(0.5 * nu);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double t)
	{
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public double density(double t)
	{
		return Math.exp(-np * Math.log(1. + Math.pow((t - mu) / sig, 2) / nu)
				+ fac)
				/ (Math.sqrt(Math.PI * nu) * sig);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p mus be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
