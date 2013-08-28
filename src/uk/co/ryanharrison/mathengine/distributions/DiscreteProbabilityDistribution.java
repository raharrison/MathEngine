package uk.co.ryanharrison.mathengine.distributions;

public abstract class DiscreteProbabilityDistribution extends
		ProbabilityDistribution
{
	public abstract double cumulative(int k);

	public abstract double density(int k);

	public abstract int inverseCumulative(double p);
}
