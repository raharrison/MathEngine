package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Gamma;

public final class BetaDistribution extends ContinuousProbabilityDistribution
{
	private double alph, bet, fac;

	public BetaDistribution(double alph, double bet)
	{
		if (alph <= 0. || bet <= 0.)
			throw new IllegalArgumentException(
					"alph and bet must be greater than 0");

		this.alph = alph;
		this.bet = bet;

		fac = Gamma.gammaLn(alph + bet) - Gamma.gammaLn(alph)
				- Gamma.gammaLn(bet);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x)
	{
		if (x < 0. || x > 1.)
			throw new IllegalArgumentException("x must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		if (x <= 0. || x >= 1.)
			throw new IllegalArgumentException("x must be between 0 and 1");
		return Math.exp((alph - 1.) * Math.log(x) + (bet - 1.)
				* Math.log(1. - x) + fac);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p < 0. || p > 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
