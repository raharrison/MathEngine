package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

import java.util.Objects;

/**
 * Immutable implementation of the Beta {@link ContinuousProbabilityDistribution}.
 * <p>
 * The beta distribution is a continuous probability distribution defined on the interval
 * [0, 1]. It is characterized by two positive shape parameters:
 * </p>
 * <ul>
 *     <li><b>α (alpha)</b>: the first shape parameter</li>
 *     <li><b>β (beta)</b>: the second shape parameter</li>
 * </ul>
 * <p>
 * The probability density function is:
 * <br>
 * f(x) = (1/B(α,β)) * x^(α-1) * (1-x)^(β-1) for x ∈ [0, 1]
 * </p>
 * <p>
 * where B(α,β) is the beta function, which normalizes the distribution.
 * </p>
 * <p>
 * The beta distribution is widely used in Bayesian inference as a conjugate prior
 * for binomial and Bernoulli distributions.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Uniform distribution (α=1, β=1)
 * BetaDistribution uniform = BetaDistribution.of(1.0, 1.0);
 *
 * // Bell-shaped distribution (α=2, β=5)
 * BetaDistribution custom = BetaDistribution.of(2.0, 5.0);
 *
 * // Calculate probability density at x = 0.3
 * double pdf = custom.density(0.3);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class BetaDistribution implements ContinuousProbabilityDistribution {

    /**
     * The first shape parameter (α).
     */
    private final double alpha;

    /**
     * The second shape parameter (β).
     */
    private final double beta;

    /**
     * Pre-computed logarithm of the normalization factor.
     * Equals ln(Γ(α+β) / (Γ(α) * Γ(β)))
     */
    private final double logNormalizationFactor;

    /**
     * Private constructor to enforce factory method usage.
     *
     * @param alpha the first shape parameter (α)
     * @param beta  the second shape parameter (β)
     */
    private BetaDistribution(double alpha, double beta) {
        this.alpha = alpha;
        this.beta = beta;
        this.logNormalizationFactor = Gamma.gammaLn(alpha + beta)
                - Gamma.gammaLn(alpha)
                - Gamma.gammaLn(beta);
    }

    /**
     * Creates a beta distribution with the specified shape parameters.
     *
     * @param alpha the first shape parameter (α), must be positive
     * @param beta  the second shape parameter (β), must be positive
     * @return a beta distribution with the given parameters
     * @throws IllegalArgumentException if alpha or beta are not positive
     */
    public static BetaDistribution of(double alpha, double beta) {
        if (alpha <= 0.0 || beta <= 0.0) {
            throw new IllegalArgumentException(
                    "Shape parameters must be positive, got: α=" + alpha + ", β=" + beta);
        }
        return new BetaDistribution(alpha, beta);
    }

    /**
     * Creates a uniform distribution on [0, 1] (α=1, β=1).
     *
     * @return a uniform beta distribution
     */
    public static BetaDistribution uniform() {
        return new BetaDistribution(1.0, 1.0);
    }

    /**
     * Gets the first shape parameter (α) of this distribution.
     *
     * @return the alpha parameter
     */
    public double getAlpha() {
        return alpha;
    }

    /**
     * Gets the second shape parameter (β) of this distribution.
     *
     * @return the beta parameter
     */
    public double getBeta() {
        return beta;
    }

    /**
     * Gets the mean (α/(α+β)) of this distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return alpha / (alpha + beta);
    }

    /**
     * Gets the variance (αβ/((α+β)²(α+β+1))) of this distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        double sum = alpha + beta;
        return (alpha * beta) / (sum * sum * (sum + 1.0));
    }

    @Override
    public double density(double x) {
        if (x <= 0.0 || x >= 1.0) {
            throw new IllegalArgumentException("x must be in (0, 1), got: " + x);
        }

        // f(x) = exp((α-1)*ln(x) + (β-1)*ln(1-x) + ln(normalization))
        return Math.exp((alpha - 1.0) * Math.log(x)
                + (beta - 1.0) * Math.log(1.0 - x)
                + logNormalizationFactor);
    }

    @Override
    public double cumulative(double x) {
        if (x < 0.0 || x > 1.0) {
            throw new IllegalArgumentException("x must be in [0, 1], got: " + x);
        }

        if (x == 0.0) {
            return 0.0;
        }
        if (x == 1.0) {
            return 1.0;
        }

        // The cumulative distribution function requires the regularized incomplete
        // beta function I_x(α,β), which is not yet implemented in this library.
        throw new UnsupportedOperationException(
                "Cumulative distribution function not yet implemented for Beta distribution. " +
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
                "Inverse cumulative distribution function not yet implemented for Beta distribution. " +
                        "Requires implementation of the regularized incomplete beta function.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BetaDistribution)) return false;
        BetaDistribution that = (BetaDistribution) o;
        return Double.compare(that.alpha, alpha) == 0 &&
                Double.compare(that.beta, beta) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, beta);
    }

    @Override
    public String toString() {
        return String.format("BetaDistribution(α=%.4f, β=%.4f)", alpha, beta);
    }
}
