package uk.co.ryanharrison.mathengine.distributions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link LogisticDistribution}.
 */
class LogisticDistributionTest {

    private static final double TOLERANCE = 1e-9;
    private static final double RELAXED_TOLERANCE = 1e-6;

    // ==================== Construction Tests ====================

    @Test
    void standardDistributionHasCorrectParameters() {
        LogisticDistribution dist = LogisticDistribution.standard();

        assertThat(dist.getLocation()).isEqualTo(0.0);
        assertThat(dist.getScale()).isEqualTo(1.0);
        assertThat(dist.getMean()).isEqualTo(0.0);
    }

    @Test
    void ofCreatesDistributionWithGivenParameters() {
        LogisticDistribution dist = LogisticDistribution.of(5.0, 2.0);

        assertThat(dist.getLocation()).isEqualTo(5.0);
        assertThat(dist.getScale()).isEqualTo(2.0);
        assertThat(dist.getMean()).isEqualTo(5.0);
    }

    @Test
    void ofRejectsNonPositiveScale() {
        assertThatThrownBy(() -> LogisticDistribution.of(0.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> LogisticDistribution.of(0.0, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    // ==================== Statistical Properties Tests ====================

    @Test
    void meanEqualsLocation() {
        LogisticDistribution dist1 = LogisticDistribution.of(0.0, 1.0);
        LogisticDistribution dist2 = LogisticDistribution.of(10.0, 3.0);

        assertThat(dist1.getMean()).isEqualTo(0.0);
        assertThat(dist2.getMean()).isEqualTo(10.0);
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.0, 3.289868133696453",
            "0.0, 2.0, 13.15947253478581",
            "5.0, 1.0, 3.289868133696453",
            "10.0, 3.0, 29.608813203468"
    })
    void varianceCalculation(double location, double scale, double expectedVariance) {
        LogisticDistribution dist = LogisticDistribution.of(location, scale);
        assertThat(dist.getVariance()).isCloseTo(expectedVariance, within(TOLERANCE));
    }

    // ==================== Probability Density Function Tests ====================

    @Test
    void densityAtLocationIsMaximum() {
        LogisticDistribution dist1 = LogisticDistribution.standard();
        LogisticDistribution dist2 = LogisticDistribution.of(5.0, 2.0);

        double densityAtLocation1 = dist1.density(dist1.getLocation());
        double densityAtLocation2 = dist2.density(dist2.getLocation());

        assertThat(dist1.density(1.0)).isLessThan(densityAtLocation1);
        assertThat(dist1.density(-1.0)).isLessThan(densityAtLocation1);

        assertThat(dist2.density(6.0)).isLessThan(densityAtLocation2);
        assertThat(dist2.density(4.0)).isLessThan(densityAtLocation2);
    }

    @Test
    void densityIsSymmetricAroundLocation() {
        LogisticDistribution dist = LogisticDistribution.of(5.0, 2.0);

        assertThat(dist.density(5.0 + 1.0)).isCloseTo(dist.density(5.0 - 1.0), within(TOLERANCE));
        assertThat(dist.density(5.0 + 2.5)).isCloseTo(dist.density(5.0 - 2.5), within(TOLERANCE));
        assertThat(dist.density(5.0 + 10.0)).isCloseTo(dist.density(5.0 - 10.0), within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.45344984105855446",
            "1.0, 0.2186158850951135",
            "-1.0, 0.2186158850951135",
            "2.0, 0.04574647059548827"
    })
    void standardLogisticDensityValues(double x, double expected) {
        LogisticDistribution dist = LogisticDistribution.standard();
        assertThat(dist.density(x)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    // ==================== Cumulative Distribution Function Tests ====================

    @Test
    void cumulativeAtLocationIsHalf() {
        LogisticDistribution dist1 = LogisticDistribution.of(0.0, 1.0);
        LogisticDistribution dist2 = LogisticDistribution.of(10.0, 3.0);
        LogisticDistribution dist3 = LogisticDistribution.of(-5.0, 0.5);

        assertThat(dist1.cumulative(0.0)).isCloseTo(0.5, within(TOLERANCE));
        assertThat(dist2.cumulative(10.0)).isCloseTo(0.5, within(TOLERANCE));
        assertThat(dist3.cumulative(-5.0)).isCloseTo(0.5, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 0.5",
            "1.0, 0.8598204351462735",
            "-1.0, 0.14017956485372646",
            "2.0, 0.9741082670626448",
            "-2.0, 0.025891732937355368"
    })
    void standardLogisticCumulativeValues(double x, double expected) {
        LogisticDistribution dist = LogisticDistribution.standard();
        assertThat(dist.cumulative(x)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void cumulativeIsMonotonicallyIncreasing() {
        LogisticDistribution dist = LogisticDistribution.of(5.0, 2.0);

        double prev = 0.0;
        for (double x = -10.0; x <= 20.0; x += 0.5) {
            double current = dist.cumulative(x);
            assertThat(current).isGreaterThanOrEqualTo(prev);
            prev = current;
        }
    }

    @Test
    void cumulativeApproachesZeroAndOne() {
        LogisticDistribution dist = LogisticDistribution.standard();

        assertThat(dist.cumulative(-10.0)).isCloseTo(0.0, within(1e-4));
        assertThat(dist.cumulative(10.0)).isCloseTo(1.0, within(1e-4));
    }

    // ==================== Inverse Cumulative Tests ====================

    @ParameterizedTest
    @CsvSource({
            "0.1, -1.2113933992163917",
            "0.25, -0.6056966996081959",
            "0.5, 0.0",
            "0.75, 0.6056966996081959",
            "0.9, 1.2113933992163919"
    })
    void standardLogisticInverseCumulativeValues(double p, double expected) {
        LogisticDistribution dist = LogisticDistribution.standard();
        assertThat(dist.inverseCumulative(p)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void inverseCumulativeIsInverseOfCumulative() {
        LogisticDistribution dist = LogisticDistribution.of(10.0, 3.0);

        double[] probabilities = {0.01, 0.1, 0.25, 0.5, 0.75, 0.9, 0.99};
        for (double p : probabilities) {
            double x = dist.inverseCumulative(p);
            double recoveredP = dist.cumulative(x);
            assertThat(recoveredP).isCloseTo(p, within(1e-10));
        }
    }

    @Test
    void inverseCumulativeRejectsInvalidProbabilities() {
        LogisticDistribution dist = LogisticDistribution.standard();

        assertThatThrownBy(() -> dist.inverseCumulative(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.inverseCumulative(1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");
    }

    @Test
    void inverseCumulativeAtHalfIsLocation() {
        LogisticDistribution dist1 = LogisticDistribution.of(0.0, 1.0);
        LogisticDistribution dist2 = LogisticDistribution.of(100.0, 15.0);
        LogisticDistribution dist3 = LogisticDistribution.of(-25.0, 5.0);

        assertThat(dist1.inverseCumulative(0.5)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(dist2.inverseCumulative(0.5)).isCloseTo(100.0, within(TOLERANCE));
        assertThat(dist3.inverseCumulative(0.5)).isCloseTo(-25.0, within(TOLERANCE));
    }

    // ==================== Equality and Hashing Tests ====================

    @Test
    void equalDistributionsAreEqual() {
        LogisticDistribution dist1 = LogisticDistribution.of(5.0, 2.0);
        LogisticDistribution dist2 = LogisticDistribution.of(5.0, 2.0);

        assertThat(dist1).isEqualTo(dist2);
        assertThat(dist1.hashCode()).isEqualTo(dist2.hashCode());
    }

    @Test
    void differentDistributionsAreNotEqual() {
        LogisticDistribution dist1 = LogisticDistribution.of(5.0, 2.0);
        LogisticDistribution dist2 = LogisticDistribution.of(5.0, 3.0);
        LogisticDistribution dist3 = LogisticDistribution.of(6.0, 2.0);

        assertThat(dist1).isNotEqualTo(dist2);
        assertThat(dist1).isNotEqualTo(dist3);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        LogisticDistribution dist = LogisticDistribution.standard();

        assertThat(dist).isNotEqualTo(null);
        assertThat(dist).isNotEqualTo("not a distribution");
        assertThat(dist).isEqualTo(dist);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsParameters() {
        LogisticDistribution dist = LogisticDistribution.of(10.5, 2.3);
        String str = dist.toString();

        assertThat(str).contains("LogisticDistribution");
        assertThat(str).contains("10.5");
        assertThat(str).contains("2.3");
    }

    // ==================== Edge Case Tests ====================

    @Test
    void handlesVerySmallScale() {
        LogisticDistribution dist = LogisticDistribution.of(0.0, 0.01);

        // Distribution should be very peaked at the location
        assertThat(dist.density(0.0)).isGreaterThan(10.0);
        assertThat(dist.density(1.0)).isCloseTo(0.0, within(1e-10));
    }

    @Test
    void handlesVeryLargeScale() {
        LogisticDistribution dist = LogisticDistribution.of(0.0, 100.0);

        // Distribution should be very flat
        assertThat(dist.density(0.0)).isLessThan(0.01);
    }

    @Test
    void handlesExtremeValues() {
        LogisticDistribution dist = LogisticDistribution.standard();

        // Should not produce NaN or infinity
        assertThat(dist.density(100.0)).isGreaterThan(0.0);
        assertThat(dist.density(-100.0)).isGreaterThan(0.0);
        assertThat(dist.cumulative(100.0)).isLessThanOrEqualTo(1.0);
        assertThat(dist.cumulative(-100.0)).isGreaterThanOrEqualTo(0.0);
    }

    // ==================== Immutability Tests ====================

    @Test
    void distributionIsImmutable() {
        LogisticDistribution dist = LogisticDistribution.of(5.0, 2.0);

        double originalLocation = dist.getLocation();
        double originalScale = dist.getScale();

        // Perform operations
        dist.density(10.0);
        dist.cumulative(10.0);
        dist.inverseCumulative(0.5);

        // Verify state hasn't changed
        assertThat(dist.getLocation()).isEqualTo(originalLocation);
        assertThat(dist.getScale()).isEqualTo(originalScale);
    }
}
