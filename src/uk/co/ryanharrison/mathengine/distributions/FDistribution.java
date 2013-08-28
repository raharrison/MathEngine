package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

public final class FDistribution extends ContinuousProbabilityDistribution
{

	private double nu1, nu2, fac;

	public FDistribution(double nu1, double nu2)
	{
		if (nu1 <= 0. || nu2 <= 0.)
			throw new IllegalArgumentException(
					"nu1 and nu2 must be greater than 0");

		this.nu1 = nu1;
		this.nu2 = nu2;

		fac = 0.5 * (nu1 * Math.log(nu1) + nu2 * Math.log(nu2))
				+ Gamma.gammaLn(0.5 * (nu1 + nu2)) - Gamma.gammaLn(0.5 * nu1)
				- Gamma.gammaLn(0.5 * nu2);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double f)
	{
		if (f < 0.)
			throw new IllegalArgumentException("f must be greater than 0");

		throw new UnsupportedOperationException("Not implemented");
	}

	// Return probability density function.
	@Override
	public double density(double f)
	{
		if (f <= 0.)
			throw new IllegalArgumentException("f must be greater than 0");
		return Math.exp((0.5 * nu1 - 1.) * Math.log(f) - 0.5 * (nu1 + nu2)
				* Math.log(nu2 + nu1 * f) + fac);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
