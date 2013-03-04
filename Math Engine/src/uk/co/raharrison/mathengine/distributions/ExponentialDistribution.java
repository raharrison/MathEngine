package uk.co.raharrison.mathengine.distributions;

public final class ExponentialDistribution extends
		ContinuousProbabilityDistribution
{
	private double bet;

	public ExponentialDistribution(double bet)
	{
		if (bet <= 0.)
			throw new IllegalArgumentException("bet must be greater than 0");

		this.bet = bet;
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double x)
	{

		if (x < 0.)
			throw new IllegalArgumentException("x must be greater than 0");
		return 1. - Math.exp(-bet * x);
	}

	// Return probability density function.
	@Override
	public double density(double x)
	{
		if (x < 0.)
			throw new IllegalArgumentException("x must be greater than 0");
		return bet * Math.exp(-bet * x);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{

		if (p < 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");
		return -Math.log(1. - p) / bet;
	}
}
