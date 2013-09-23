package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

/**
 * Class representing the F {@link ProbabilityDistribution}
 * 
 * @author Ryan Harrison
 * 
 */
public final class FDistribution extends ContinuousProbabilityDistribution
{
	/** Variables of the distribution */
	private double nu1, nu2, fac;

	/**
	 * Construct a new {@link FDistribution} object with specified values of nu1
	 * and nu2
	 * 
	 * @param nu1
	 *            The first parameter
	 * @param nu2
	 *            The second parameter
	 * @exception IllegalArgumentException
	 *                If nu1 or nu2 are less than or equal to zero
	 */
	public FDistribution(double nu1, double nu2)
	{
		if (nu1 <= 0.0 || nu2 <= 0.0)
			throw new IllegalArgumentException("nu1 and nu2 must be greater than 0");

		this.nu1 = nu1;
		this.nu2 = nu2;

		fac = 0.5 * (nu1 * Math.log(nu1) + nu2 * Math.log(nu2)) + Gamma.gammaLn(0.5 * (nu1 + nu2))
				- Gamma.gammaLn(0.5 * nu1) - Gamma.gammaLn(0.5 * nu2);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If f is less than zero
	 */
	@Override
	public double cumulative(double f)
	{
		if (f < 0.0)
			throw new IllegalArgumentException("f must be greater than 0");

		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If f is less than or equal to zero
	 */
	@Override
	public double density(double f)
	{
		if (f <= 0.0)
			throw new IllegalArgumentException("f must be greater than 0");
		return Math.exp((0.5 * nu1 - 1.0) * Math.log(f) - 0.5 * (nu1 + nu2)
				* Math.log(nu2 + nu1 * f) + fac);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception IllegalArgumentException
	 *                If p is not between zero and one
	 */
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0.0 || p >= 1.0)
			throw new IllegalArgumentException("p must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
