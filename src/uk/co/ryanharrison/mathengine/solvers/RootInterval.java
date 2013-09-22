package uk.co.ryanharrison.mathengine.solvers;

/**
 * Class representing an interval in which a root of a function may lie
 * 
 * @author Ryan Harrison
 * 
 */
public class RootInterval
{
	/** The lower bound of the interval */
	private double lower;

	/** The upper bound of the interval */
	private double upper;

	/**
	 * Construct a new {@link RootInterval} with the specified lower and upper
	 * bounds
	 * 
	 * @param lower
	 *            The lower bound of the interval
	 * @param upper
	 *            The upper bound of the interval
	 */
	public RootInterval(double lower, double upper)
	{
		this.lower = lower;
		this.upper = upper;
	}

	/**
	 * Get the lower bound of this interval
	 * 
	 * @return The lower bound of this interval
	 */
	public double getLower()
	{
		return lower;
	}

	/**
	 * Get the upper bound of this interval
	 * 
	 * @return The upper bound of this interval
	 */
	public double getUpper()
	{
		return upper;
	}
}
