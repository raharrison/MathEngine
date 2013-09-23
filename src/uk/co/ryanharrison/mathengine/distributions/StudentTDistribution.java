package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

/**
 * Class representing the Student T {@link ProbabilityDistribution}
 * 
 * @author Ryan Harrison
 * 
 */
public final class StudentTDistribution extends ContinuousProbabilityDistribution
{
	/** Variables of the distribution */
	private double nu, mu, sig, np, fac;

	/**
	 * Construct a new {@link StudentTDistribution} object with specified nu
	 * value
	 * 
	 * @param nu
	 *            The value of nu
	 */
	public StudentTDistribution(double nu)
	{
		this(nu, 0.0, 1.0);
	}

	/**
	 * Construct a new {@link StudentTDistribution} object with specified nu,
	 * sigma and mu values
	 * 
	 * @param nu
	 *            The value of nu
	 * @param mu
	 *            The mean
	 * @param sig
	 *            The standard deviation
	 * @exception IllegalArgumentException
	 *                If sig or nu are less than or equal to zero
	 */
	public StudentTDistribution(double nu, double mu, double sig)
	{
		if (sig <= 0.0 || nu <= 0.0)
			throw new IllegalArgumentException("Standard deviation and nu must be greater than 0");

		this.nu = nu;
		this.mu = mu;
		this.sig = sig;

		np = 0.5 * (nu + 1.0);
		fac = Gamma.gammaLn(np) - Gamma.gammaLn(0.5 * nu);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double cumulative(double t)
	{
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double density(double t)
	{
		return Math.exp(-np * Math.log(1 + Math.pow((t - mu) / sig, 2.0) / nu) + fac)
				/ (Math.sqrt(Math.PI * nu) * sig);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @exception UnsupportedOperationException
	 *                If p is not between 0 and 1
	 */
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0.0 || p >= 1.0)
			throw new IllegalArgumentException("p must be between 0 and 1");

		throw new UnsupportedOperationException("Not implemented");
	}
}
