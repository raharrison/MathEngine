package uk.co.raharrison.mathengine.solvers;

public class RootInterval
{
	private double lower;
	private double upper;

	public RootInterval(double lower, double upper)
	{
		this.lower = lower;
		this.upper = upper;
	}

	public double getLower()
	{
		return lower;
	}

	public double getUpper()
	{
		return upper;
	}
}
