package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

/**
 * Class representing the Beta {@link ProbabilityDistribution}
 *
 * @author Ryan Harrison
 *
 */
public final class BetaDistribution extends ContinuousProbabilityDistribution {
    /**
     * Variables of the distribution
     */
    private double alph, bet, fac;

    /**
     * Construct a new instance of {@link BetaDistribution} with the specified
     * values of alpha and beta
     *
     * @param alph The value of alpha
     * @param bet  The value of beta
     * @throws IllegalArgumentException If alpha or beta are less than or equal to zero
     */
    public BetaDistribution(double alph, double bet) {
        if (alph <= 0.0 || bet <= 0.0)
            throw new IllegalArgumentException("alph and bet must be greater than 0");

        this.alph = alph;
        this.bet = bet;

        fac = Gamma.gammaLn(alph + bet) - Gamma.gammaLn(alph) - Gamma.gammaLn(bet);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If x is not between zero and one
     */
    @Override
    public double cumulative(double x) {
        if (x < 0.0 || x > 1.0)
            throw new IllegalArgumentException("x must be between 0 and 1");

        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If x is not between zero and one
     */
    @Override
    public double density(double x) {
        if (x <= 0.0 || x >= 1.0)
            throw new IllegalArgumentException("x must be between 0 and 1");
        return Math.exp((alph - 1.0) * Math.log(x) + (bet - 1.0) * Math.log(1.0 - x) + fac);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException If p is not between zero and one
     */
    @Override
    public double inverseCumulative(double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("p must be between 0 and 1");

        throw new UnsupportedOperationException("Not implemented");
    }
}
