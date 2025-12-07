package uk.co.ryanharrison.mathengine.distributions;

import java.util.Objects;

/**
 * Immutable implementation of the Logistic {@link ContinuousProbabilityDistribution}.
 * <p>
 * The logistic distribution is a continuous probability distribution with a sigmoid-shaped
 * cumulative distribution function. It is characterized by two parameters:
 * </p>
 * <ul>
 *     <li><b>μ (mu)</b>: the location parameter, which determines the center of the distribution</li>
 *     <li><b>s (scale)</b>: the scale parameter, which determines the spread</li>
 * </ul>
 * <p>
 * The probability density function is:
 * <br>
 * f(x) = (e^(-(x-μ)/s)) / (s(1 + e^(-(x-μ)/s))²)
 * </p>
 * <p>
 * The logistic distribution is commonly used in logistic regression and has applications
 * in various fields including economics, medicine, and machine learning.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Standard logistic distribution (μ=0, s=1)
 * LogisticDistribution standard = LogisticDistribution.standard();
 *
 * // Custom distribution
 * LogisticDistribution custom = LogisticDistribution.of(5.0, 2.0);
 *
 * // Calculate probability density
 * double pdf = custom.density(6.0);
 *
 * // Calculate cumulative probability
 * double cdf = custom.cumulative(7.0);
 *
 * // Find quantile
 * double quantile = custom.inverseCumulative(0.75);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class LogisticDistribution implements ContinuousProbabilityDistribution {

    /**
     * Constant value used in computations: π/√3 ≈ 1.8138
     */
    private static final double PI_OVER_SQRT3 = Math.PI / Math.sqrt(3.0);

    /**
     * Constant value used in computations: √3/π ≈ 0.5513
     */
    private static final double SQRT3_OVER_PI = Math.sqrt(3.0) / Math.PI;

    /**
     * The location parameter (μ) of the distribution.
     */
    private final double location;

    /**
     * The scale parameter (s) of the distribution.
     */
    private final double scale;

    /**
     * Private constructor to enforce factory method usage.
     *
     * @param location the location parameter (μ)
     * @param scale    the scale parameter (s)
     */
    private LogisticDistribution(double location, double scale) {
        this.location = location;
        this.scale = scale;
    }

    /**
     * Creates a standard logistic distribution with location = 0 and scale = 1.
     *
     * @return a standard logistic distribution
     */
    public static LogisticDistribution standard() {
        return new LogisticDistribution(0.0, 1.0);
    }

    /**
     * Creates a logistic distribution with the specified location and scale parameters.
     *
     * @param location the location parameter (μ)
     * @param scale    the scale parameter (s), must be positive
     * @return a logistic distribution with the given parameters
     * @throws IllegalArgumentException if scale is not positive
     */
    public static LogisticDistribution of(double location, double scale) {
        if (scale <= 0.0) {
            throw new IllegalArgumentException("Scale must be positive, got: " + scale);
        }
        return new LogisticDistribution(location, scale);
    }

    /**
     * Gets the location parameter (μ) of this distribution.
     *
     * @return the location parameter
     */
    public double getLocation() {
        return location;
    }

    /**
     * Gets the scale parameter (s) of this distribution.
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * Gets the mean (μ) of this distribution.
     * <p>
     * For the logistic distribution, the mean equals the location parameter.
     * </p>
     *
     * @return the mean
     */
    public double getMean() {
        return location;
    }

    /**
     * Gets the variance (s²π²/3) of this distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return scale * scale * Math.PI * Math.PI / 3.0;
    }

    @Override
    public double density(double x) {
        double z = PI_OVER_SQRT3 * (x - location) / scale;
        double expNegZ = Math.exp(-Math.abs(z));
        double denominator = 1.0 + expNegZ;
        return PI_OVER_SQRT3 * expNegZ / (scale * denominator * denominator);
    }

    @Override
    public double cumulative(double x) {
        double z = PI_OVER_SQRT3 * (x - location) / scale;
        double expNegAbsZ = Math.exp(-Math.abs(z));

        if (x >= location) {
            return 1.0 / (1.0 + expNegAbsZ);
        } else {
            return expNegAbsZ / (1.0 + expNegAbsZ);
        }
    }

    @Override
    public double inverseCumulative(double p) {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException("Probability must be in (0, 1), got: " + p);
        }

        // Inverse logistic CDF: μ + s * log(p / (1-p))
        // Using √3/π scaling factor for our parameterization
        return location + SQRT3_OVER_PI * scale * Math.log(p / (1.0 - p));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogisticDistribution)) return false;
        LogisticDistribution that = (LogisticDistribution) o;
        return Double.compare(that.location, location) == 0 &&
                Double.compare(that.scale, scale) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, scale);
    }

    @Override
    public String toString() {
        return String.format("LogisticDistribution(μ=%.4f, s=%.4f)", location, scale);
    }
}
