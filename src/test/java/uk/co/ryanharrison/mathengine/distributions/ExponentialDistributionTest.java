package uk.co.ryanharrison.mathengine.distributions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link ExponentialDistribution}.
 */
class ExponentialDistributionTest {

    private static final double TOLERANCE = 1e-9;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesDistributionWithGivenRate() {
        ExponentialDistribution dist = ExponentialDistribution.of(0.5);

        assertThat(dist.getRate()).isEqualTo(0.5);
        assertThat(dist.getMean()).isCloseTo(2.0, within(TOLERANCE));
        assertThat(dist.getVariance()).isCloseTo(4.0, within(TOLERANCE));
    }

    @Test
    void withMeanCreatesDistributionWithGivenMean() {
        ExponentialDistribution dist = ExponentialDistribution.withMean(3.0);

        assertThat(dist.getMean()).isCloseTo(3.0, within(TOLERANCE));
        assertThat(dist.getRate()).isCloseTo(1.0 / 3.0, within(TOLERANCE));
    }

    @Test
    void ofRejectsNonPositiveRate() {
        assertThatThrownBy(() -> ExponentialDistribution.of(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> ExponentialDistribution.of(-1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void withMeanRejectsNonPositiveMean() {
        assertThatThrownBy(() -> ExponentialDistribution.withMean(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> ExponentialDistribution.withMean(-2.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    // ==================== Statistical Properties Tests ====================

    @Test
    void meanAndVarianceRelationship() {
        ExponentialDistribution dist = ExponentialDistribution.of(2.0);

        double mean = dist.getMean();
        double variance = dist.getVariance();

        // For exponential distribution: variance = mean²
        assertThat(variance).isCloseTo(mean * mean, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "0.5, 2.0, 4.0",
            "1.0, 1.0, 1.0",
            "2.0, 0.5, 0.25",
            "0.1, 10.0, 100.0"
    })
    void statisticalPropertiesForDifferentRates(double rate, double expectedMean, double expectedVariance) {
        ExponentialDistribution dist = ExponentialDistribution.of(rate);

        assertThat(dist.getMean()).isCloseTo(expectedMean, within(TOLERANCE));
        assertThat(dist.getVariance()).isCloseTo(expectedVariance, within(TOLERANCE));
    }

    // ==================== Probability Density Function Tests ====================

    @Test
    void densityAtZeroEqualsRate() {
        ExponentialDistribution dist1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution dist2 = ExponentialDistribution.of(2.0);

        assertThat(dist1.density(0.0)).isCloseTo(0.5, within(TOLERANCE));
        assertThat(dist2.density(0.0)).isCloseTo(2.0, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 0.0, 1.0",
            "1.0, 1.0, 0.367879441171442",
            "1.0, 2.0, 0.135335283236613",
            "0.5, 2.0, 0.183939720585721",
            "2.0, 1.0, 0.270670566473226"
    })
    void densityValues(double rate, double x, double expected) {
        ExponentialDistribution dist = ExponentialDistribution.of(rate);
        assertThat(dist.density(x)).isCloseTo(expected, within(TOLERANCE));
    }

    @Test
    void densityIsStrictlyDecreasing() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        double prev = dist.density(0.0);
        for (double x = 0.1; x <= 10.0; x += 0.1) {
            double current = dist.density(x);
            assertThat(current).isLessThan(prev);
            prev = current;
        }
    }

    @Test
    void densityApproachesZeroAsXIncreases() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        assertThat(dist.density(10.0)).isLessThan(0.0001);
        assertThat(dist.density(20.0)).isLessThan(0.00000001);
    }

    @Test
    void densityRejectsNegativeX() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        assertThatThrownBy(() -> dist.density(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");

        assertThatThrownBy(() -> dist.density(-5.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== Cumulative Distribution Function Tests ====================

    @ParameterizedTest
    @CsvSource({
            "1.0, 0.0, 0.0",
            "1.0, 1.0, 0.632120558828558",
            "1.0, 2.0, 0.864664716763387",
            "1.0, 5.0, 0.993262053000914",
            "0.5, 2.0, 0.632120558828558",
            "2.0, 0.5, 0.632120558828558"
    })
    void cumulativeValues(double rate, double x, double expected) {
        ExponentialDistribution dist = ExponentialDistribution.of(rate);
        assertThat(dist.cumulative(x)).isCloseTo(expected, within(TOLERANCE));
    }

    @Test
    void cumulativeAtZeroIsZero() {
        ExponentialDistribution dist1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution dist2 = ExponentialDistribution.of(2.0);

        assertThat(dist1.cumulative(0.0)).isEqualTo(0.0);
        assertThat(dist2.cumulative(0.0)).isEqualTo(0.0);
    }

    @Test
    void cumulativeIsMonotonicallyIncreasing() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        double prev = 0.0;
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double current = dist.cumulative(x);
            assertThat(current).isGreaterThanOrEqualTo(prev);
            prev = current;
        }
    }

    @Test
    void cumulativeApproachesOne() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        assertThat(dist.cumulative(10.0)).isCloseTo(1.0, within(0.0001));
        assertThat(dist.cumulative(20.0)).isCloseTo(1.0, within(1e-8));
    }

    @Test
    void cumulativeRejectsNegativeX() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        assertThatThrownBy(() -> dist.cumulative(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    void cumulativeAtMeanIsApproximately63Percent() {
        ExponentialDistribution dist1 = ExponentialDistribution.of(0.5);
        ExponentialDistribution dist2 = ExponentialDistribution.of(2.0);

        // CDF at mean (1/λ) is 1 - e^(-1) ≈ 0.632
        assertThat(dist1.cumulative(dist1.getMean())).isCloseTo(0.632120558828558, within(TOLERANCE));
        assertThat(dist2.cumulative(dist2.getMean())).isCloseTo(0.632120558828558, within(TOLERANCE));
    }

    // ==================== Inverse Cumulative Tests ====================

    @ParameterizedTest
    @CsvSource({
            "1.0, 0.1, 0.105360515657826",
            "1.0, 0.5, 0.693147180559945",
            "1.0, 0.9, 2.302585092994046",
            "0.5, 0.5, 1.386294361119891",
            "2.0, 0.5, 0.346573590279973"
    })
    void inverseCumulativeValues(double rate, double p, double expected) {
        ExponentialDistribution dist = ExponentialDistribution.of(rate);
        assertThat(dist.inverseCumulative(p)).isCloseTo(expected, within(TOLERANCE));
    }

    @Test
    void inverseCumulativeIsInverseOfCumulative() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.5);

        double[] probabilities = {0.01, 0.1, 0.25, 0.5, 0.75, 0.9, 0.99};
        for (double p : probabilities) {
            double x = dist.inverseCumulative(p);
            double recoveredP = dist.cumulative(x);
            assertThat(recoveredP).isCloseTo(p, within(1e-10));
        }
    }

    @Test
    void inverseCumulativeRejectsInvalidProbabilities() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

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

    @Test
    void inverseCumulativeAtHalfGivesMedian() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        // Median of exponential(λ) is ln(2)/λ
        double median = dist.inverseCumulative(0.5);
        assertThat(median).isCloseTo(Math.log(2.0), within(TOLERANCE));
    }

    // ==================== Equality and Hashing Tests ====================

    @Test
    void equalDistributionsAreEqual() {
        ExponentialDistribution dist1 = ExponentialDistribution.of(1.5);
        ExponentialDistribution dist2 = ExponentialDistribution.of(1.5);

        assertThat(dist1).isEqualTo(dist2);
        assertThat(dist1.hashCode()).isEqualTo(dist2.hashCode());
    }

    @Test
    void differentDistributionsAreNotEqual() {
        ExponentialDistribution dist1 = ExponentialDistribution.of(1.0);
        ExponentialDistribution dist2 = ExponentialDistribution.of(2.0);

        assertThat(dist1).isNotEqualTo(dist2);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        assertThat(dist).isNotEqualTo(null);
        assertThat(dist).isNotEqualTo("not a distribution");
        assertThat(dist).isEqualTo(dist);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsParameters() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.5);
        String str = dist.toString();

        assertThat(str).contains("ExponentialDistribution");
        assertThat(str).contains("1.5");
    }

    // ==================== Memoryless Property Tests ====================

    @Test
    void memorylessPropertyHolds() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        double s = 2.0;
        double t = 3.0;

        // P(X > s + t | X > s) = P(X > t)
        // Which means: (1 - CDF(s+t)) / (1 - CDF(s)) = 1 - CDF(t)

        double probGivenSurvival = (1.0 - dist.cumulative(s + t)) / (1.0 - dist.cumulative(s));
        double unconditionalProb = 1.0 - dist.cumulative(t);

        assertThat(probGivenSurvival).isCloseTo(unconditionalProb, within(1e-10));
    }

    // ==================== Edge Case Tests ====================

    @Test
    void handlesVerySmallRate() {
        ExponentialDistribution dist = ExponentialDistribution.of(0.001);

        // Mean should be very large
        assertThat(dist.getMean()).isCloseTo(1000.0, within(TOLERANCE));

        // Density at 0 should be very small
        assertThat(dist.density(0.0)).isCloseTo(0.001, within(TOLERANCE));
    }

    @Test
    void handlesVeryLargeRate() {
        ExponentialDistribution dist = ExponentialDistribution.of(100.0);

        // Mean should be very small
        assertThat(dist.getMean()).isCloseTo(0.01, within(TOLERANCE));

        // Density at 0 should be very large
        assertThat(dist.density(0.0)).isCloseTo(100.0, within(TOLERANCE));
    }

    @Test
    void handlesVeryLargeX() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.0);

        // Should not produce NaN or infinity
        assertThat(dist.density(100.0)).isGreaterThan(0.0);
        assertThat(dist.cumulative(100.0)).isLessThanOrEqualTo(1.0);
        assertThat(dist.cumulative(100.0)).isCloseTo(1.0, within(1e-40));
    }

    // ==================== Immutability Tests ====================

    @Test
    void distributionIsImmutable() {
        ExponentialDistribution dist = ExponentialDistribution.of(1.5);

        double originalRate = dist.getRate();

        // Perform operations
        dist.density(5.0);
        dist.cumulative(5.0);
        dist.inverseCumulative(0.5);

        // Verify state hasn't changed
        assertThat(dist.getRate()).isEqualTo(originalRate);
    }
}
