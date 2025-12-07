package uk.co.ryanharrison.mathengine.distributions;

import uk.co.ryanharrison.mathengine.special.Gamma;

import java.util.Objects;

/**
 * Immutable implementation of the Binomial {@link DiscreteProbabilityDistribution}.
 * <p>
 * The binomial distribution models the number of successes in a fixed number of independent
 * Bernoulli trials, each with the same probability of success. It is characterized by two parameters:
 * </p>
 * <ul>
 *     <li><b>n</b>: the number of trials (must be a positive integer)</li>
 *     <li><b>p</b>: the probability of success on each trial (must be in [0, 1])</li>
 * </ul>
 * <p>
 * The probability mass function is:
 * <br>
 * P(X = k) = C(n, k) * p^k * (1-p)^(n-k)
 * </p>
 * <p>
 * where C(n, k) is the binomial coefficient "n choose k".
 * </p>
 * <p>
 * The mean is np and the variance is np(1-p).
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Binomial distribution: 10 coin flips with fair coin
 * BinomialDistribution coinFlips = BinomialDistribution.of(10, 0.5);
 *
 * // Probability of exactly 7 successes
 * double pmf = coinFlips.density(7);
 *
 * // Probability of 5 or fewer successes
 * double cdf = coinFlips.cumulative(5);
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class BinomialDistribution implements DiscreteProbabilityDistribution {

    /**
     * The number of trials.
     */
    private final int numberOfTrials;

    /**
     * The probability of success on each trial.
     */
    private final double probability;

    /**
     * Pre-computed logarithm of n! for efficiency.
     */
    private final double logNFactorial;

    /**
     * Pre-computed logarithm of p for efficiency.
     */
    private final double logProbability;

    /**
     * Pre-computed logarithm of (1-p) for efficiency.
     */
    private final double logOneMinusProbability;

    /**
     * Private constructor to enforce factory method usage.
     *
     * @param numberOfTrials the number of trials
     * @param probability the probability of success
     */
    private BinomialDistribution(int numberOfTrials, double probability) {
        this.numberOfTrials = numberOfTrials;
        this.probability = probability;
        this.logNFactorial = Gamma.gammaLn(numberOfTrials + 1.0);
        this.logProbability = Math.log(probability);
        this.logOneMinusProbability = Math.log(1.0 - probability);
    }

    /**
     * Creates a binomial distribution with the specified number of trials and success probability.
     *
     * @param numberOfTrials the number of trials, must be positive
     * @param probability the probability of success, must be in (0, 1)
     * @return a binomial distribution with the given parameters
     * @throws IllegalArgumentException if numberOfTrials is not positive or probability is not in (0, 1)
     */
    public static BinomialDistribution of(int numberOfTrials, double probability) {
        if (numberOfTrials <= 0) {
            throw new IllegalArgumentException(
                    "Number of trials must be positive, got: " + numberOfTrials);
        }
        if (probability <= 0.0 || probability >= 1.0) {
            throw new IllegalArgumentException(
                    "Probability must be in (0, 1), got: " + probability);
        }
        return new BinomialDistribution(numberOfTrials, probability);
    }

    /**
     * Gets the number of trials (n) of this distribution.
     *
     * @return the number of trials
     */
    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    /**
     * Gets the probability of success (p) for each trial.
     *
     * @return the probability of success
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Gets the mean (np) of this distribution.
     *
     * @return the mean
     */
    public double getMean() {
        return numberOfTrials * probability;
    }

    /**
     * Gets the variance (np(1-p)) of this distribution.
     *
     * @return the variance
     */
    public double getVariance() {
        return numberOfTrials * probability * (1.0 - probability);
    }

    @Override
    public double density(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative, got: " + k);
        }
        if (k > numberOfTrials) {
            return 0.0;
        }

        // P(X = k) = exp(k*ln(p) + (n-k)*ln(1-p) + ln(n!) - ln(k!) - ln((n-k)!))
        return Math.exp(k * logProbability
                + (numberOfTrials - k) * logOneMinusProbability
                + logNFactorial
                - Gamma.gammaLn(k + 1.0)
                - Gamma.gammaLn(numberOfTrials - k + 1.0));
    }

    @Override
    public double cumulative(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k must be non-negative, got: " + k);
        }
        if (k >= numberOfTrials) {
            return 1.0;
        }

        // Sum P(X = i) for i = 0 to k
        double sum = 0.0;
        for (int i = 0; i <= k; i++) {
            sum += density(i);
        }
        return sum;
    }

    @Override
    public int inverseCumulative(double p) {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException("Probability must be in (0, 1), got: " + p);
        }

        // Find the smallest k such that P(X ≤ k) ≥ p
        double cumulativeProb = 0.0;
        for (int k = 0; k <= numberOfTrials; k++) {
            cumulativeProb += density(k);
            if (cumulativeProb >= p) {
                return k;
            }
        }

        // Should never reach here, but return n as fallback
        return numberOfTrials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinomialDistribution)) return false;
        BinomialDistribution that = (BinomialDistribution) o;
        return numberOfTrials == that.numberOfTrials &&
                Double.compare(that.probability, probability) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfTrials, probability);
    }

    @Override
    public String toString() {
        return String.format("BinomialDistribution(n=%d, p=%.4f)", numberOfTrials, probability);
    }
}
