package uk.co.ryanharrison.mathengine.distributions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link FDistribution}.
 */
class FDistributionTest {

    private static final double TOLERANCE = 1e-9;
    private static final double RELAXED_TOLERANCE = 1e-6;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesDistributionWithGivenParameters() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThat(dist.getDegreesOfFreedom1()).isEqualTo(5.0);
        assertThat(dist.getDegreesOfFreedom2()).isEqualTo(10.0);
    }

    @Test
    void ofRejectsNonPositiveDegreesOfFreedom1() {
        assertThatThrownBy(() -> FDistribution.of(0.0, 10.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> FDistribution.of(-1.0, 10.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void ofRejectsNonPositiveDegreesOfFreedom2() {
        assertThatThrownBy(() -> FDistribution.of(5.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> FDistribution.of(5.0, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    // ==================== Statistical Properties Tests ====================

    @ParameterizedTest
    @CsvSource({
            "5.0, 10.0, 1.25",
            "3.0, 8.0, 1.333333333333333",
            "10.0, 20.0, 1.111111111111111"
    })
    void meanForValidDegreesOfFreedom(double d1, double d2, double expectedMean) {
        FDistribution dist = FDistribution.of(d1, d2);
        assertThat(dist.getMean()).isCloseTo(expectedMean, within(TOLERANCE));
    }

    @Test
    void meanIsUndefinedForDf2LessThanOrEqualTo2() {
        FDistribution dist1 = FDistribution.of(5.0, 2.0);
        FDistribution dist2 = FDistribution.of(5.0, 1.0);

        assertThatThrownBy(dist1::getMean)
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("undefined");

        assertThatThrownBy(dist2::getMean)
                .isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "5.0, 10.0, 1.3541666666666667",
            "3.0, 8.0, 2.6666666666666665",
            "10.0, 20.0, 0.43209876543209874"
    })
    void varianceForValidDegreesOfFreedom(double d1, double d2, double expectedVariance) {
        FDistribution dist = FDistribution.of(d1, d2);
        assertThat(dist.getVariance()).isCloseTo(expectedVariance, within(RELAXED_TOLERANCE));
    }

    @Test
    void varianceIsUndefinedForDf2LessThanOrEqualTo4() {
        FDistribution dist1 = FDistribution.of(5.0, 4.0);
        FDistribution dist2 = FDistribution.of(5.0, 3.0);

        assertThatThrownBy(dist1::getVariance)
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("undefined");

        assertThatThrownBy(dist2::getVariance)
                .isInstanceOf(ArithmeticException.class);
    }

    // ==================== Probability Density Function Tests ====================

    @ParameterizedTest
    @CsvSource({
            "5.0, 10.0, 1.0, 0.49547979076859555",
            "5.0, 10.0, 2.0, 0.16200574456107747",
            "10.0, 5.0, 0.5, 0.6480229782443077",
            "3.0, 8.0, 1.5, 0.23781703679999944"
    })
    void densityValues(double d1, double d2, double x, double expected) {
        FDistribution dist = FDistribution.of(d1, d2);
        assertThat(dist.density(x)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void densityRejectsNonPositiveX() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThatThrownBy(() -> dist.density(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> dist.density(-1.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void densityIsPositiveForPositiveX() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThat(dist.density(0.1)).isGreaterThan(0.0);
        assertThat(dist.density(1.0)).isGreaterThan(0.0);
        assertThat(dist.density(5.0)).isGreaterThan(0.0);
    }

    // ==================== Cumulative Distribution Function Tests ====================

    @Test
    void cumulativeAtZeroIsZero() {
        FDistribution dist = FDistribution.of(5.0, 10.0);
        assertThat(dist.cumulative(0.0)).isEqualTo(0.0);
    }

    @Test
    void cumulativeRejectsNegativeX() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThatThrownBy(() -> dist.cumulative(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    void cumulativeThrowsUnsupportedOperationForPositiveX() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        // CDF not yet implemented for positive values
        assertThatThrownBy(() -> dist.cumulative(1.0))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("not yet implemented");
    }

    // ==================== Inverse Cumulative Tests ====================

    @Test
    void inverseCumulativeThrowsUnsupportedOperation() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThatThrownBy(() -> dist.inverseCumulative(0.5))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("not yet implemented");
    }

    @Test
    void inverseCumulativeRejectsInvalidProbabilities() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThatThrownBy(() -> dist.inverseCumulative(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.inverseCumulative(1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");
    }

    // ==================== Equality and Hashing Tests ====================

    @Test
    void equalDistributionsAreEqual() {
        FDistribution dist1 = FDistribution.of(5.0, 10.0);
        FDistribution dist2 = FDistribution.of(5.0, 10.0);

        assertThat(dist1).isEqualTo(dist2);
        assertThat(dist1.hashCode()).isEqualTo(dist2.hashCode());
    }

    @Test
    void differentDistributionsAreNotEqual() {
        FDistribution dist1 = FDistribution.of(5.0, 10.0);
        FDistribution dist2 = FDistribution.of(5.0, 15.0);
        FDistribution dist3 = FDistribution.of(10.0, 10.0);

        assertThat(dist1).isNotEqualTo(dist2);
        assertThat(dist1).isNotEqualTo(dist3);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        assertThat(dist).isNotEqualTo(null);
        assertThat(dist).isNotEqualTo("not a distribution");
        assertThat(dist).isEqualTo(dist);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsParameters() {
        FDistribution dist = FDistribution.of(5.5, 10.3);
        String str = dist.toString();

        assertThat(str).contains("FDistribution");
        assertThat(str).contains("5.5");
        assertThat(str).contains("10.3");
    }

    // ==================== Edge Case Tests ====================

    @Test
    void handlesLargeDegreesOfFreedom() {
        FDistribution dist = FDistribution.of(100.0, 200.0);

        // Should not produce NaN or infinity
        assertThat(dist.getMean()).isGreaterThan(0.0);
        assertThat(dist.getVariance()).isGreaterThan(0.0);
        assertThat(dist.density(1.0)).isGreaterThan(0.0);
    }

    @Test
    void handlesSmallDegreesOfFreedom() {
        FDistribution dist = FDistribution.of(1.0, 1.0);

        // Should not produce NaN or infinity
        assertThat(dist.density(1.0)).isGreaterThan(0.0);
        assertThat(dist.density(5.0)).isGreaterThan(0.0);
    }

    @Test
    void handlesVeryLargeX() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        // Density should approach 0 as x increases
        assertThat(dist.density(100.0)).isLessThan(dist.density(10.0));
        assertThat(dist.density(100.0)).isGreaterThan(0.0);
    }

    // ==================== Immutability Tests ====================

    @Test
    void distributionIsImmutable() {
        FDistribution dist = FDistribution.of(5.0, 10.0);

        double originalDf1 = dist.getDegreesOfFreedom1();
        double originalDf2 = dist.getDegreesOfFreedom2();

        // Perform operations
        dist.density(2.0);
        dist.getMean();

        // Verify state hasn't changed
        assertThat(dist.getDegreesOfFreedom1()).isEqualTo(originalDf1);
        assertThat(dist.getDegreesOfFreedom2()).isEqualTo(originalDf2);
    }
}
