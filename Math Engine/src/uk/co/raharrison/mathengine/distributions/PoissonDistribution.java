package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Gamma;

public final class PoissonDistribution extends DiscreteProbabilityDistribution
{
	private double lam;

	public PoissonDistribution(double lam)
	{
		if (lam <= 0.)
			throw new IllegalArgumentException("lam must be greater than 0");

		this.lam = lam;
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(int n)
	{
		if (n < 0)
			throw new IllegalArgumentException("n must be greater than 0");
		if (n == 0)
			return 0.;
		return Gamma.regUpperIncompleteGamma(n + 1, lam);
	}

	// Return probability density function.
	@Override
	public double density(int n)
	{

		if (n < 0)
			throw new IllegalArgumentException("n must be greater than 0");
		return Math.exp(-lam + n * Math.log(lam) - Gamma.gammaLn(n + 1.));
	}

	// // Return inverse cumulative distribution function.
	@Override
	public int inverseCumulative(double p)
	{

		int n, nl, nu, inc = 1;
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");
		if (p < Math.exp(-lam))
			return 0;
		n = (int) Math.max(Math.sqrt(lam), 5.); // Starting guess near peak of
												// density.
		if (p < cumulative(n))
		{ // Expand interval until we bracket.
			do
			{
				n = Math.max(n - inc, 0);
				inc *= 2;
			}
			while (p < cumulative(n));
			nl = n;
			nu = n + inc / 2;
		}
		else
		{
			do
			{
				n += inc;
				inc *= 2;
			}
			while (p > cumulative(n));
			nu = n;
			nl = n - inc / 2;
		}
		while (nu - nl > 1)
		{ // Now contract the interval by bisection.
			n = (nl + nu) / 2;
			if (p < cumulative(n))
				nu = n;
			else
				nl = n;
		}
		return nl;
	}
}
