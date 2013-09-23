package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

/**
 * Class representing the Binomial {@link ProbabilityDistribution}
 * 
 * @author Ryan Harrison
 * 
 */
public final class BinomialDistribution extends DiscreteProbabilityDistribution
{
	/** The number of elements */
	private int n;

	/**
	 * The probability and the natural logarithm of the gamma function at the
	 * probability
	 */
	private double pe, fac;

	/**
	 * Construct a new {@link BinomialDistribution} with the specified number of
	 * elements and their probability
	 * 
	 * @param n
	 *            The number of elements
	 * @param pe
	 *            The probability
	 * @exception IllegalArgumentException
	 *                If n is less than or equal to zero. If pe is not between
	 *                zero and one
	 */
	public BinomialDistribution(int n, double pe)
	{
		if (n <= 0.0 || pe <= 0.0 || pe >= 1.0)
			throw new IllegalArgumentException(
					"n must be greater than 0, and pe must be between 0 and 1");

		this.n = n;
		this.pe = pe;

		fac = Gamma.gammaLn(n + 1.0);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If k is not greater than zero
	 */
	@Override
	public double cumulative(int k)
	{
		if (k < 0.0)
			throw new IllegalArgumentException("k must be greater than 0");
		if (k == 0.0)
			return 0.0;
		if (k > n)
			return 1.0;

		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If k is not greater than zero
	 */
	@Override
	public double density(int k)
	{
		if (k < 0.0)
			throw new IllegalArgumentException("k must be greater than 0");
		if (k > n)
			return 0.0;

		return Math.exp(k * Math.log(pe) + (n - k) * Math.log(1.0 - pe) + fac
				- Gamma.gammaLn(k + 1.0) - Gamma.gammaLn(n - k + 1.0));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If p is not between zero and one
	 */
	@Override
	public int inverseCumulative(double p)
	{
		if (p <= 0.0 || p >= 1.0)
			throw new IllegalArgumentException("p must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
