package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Gamma;

public final class ChiSquareDistribution extends ContinuousProbabilityDistribution
{
	private double nu, fac;

	public ChiSquareDistribution(double nu)
	{
		if (nu <= 0.)
			throw new IllegalArgumentException("nu must be greater than 0");

		this.nu = nu;

		fac = 0.693147180559945309 * (0.5 * nu) + Gamma.gammaLn(0.5 * nu);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x2)
	{
		if (x2 < 0.)
			throw new IllegalArgumentException("x2 must be greater than 0");
		return Gamma.regLowerIncompleteGamma(0.5 * nu, 0.5 * x2);
	}

	// Return probability density function.
	@Override
	public double density(double x2)
	{
		if (x2 <= 0.)
			throw new IllegalArgumentException("x2 must be greater than 0");
		return Math.exp(-0.5 * (x2 - (nu - 2.) * Math.log(x2)) - fac);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{

		if (p < 0. || p >= 1.)
			throw new IllegalArgumentException("p must be greater than 0 and less than 0");
		return 2. * Gamma.inverseRegLowerIncompleteGamma(p, 0.5 * nu);
	}
}
