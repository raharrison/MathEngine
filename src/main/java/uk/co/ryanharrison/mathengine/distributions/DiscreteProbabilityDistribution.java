package uk.co.ryanharrison.mathengine.distributions;

/**
 * Interface representing a {@link ProbabilityDistribution} for discrete random variables.
 * <p>
 * Discrete probability distributions model variables that can only take specific,
 * countable values (typically integers). Examples include the Binomial and Poisson
 * distributions.
 * </p>
 * <p>
 * Implementations must provide methods for calculating the probability mass function (PMF),
 * cumulative distribution function (CDF), and inverse cumulative distribution function
 * (quantile function).
 * </p>
 *
 * @author Ryan Harrison
 */
public interface DiscreteProbabilityDistribution extends ProbabilityDistribution {

    /**
     * Calculates the probability mass function (PMF) at the given point.
     * <p>
     * The PMF gives the probability that the discrete random variable equals exactly
     * the value {@code k}. Unlike continuous distributions, this directly represents
     * the probability P(X = k).
     * </p>
     *
     * @param k the value at which to evaluate the probability mass
     * @return the probability that the random variable equals {@code k}, a value in the range [0, 1]
     * @throws IllegalArgumentException if {@code k} is outside the valid range for this distribution
     */
    double density(int k);

    /**
     * Calculates the cumulative distribution function (CDF) at the given point.
     * <p>
     * The CDF represents the probability that the random variable is less than or
     * equal to {@code k}. Mathematically, this is the sum of all probability masses
     * from the minimum value up to and including {@code k}.
     * </p>
     *
     * @param k the upper bound of the probability calculation
     * @return the probability that a random variable is less than or equal to {@code k},
     *         a value in the range [0, 1]
     * @throws IllegalArgumentException if {@code k} is outside the valid range for this distribution
     */
    double cumulative(int k);

    /**
     * Calculates the inverse cumulative distribution function (quantile function).
     * <p>
     * This is the inverse of the {@link #cumulative(int)} function. Given a probability
     * {@code p}, this method finds the smallest value {@code k} such that P(X ≤ k) ≥ p.
     * </p>
     * <p>
     * For discrete distributions, there may not be an exact inverse, so this returns
     * the smallest integer {@code k} where the cumulative probability meets or exceeds {@code p}.
     * </p>
     *
     * @param p the probability, must be in the range (0, 1)
     * @return the smallest value {@code k} such that the cumulative probability up to {@code k}
     *         is at least {@code p}
     * @throws IllegalArgumentException if {@code p} is not in the range (0, 1)
     * @throws UnsupportedOperationException if the inverse cumulative function is not implemented
     *         for this distribution
     */
    int inverseCumulative(double p);
}
