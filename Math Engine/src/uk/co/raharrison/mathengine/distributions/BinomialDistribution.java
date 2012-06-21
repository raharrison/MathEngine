package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Beta;
import uk.co.raharrison.mathengine.special.Gamma;

public final class BinomialDistribution extends DiscreteProbabilityDistribution
{
	private int n;
	private double pe, fac;

	public BinomialDistribution(int n, double pe)
	{
		if (n <= 0 || pe <= 0. || pe >= 1.)
			throw new IllegalArgumentException(
					"n must be greater than 0, and pe must be between 0 and 1");

		this.n = n;
		this.pe = pe;

		fac = Gamma.gammaLn(n + 1);
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(int k)
	{
		if (k < 0)
			throw new IllegalArgumentException("k must be greater than 0");
		if (k == 0)
			return 0;
		if (k > n)
			return 1;
		return 1 - Beta.regIncompleteBeta(k, n - k + 1., pe);
	}

	// Return probability density function.
	@Override
	public double density(int k)
	{
		if (k < 0)
			throw new IllegalArgumentException("k must be greater than 0");
		if (k > n)
			return 0;

		return Math.exp(k * Math.log(pe) + (n - k) * Math.log(1. - pe) + fac
				- Gamma.gammaLn(k + 1.) - Gamma.gammaLn(n - k + 1.));
	}

	// Returns the inverse cumulative distribution function.
	@Override
	public int inverseCumulative(double p)
	{
		int k, kl, ku, inc = 1;
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");

		k = Math.max(0, Math.min(n, (int) (n * pe))); // Starting guess near
														// peak of density.
		if (p < cumulative(k))
		{ // Expand interval until we bracket.
			do
			{
				k = Math.max(k - inc, 0);
				inc *= 2;
			}
			while (p < cumulative(k));
			kl = k;
			ku = k + inc / 2;
		}
		else
		{
			do
			{
				k = Math.min(k + inc, n + 1);
				inc *= 2;
			}
			while (p > cumulative(k));
			ku = k;
			kl = k - inc / 2;
		}
		while (ku - kl > 1)
		{ // Now contract the interval by bisection.
			k = (kl + ku) / 2;
			if (p < cumulative(k))
				ku = k;
			else
				kl = k;
		}
		return kl;
	}
}
