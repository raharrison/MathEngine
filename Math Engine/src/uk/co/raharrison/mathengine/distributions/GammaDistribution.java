package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Gamma;

public final class GammaDistribution extends ContinuousProbabilityDistribution
{
	private double alph, bet, fac;

	public GammaDistribution(double alph)
	{
		this(alph, 1);
	}

	public GammaDistribution(double alph, double bet)
	{
		if (alph <= 0. || bet <= 0.)
			throw new IllegalArgumentException("alph and bet must be greater than 0");

		this.alph = alph;
		this.bet = bet;

		fac = alph * Math.log(bet) - Gamma.gammaLn(alph);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x)
	{
		if (x < 0.)
			throw new IllegalArgumentException("x must be greater than 0");
		return Gamma.regLowerIncompleteGamma(alph, bet * x);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		if (x <= 0.)
			throw new IllegalArgumentException("x must be greater than 0");
		return Math.exp(-bet * x + (alph - 1.) * Math.log(x) + fac);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p < 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		return Gamma.inverseRegLowerIncompleteGamma(p, alph) / bet;
	}
}
