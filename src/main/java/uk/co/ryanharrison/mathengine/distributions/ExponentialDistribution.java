package uk.co.ryanharrison.mathengine.distributions;

/**
 * Class representing the Exponential {@link ProbabilityDistribution}
 *
 * @author Ryan Harrison
 *
 */
public final class ExponentialDistribution extends ContinuousProbabilityDistribution {
    /**
     * The value of beta
     */
    private double bet;

    /**
     * Construct a new instance of {@link ExponentialDistribution} with the
     * specified value of beta
     *
     * @param bet The value of beta
     * @throws IllegalArgumentException If beta is not greater than zero
     */
    public ExponentialDistribution(double bet) {
        if (bet <= 0.0)
            throw new IllegalArgumentException("bet must be greater than 0");

        this.bet = bet;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If x is not greater than zero
     */
    @Override
    public double cumulative(double x) {
        if (x < 0.0)
            throw new IllegalArgumentException("x must be greater than 0");
        return 1.0 - Math.exp(-bet * x);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If x is not greater than zero
     */
    @Override
    public double density(double x) {
        if (x < 0.0)
            throw new IllegalArgumentException("x must be greater than 0");
        return bet * Math.exp(-bet * x);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If p is not between zero and one
     */
    @Override
    public double inverseCumulative(double p) {
        if (p < 0.0 || p >= 1.0)
            throw new IllegalArgumentException("p must be between 0 and 1");
        return -Math.log(1.0 - p) / bet;
    }
}
