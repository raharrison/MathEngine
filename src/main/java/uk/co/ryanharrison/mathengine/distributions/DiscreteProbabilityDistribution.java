package uk.co.ryanharrison.mathengine.distributions;

/**
 * Class representing a {@link ProbabilityDistribution} that can be applied to
 * discrete random variables
 *
 * @author Ryan Harrison
 *
 */
public abstract class DiscreteProbabilityDistribution extends ProbabilityDistribution {
    /**
     * Calculate the area of the distribution function from negative infinity to
     * x
     *
     * @param k The upper bound of the integral calculation
     * @return The area of the distribution function from negative infinity to x
     */
    public abstract double cumulative(int k);

    /**
     * Calculate the value of the distribution function at x
     *
     * @param k The value to evaluate at
     * @return The value of the distribution function at x
     */
    public abstract double density(int k);

    /**
     * Find the value of the upper bound x which forms an integral calculation
     * with negative infinity that results in a probability value of p
     * <p>
     * This is the inverse of the cumulative function above.
     * <p>
     * For example if x = 3.5 and cumulative of 3.5 is 0.7 = p, then inverse
     * cumulative of 0.7 (p) = 3.5 (the original x value)
     *
     * @param p The probability area
     * @return The upper bound of the integral calculation which results in a
     * probability of p
     */
    public abstract int inverseCumulative(double p);
}
