package uk.co.ryanharrison.mathengine.distributions;

/**
 * Class representing a {@link ProbabilityDistribution} that can be applied to
 * continuous random variables
 * 
 * @author Ryan Harrison
 * 
 */
public abstract class ContinuousProbabilityDistribution extends ProbabilityDistribution
{
	/**
	 * Calculate the area of the distribution function from negative infinity to
	 * x
	 * 
	 * @param x
	 *            The upper bound of the integral calculation
	 * @return The area of the distribution function from negative infinity to x
	 */
	public abstract double cumulative(double x);

	/**
	 * Calculate the value of the distribution function at x
	 * 
	 * @param x
	 *            The value to evaluate at
	 * @return The value of the distribution function at x
	 */
	public abstract double density(double x);

	/**
	 * Find the value of the upper bound x which forms an integral calculation
	 * with negative infinity that results in a probability value of p
	 * 
	 * This is the inverse of the cumulative function above.
	 * 
	 * For example if x = 3.5 and cumulative of 3.5 is 0.7 = p, then inverse
	 * cumulative of 0.7 (p) = 3.5 (the original x value)
	 * 
	 * @param p
	 *            The probability area
	 * @return The upper bound of the integral calculation which results in a
	 *         probability of p
	 */
	public abstract double inverseCumulative(double p);
}
