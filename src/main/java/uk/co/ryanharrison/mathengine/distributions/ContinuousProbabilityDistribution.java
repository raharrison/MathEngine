package uk.co.ryanharrison.mathengine.distributions;

/**
 * Interface representing a {@link ProbabilityDistribution} for continuous random variables.
 * <p>
 * Continuous probability distributions model variables that can take any value within
 * a continuous range. Examples include the Normal, Exponential, and Beta distributions.
 * </p>
 * <p>
 * Implementations must provide methods for calculating the probability density function (PDF),
 * cumulative distribution function (CDF), and inverse cumulative distribution function
 * (quantile function).
 * </p>
 *
 * @author Ryan Harrison
 */
public interface ContinuousProbabilityDistribution extends ProbabilityDistribution {

    /**
     * Calculates the probability density function (PDF) at the given point.
     * <p>
     * The PDF represents the relative likelihood of the random variable taking on
     * a specific value. For continuous distributions, the PDF does not give probabilities
     * directly but must be integrated over an interval.
     * </p>
     *
     * @param x the value at which to evaluate the density
     * @return the probability density at {@code x}
     * @throws IllegalArgumentException if {@code x} is outside the valid range for this distribution
     */
    double density(double x);

    /**
     * Calculates the cumulative distribution function (CDF) at the given point.
     * <p>
     * The CDF represents the probability that the random variable is less than or
     * equal to {@code x}. Mathematically, this is the integral of the PDF from
     * negative infinity to {@code x}.
     * </p>
     *
     * @param x the upper bound of the probability calculation
     * @return the probability that a random variable is less than or equal to {@code x},
     *         a value in the range [0, 1]
     * @throws IllegalArgumentException if {@code x} is outside the valid range for this distribution
     */
    double cumulative(double x);

    /**
     * Calculates the inverse cumulative distribution function (quantile function).
     * <p>
     * This is the inverse of the {@link #cumulative(double)} function. Given a probability
     * {@code p}, this method finds the value {@code x} such that P(X ≤ x) = p.
     * </p>
     * <p>
     * For example, if {@code cumulative(3.5) = 0.7}, then {@code inverseCumulative(0.7) = 3.5}.
     * </p>
     *
     * @param p the probability, must be in the range (0, 1)
     * @return the value {@code x} such that the cumulative probability up to {@code x} equals {@code p}
     * @throws IllegalArgumentException if {@code p} is not in the range (0, 1)
     * @throws UnsupportedOperationException if the inverse cumulative function is not implemented
     *         for this distribution
     */
    double inverseCumulative(double p);
}
