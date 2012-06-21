package uk.co.raharrison.mathengine.distributions;

import uk.co.raharrison.mathengine.special.Beta;
import uk.co.raharrison.mathengine.special.Gamma;

public final class StudentTDistribution extends ContinuousProbabilityDistribution
{
	private double nu, mu, sig, np, fac;

	public StudentTDistribution(double nu)
	{
		this(nu, 0, 1);
	}

	public StudentTDistribution(double nu, double mu, double sig)
	{
		if (sig <= 0 || nu <= 0)
			throw new IllegalArgumentException("Standard deviation and nu must be greater than 0");

		this.nu = nu;
		this.mu = mu;
		this.sig = sig;

		np = 0.5 * (nu + 1.);
		fac = Gamma.gammaLn(np) - Gamma.gammaLn(0.5 * nu);
	}

	// Return probability density function.

	// Return the two-tailed cdf A.tj/.
	public double aa(double t)
	{
		if (t < 0.)
			throw new IllegalArgumentException("t must be less than 0");
		return 1. - Beta.regIncompleteBeta(0.5 * nu, 0.5, nu / (nu + Math.pow(t, 2)));
	}

	// Return cumulative distribution function.
	@Override
	public double cumulative(double t)
	{
		double p = 0.5 * Beta.regIncompleteBeta(0.5 * nu, 0.5,
				nu / (nu + Math.pow((t - mu) / sig, 2)));
		if (t >= mu)
			return 1. - p;
		else
			return p;
	}

	@Override
	public double density(double t)
	{
		return Math.exp(-np * Math.log(1. + Math.pow((t - mu) / sig, 2) / nu) + fac)
				/ (Math.sqrt(3.14159265358979324 * nu) * sig);
	}

	// Return the inverse two tailed cdf
	public double inverseAA(double p)
	{

		if (p < 0. || p >= 1.)
			throw new IllegalArgumentException("p must be between 0 and 1");
		double x = Beta.inverseRegIncompleteBeta(1. - p, 0.5 * nu, 0.5);
		return Math.sqrt(nu * (1. - x) / x);
	}

	// Return inverse cumulative distribution function.
	@Override
	public double inverseCumulative(double p)
	{
		if (p <= 0. || p >= 1.)
			throw new IllegalArgumentException("p mus be between 0 and 1");
		double x = Beta.inverseRegIncompleteBeta(2. * Math.min(p, 1. - p), 0.5 * nu, 0.5);
		x = sig * Math.sqrt(nu * (1. - x) / x);
		return p >= 0.5 ? mu + x : mu - x;
	}
}
