package uk.co.ryanharrison.mathengine.distributions;

import java.util.Objects;

/**
 * Immutable implementation of the Exponential {@link ContinuousProbabilityDistribution}.
 * <p>
 * The exponential distribution models the time between events in a Poisson process,
 * where events occur continuously and independently at a constant average rate.
 * It is characterized by a single parameter:
 * </p>
 * <ul>
 *     <li><b>λ (lambda or rate)</b>: the rate parameter, representing the average number
 *     of events per unit time</li>
 * </ul>
 * <p>
 * The probability density function is:
 * <br>
 * f(x) = λe^(-λx) for x ≥ 0
 * </p>
 * <p>
 * The mean of the distribution is 1/λ and the variance is 1/λ².
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Exponential distribution with rate λ = 0.5
 * ExponentialDistribution dist = ExponentialDistribution.of(0.5);
 *
 * // Calculate probability density at x = 2.0
 * double pdf = dist.density(2.0);
 *
 * // Calculate cumulative probability P(X ≤ 3.0)
 * double cdf = dist.cumulative(3.0);
 *
 * // Find the median (50th percentile)
 * double median = dist.inverseCumulative(0.5);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class ExponentialDistribution implements ContinuousProbabilityDistribution {

    /**
     * The rate parameter (λ) of the distribution.
     */
    private final double rate;

    /**
     * Private constructor to enforce factory method usage.
     *
     * @param rate the rate parameter (λ)
     */
    private ExponentialDistribution(double rate) {
        this.rate = rate;
    }

    /**
     * Creates an exponential distribution with the specified rate parameter.
     *
     * @param rate the rate parameter (λ), must be positive
     * @return an exponential distribution with the given rate
     * @throws IllegalArgumentException if rate is not positive
     */
    public static ExponentialDistribution of(double rate) {
        if (rate <= 0.0) {
            throw new IllegalArgumentException("Rate must be positive, got: " + rate);
        }
        return new ExponentialDistribution(rate);
    }

    /**
     * Creates an exponential distribution with mean equal to the specified value.
     * <p>
     * Since mean = 1/λ, this creates a distribution with rate λ = 1/mean.
     * </p>
     *
     * @param mean the desired mean, must be positive
     * @return an exponential distribution with the given mean
     * @throws IllegalArgumentException if mean is not positive
     */
    public static ExponentialDistribution withMean(double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("Mean must be positive, got: " + mean);
        }
        return new ExponentialDistribution(1.0 / mean);
    }

    /**
     * Gets the rate parameter (λ) of this distribution.
     *
     * @return the rate parameter
     */
    public double getRate() {
        return rate;
    }

    /**
     * Gets the mean (1/λ) of this distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return 1.0 / rate;
    }

    /**
     * Gets the variance (1/λ²) of this distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return 1.0 / (rate * rate);
    }

    @Override
    public double density(double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException("x must be non-negative, got: " + x);
        }
        return rate * Math.exp(-rate * x);
    }

    @Override
    public double cumulative(double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException("x must be non-negative, got: " + x);
        }
        return 1.0 - Math.exp(-rate * x);
    }

    @Override
    public double inverseCumulative(double p) {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException("Probability must be in (0, 1), got: " + p);
        }
        return -Math.log(1.0 - p) / rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExponentialDistribution)) return false;
        ExponentialDistribution that = (ExponentialDistribution) o;
        return Double.compare(that.rate, rate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate);
    }

    @Override
    public String toString() {
        return String.format("ExponentialDistribution(λ=%.4f)", rate);
    }
}
