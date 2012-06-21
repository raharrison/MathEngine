package uk.co.raharrison.mathengine.distributions;

public abstract class ContinuousProbabilityDistribution extends ProbabilityDistribution
{
	public abstract double cumulative(double x);

	public abstract double density(double x);

	public abstract double inverseCumulative(double p);
}
