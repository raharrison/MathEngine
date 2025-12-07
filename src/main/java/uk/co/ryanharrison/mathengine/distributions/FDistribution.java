package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

import java.util.Objects;

/**
 * Immutable implementation of the F (Fisher-Snedecor) {@link ContinuousProbabilityDistribution}.
 * <p>
 * The F-distribution arises frequently in hypothesis testing, particularly in analysis of variance (ANOVA)
 * and in testing whether two samples have the same variance. It is characterized by two degrees of freedom
 * parameters:
 * </p>
 * <ul>
 *     <li><b>d₁ (degrees of freedom 1)</b>: the numerator degrees of freedom</li>
 *     <li><b>d₂ (degrees of freedom 2)</b>: the denominator degrees of freedom</li>
 * </ul>
 * <p>
 * The probability density function is:
 * <br>
 * f(x) = sqrt((d₁x)^d₁ * d₂^d₂ / (d₁x + d₂)^(d₁+d₂)) / (x * B(d₁/2, d₂/2))
 * </p>
 * <p>
 * where B is the beta function.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // F-distribution with d₁=5, d₂=10
 * FDistribution dist = FDistribution.of(5.0, 10.0);
 *
 * // Calculate probability density at x = 2.0
 * double pdf = dist.density(2.0);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class FDistribution implements ContinuousProbabilityDistribution {

    /**
     * The first degrees of freedom parameter (d₁).
     */
    private final double degreesOfFreedom1;

    /**
     * The second degrees of freedom parameter (d₂).
     */
    private final double degreesOfFreedom2;

    /**
     * Pre-computed logarithm factor for the density calculation.
     * Equals 0.5 * (d₁*ln(d₁) + d₂*ln(d₂)) + ln(Γ((d₁+d₂)/2)) - ln(Γ(d₁/2)) - ln(Γ(d₂/2))
     */
    private final double logDensityFactor;

    /**
     * Private constructor to enforce factory method usage.
     *
     * @param degreesOfFreedom1 the first degrees of freedom (d₁)
     * @param degreesOfFreedom2 the second degrees of freedom (d₂)
     */
    private FDistribution(double degreesOfFreedom1, double degreesOfFreedom2) {
        this.degreesOfFreedom1 = degreesOfFreedom1;
        this.degreesOfFreedom2 = degreesOfFreedom2;
        this.logDensityFactor = 0.5 * (degreesOfFreedom1 * Math.log(degreesOfFreedom1)
                + degreesOfFreedom2 * Math.log(degreesOfFreedom2))
                + Gamma.gammaLn(0.5 * (degreesOfFreedom1 + degreesOfFreedom2))
                - Gamma.gammaLn(0.5 * degreesOfFreedom1)
                - Gamma.gammaLn(0.5 * degreesOfFreedom2);
    }

    /**
     * Creates an F-distribution with the specified degrees of freedom.
     *
     * @param degreesOfFreedom1 the first degrees of freedom (d₁), must be positive
     * @param degreesOfFreedom2 the second degrees of freedom (d₂), must be positive
     * @return an F-distribution with the given parameters
     * @throws IllegalArgumentException if either degrees of freedom is not positive
     */
    public static FDistribution of(double degreesOfFreedom1, double degreesOfFreedom2) {
        if (degreesOfFreedom1 <= 0.0 || degreesOfFreedom2 <= 0.0) {
            throw new IllegalArgumentException(
                    "Degrees of freedom must be positive, got: d₁=" + degreesOfFreedom1 +
                            ", d₂=" + degreesOfFreedom2);
        }
        return new FDistribution(degreesOfFreedom1, degreesOfFreedom2);
    }

    /**
     * Gets the first degrees of freedom (d₁) of this distribution.
     *
     * @return the first degrees of freedom
     */
    public double getDegreesOfFreedom1() {
        return degreesOfFreedom1;
    }

    /**
     * Gets the second degrees of freedom (d₂) of this distribution.
     *
     * @return the second degrees of freedom
     */
    public double getDegreesOfFreedom2() {
        return degreesOfFreedom2;
    }

    /**
     * Gets the mean of this distribution.
     * <p>
     * The mean is defined for d₂ > 2 and equals d₂/(d₂-2).
     * </p>
     *
     * @return the mean
     * @throws ArithmeticException if d₂ ≤ 2 (mean is undefined)
     */
    public double getMean() {
        if (degreesOfFreedom2 <= 2.0) {
            throw new ArithmeticException("Mean is undefined for d₂ ≤ 2");
        }
        return degreesOfFreedom2 / (degreesOfFreedom2 - 2.0);
    }

    /**
     * Gets the variance of this distribution.
     * <p>
     * The variance is defined for d₂ > 4 and equals:
     * 2d₂²(d₁+d₂-2) / (d₁(d₂-2)²(d₂-4))
     * </p>
     *
     * @return the variance
     * @throws ArithmeticException if d₂ ≤ 4 (variance is undefined)
     */
    public double getVariance() {
        if (degreesOfFreedom2 <= 4.0) {
            throw new ArithmeticException("Variance is undefined for d₂ ≤ 4");
        }
        double d2m2 = degreesOfFreedom2 - 2.0;
        return (2.0 * degreesOfFreedom2 * degreesOfFreedom2 * (degreesOfFreedom1 + degreesOfFreedom2 - 2.0))
                / (degreesOfFreedom1 * d2m2 * d2m2 * (degreesOfFreedom2 - 4.0));
    }

    @Override
    public double density(double x) {
        if (x <= 0.0) {
            throw new IllegalArgumentException("x must be positive, got: " + x);
        }

        // f(x) = exp((d₁/2 - 1)*ln(x) - (d₁+d₂)/2 * ln(d₂ + d₁*x) + logFactor)
        return Math.exp((0.5 * degreesOfFreedom1 - 1.0) * Math.log(x)
                - 0.5 * (degreesOfFreedom1 + degreesOfFreedom2)
                * Math.log(degreesOfFreedom2 + degreesOfFreedom1 * x)
                + logDensityFactor);
    }

    @Override
    public double cumulative(double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException("x must be non-negative, got: " + x);
        }

        if (x == 0.0) {
            return 0.0;
        }

        // The cumulative distribution function requires the regularized incomplete
        // beta function I_t(d₁/2, d₂/2) where t = d₁*x/(d₁*x + d₂).
        throw new UnsupportedOperationException(
                "Cumulative distribution function not yet implemented for F-distribution. " +
                        "Requires implementation of the regularized incomplete beta function.");
    }

    @Override
    public double inverseCumulative(double p) {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException("Probability must be in (0, 1), got: " + p);
        }

        // The inverse CDF requires numerical inversion of the regularized incomplete
        // beta function, which is not yet implemented in this library.
        throw new UnsupportedOperationException(
                "Inverse cumulative distribution function not yet implemented for F-distribution. " +
                        "Requires implementation of the regularized incomplete beta function.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FDistribution)) return false;
        FDistribution that = (FDistribution) o;
        return Double.compare(that.degreesOfFreedom1, degreesOfFreedom1) == 0 &&
                Double.compare(that.degreesOfFreedom2, degreesOfFreedom2) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(degreesOfFreedom1, degreesOfFreedom2);
    }

    @Override
    public String toString() {
        return String.format("FDistribution(d₁=%.4f, d₂=%.4f)", degreesOfFreedom1, degreesOfFreedom2);
    }
}
