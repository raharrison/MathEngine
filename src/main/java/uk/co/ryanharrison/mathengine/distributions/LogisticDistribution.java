package uk.co.ryanharrison.mathengine.distributions;

/**
 * Class representing the Logistic {@link ProbabilityDistribution}
 *
 * @author Ryan Harrison
 *
 */
public final class LogisticDistribution extends ContinuousProbabilityDistribution {
    /**
     * Variables of the distribution
     */
    private double mu, sig;

    /**
     * Construct a new {@link LogisticDistribution} object.
     * <p>
     * Values of mu and sigma default to zero and one respectively
     */
    public LogisticDistribution() {
        this(0.0, 1.0);
    }

    /**
     * Construct a new {@link LogisticDistribution} object with specified values
     * of mu and sigma
     *
     * @param mu  The mean
     * @param sig The standard deviation
     * @throws IllegalArgumentException If the standard deviation is less than or equal to zero
     */
    public LogisticDistribution(double mu, double sig) {
        if (sig <= 0.0)
            throw new IllegalArgumentException("Standard deviation must be greater than 0");

        this.mu = mu;
        this.sig = sig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double cumulative(double x) {
        double e = Math.exp(-Math.abs(1.81379936423421785 * (x - mu) / sig));
        if (x >= mu)
            return 1.0 / (1.0 + e);
        return e / (1.0 + e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double density(double x) {
        double e = Math.exp(-Math.abs(1.81379936423421785 * (x - mu) / sig));
        return 1.81379936423421785 * e / (sig * Math.pow(1.0 + e, 2.0));
    }

    /**
     * {@inheritDoc}
     *
     * @throws UnsupportedOperationException If p is not between 0 and 1
     */
    @Override
    public double inverseCumulative(double p) {
        if (p <= 0.0 || p >= 1.0)
            throw new IllegalArgumentException("p must be between 0 and 1");

        return mu + 0.551328895421792049 * sig * Math.log(p / (1.0 - p));
    }
}
