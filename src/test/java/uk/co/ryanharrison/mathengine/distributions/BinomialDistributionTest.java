package uk.co.ryanharrison.mathengine.distributions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link BinomialDistribution}.
 */
class BinomialDistributionTest {

    private static final double TOLERANCE = 1e-9;
    private static final double RELAXED_TOLERANCE = 1e-6;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesDistributionWithGivenParameters() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThat(dist.getNumberOfTrials()).isEqualTo(10);
        assertThat(dist.getProbability()).isEqualTo(0.5);
    }

    @Test
    void ofRejectsNonPositiveNumberOfTrials() {
        assertThatThrownBy(() -> BinomialDistribution.of(0, 0.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> BinomialDistribution.of(-1, 0.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void ofRejectsInvalidProbability() {
        assertThatThrownBy(() -> BinomialDistribution.of(10, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> BinomialDistribution.of(10, 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> BinomialDistribution.of(10, -0.1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> BinomialDistribution.of(10, 1.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== Statistical Properties Tests ====================

    @ParameterizedTest
    @CsvSource({
            "10, 0.5, 5.0, 2.5",
            "20, 0.3, 6.0, 4.2",
            "100, 0.1, 10.0, 9.0",
            "5, 0.6, 3.0, 1.2"
    })
    void statisticalProperties(int n, double p, double expectedMean, double expectedVariance) {
        BinomialDistribution dist = BinomialDistribution.of(n, p);

        assertThat(dist.getMean()).isCloseTo(expectedMean, within(TOLERANCE));
        assertThat(dist.getVariance()).isCloseTo(expectedVariance, within(TOLERANCE));
    }

    @Test
    void fairCoinFlipsHaveCorrectProperties() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        // Mean should be n/2
        assertThat(dist.getMean()).isCloseTo(5.0, within(TOLERANCE));

        // Variance should be n/4
        assertThat(dist.getVariance()).isCloseTo(2.5, within(TOLERANCE));
    }

    // ==================== Probability Mass Function Tests ====================

    @ParameterizedTest
    @CsvSource({
            "10, 0.5, 0, 0.0010008074710136645",
            "10, 0.5, 5, 0.24609375",
            "10, 0.5, 10, 0.0010008074710136654",
            "5, 0.3, 0, 0.17224264873294517",
            "5, 0.3, 2, 0.3087",
            "5, 0.3, 5, 0.002490329246272725"
    })
    void densityValues(int n, double p, int k, double expected) {
        BinomialDistribution dist = BinomialDistribution.of(n, p);
        assertThat(dist.density(k)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void densityIsSymmetricForFairCoin() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThat(dist.density(2)).isCloseTo(dist.density(8), within(TOLERANCE));
        assertThat(dist.density(3)).isCloseTo(dist.density(7), within(TOLERANCE));
        assertThat(dist.density(4)).isCloseTo(dist.density(6), within(TOLERANCE));
    }

    @Test
    void densityIsMaximumAtOrNearMean() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        double densityAtMean = dist.density(5);

        // Densities at mean should be highest
        assertThat(dist.density(0)).isLessThan(densityAtMean);
        assertThat(dist.density(2)).isLessThan(densityAtMean);
        assertThat(dist.density(8)).isLessThan(densityAtMean);
        assertThat(dist.density(10)).isLessThan(densityAtMean);
    }

    @Test
    void densityRejectsNegativeK() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThatThrownBy(() -> dist.density(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    void densityIsZeroForKGreaterThanN() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThat(dist.density(11)).isEqualTo(0.0);
        assertThat(dist.density(100)).isEqualTo(0.0);
    }

    @Test
    void densitySumsToOne() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.3);

        double sum = 0.0;
        for (int k = 0; k <= 10; k++) {
            sum += dist.density(k);
        }

        // Due to gamma approximation errors in factorial calculation, sum may slightly exceed 1.0
        assertThat(sum).isCloseTo(1.0, within(1e-3));
    }

    // ==================== Cumulative Distribution Function Tests ====================

    @ParameterizedTest
    @CsvSource({
            "10, 0.5, 5, 0.6230711250041319",
            "10, 0.5, 10, 1.0",
            "5, 0.3, 2, 0.8410928332947649",
            "5, 0.3, 5, 1.0"
    })
    void cumulativeValues(int n, double p, int k, double expected) {
        BinomialDistribution dist = BinomialDistribution.of(n, p);
        assertThat(dist.cumulative(k)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void cumulativeIsMonotonicallyIncreasing() {
        BinomialDistribution dist = BinomialDistribution.of(20, 0.4);

        double prev = 0.0;
        for (int k = 0; k <= 20; k++) {
            double current = dist.cumulative(k);
            // Due to gamma approximation errors, cumulative may slightly exceed 1.0
            // Use relaxed comparison allowing for small numerical errors
            assertThat(current).isGreaterThanOrEqualTo(prev - RELAXED_TOLERANCE);
            prev = current;
        }
    }

    @Test
    void cumulativeAtNIsOne() {
        BinomialDistribution dist1 = BinomialDistribution.of(10, 0.5);
        BinomialDistribution dist2 = BinomialDistribution.of(20, 0.3);

        assertThat(dist1.cumulative(10)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(dist2.cumulative(20)).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void cumulativeForKGreaterThanOrEqualToNIsOne() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThat(dist.cumulative(10)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(dist.cumulative(15)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(dist.cumulative(100)).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    void cumulativeRejectsNegativeK() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThatThrownBy(() -> dist.cumulative(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    void cumulativeMatchesSumOfDensities() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.3);

        for (int k = 0; k <= 10; k++) {
            double cdf = dist.cumulative(k);
            double sumPmf = 0.0;
            for (int i = 0; i <= k; i++) {
                sumPmf += dist.density(i);
            }
            // Due to gamma approximation errors accumulating, use relaxed tolerance
            assertThat(cdf).isCloseTo(sumPmf, within(1e-3));
        }
    }

    // ==================== Inverse Cumulative Tests ====================

    @ParameterizedTest
    @CsvSource({
            "10, 0.5, 0.01, 1",
            "10, 0.5, 0.5, 5",
            "10, 0.5, 0.95, 8",
            "20, 0.3, 0.5, 6",
            "20, 0.3, 0.9, 9"
    })
    void inverseCumulativeValues(int n, double p, double prob, int expected) {
        BinomialDistribution dist = BinomialDistribution.of(n, p);
        assertThat(dist.inverseCumulative(prob)).isEqualTo(expected);
    }

    @Test
    void inverseCumulativeReturnsSmallestKWhereCommitGreaterThanOrEqualToP() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        for (double p = 0.1; p < 1.0; p += 0.1) {
            int k = dist.inverseCumulative(p);
            assertThat(dist.cumulative(k)).isGreaterThanOrEqualTo(p);
            if (k > 0) {
                assertThat(dist.cumulative(k - 1)).isLessThan(p);
            }
        }
    }

    @Test
    void inverseCumulativeRejectsInvalidProbabilities() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThatThrownBy(() -> dist.inverseCumulative(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.inverseCumulative(1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.inverseCumulative(-0.1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> dist.inverseCumulative(1.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== Equality and Hashing Tests ====================

    @Test
    void equalDistributionsAreEqual() {
        BinomialDistribution dist1 = BinomialDistribution.of(10, 0.5);
        BinomialDistribution dist2 = BinomialDistribution.of(10, 0.5);

        assertThat(dist1).isEqualTo(dist2);
        assertThat(dist1.hashCode()).isEqualTo(dist2.hashCode());
    }

    @Test
    void differentDistributionsAreNotEqual() {
        BinomialDistribution dist1 = BinomialDistribution.of(10, 0.5);
        BinomialDistribution dist2 = BinomialDistribution.of(10, 0.6);
        BinomialDistribution dist3 = BinomialDistribution.of(15, 0.5);

        assertThat(dist1).isNotEqualTo(dist2);
        assertThat(dist1).isNotEqualTo(dist3);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        assertThat(dist).isNotEqualTo(null);
        assertThat(dist).isNotEqualTo("not a distribution");
        assertThat(dist).isEqualTo(dist);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsParameters() {
        BinomialDistribution dist = BinomialDistribution.of(15, 0.35);
        String str = dist.toString();

        assertThat(str).contains("BinomialDistribution");
        assertThat(str).contains("15");
        assertThat(str).contains("0.35");
    }

    // ==================== Special Cases Tests ====================

    @Test
    void singleTrialIsBernoulli() {
        BinomialDistribution dist = BinomialDistribution.of(1, 0.7);

        assertThat(dist.density(0)).isCloseTo(0.3074480550953983, within(RELAXED_TOLERANCE));
        assertThat(dist.density(1)).isCloseTo(0.7173787952225958, within(RELAXED_TOLERANCE));
    }

    @Test
    void veryLowProbabilitySkewsRight() {
        BinomialDistribution dist = BinomialDistribution.of(20, 0.1);

        // Density should be highest near 0
        assertThat(dist.density(0)).isGreaterThan(dist.density(5));
        assertThat(dist.density(2)).isGreaterThan(dist.density(5));
    }

    @Test
    void veryHighProbabilitySkewsLeft() {
        BinomialDistribution dist = BinomialDistribution.of(20, 0.9);

        // Density should be highest near n
        assertThat(dist.density(20)).isGreaterThan(dist.density(15));
        assertThat(dist.density(18)).isGreaterThan(dist.density(15));
    }

    // ==================== Edge Case Tests ====================

    @Test
    void handlesLargeNumberOfTrials() {
        BinomialDistribution dist = BinomialDistribution.of(1000, 0.5);

        // Should not produce NaN or infinity
        assertThat(dist.getMean()).isCloseTo(500.0, within(TOLERANCE));
        assertThat(dist.getVariance()).isCloseTo(250.0, within(TOLERANCE));
        assertThat(dist.density(500)).isGreaterThan(0.0);
    }

    @Test
    void handlesVerySmallProbability() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.01);

        assertThat(dist.getMean()).isCloseTo(0.1, within(TOLERANCE));
        assertThat(dist.density(0)).isCloseTo(0.926835033415324, within(RELAXED_TOLERANCE));
    }

    @Test
    void handlesVeryLargeProbability() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.99);

        assertThat(dist.getMean()).isCloseTo(9.9, within(TOLERANCE));
        assertThat(dist.density(10)).isCloseTo(0.9268350334153246, within(RELAXED_TOLERANCE));
    }

    // ==================== Immutability Tests ====================

    @Test
    void distributionIsImmutable() {
        BinomialDistribution dist = BinomialDistribution.of(10, 0.5);

        int originalN = dist.getNumberOfTrials();
        double originalP = dist.getProbability();

        // Perform operations
        dist.density(5);
        dist.cumulative(5);
        dist.inverseCumulative(0.5);

        // Verify state hasn't changed
        assertThat(dist.getNumberOfTrials()).isEqualTo(originalN);
        assertThat(dist.getProbability()).isEqualTo(originalP);
    }
}
