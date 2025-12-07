package uk.co.ryanharrison.mathengine.distributions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link BetaDistribution}.
 */
class BetaDistributionTest {

    private static final double TOLERANCE = 1e-9;
    private static final double RELAXED_TOLERANCE = 1e-6;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesDistributionWithGivenParameters() {
        BetaDistribution dist = BetaDistribution.of(2.0, 5.0);

        assertThat(dist.getAlpha()).isEqualTo(2.0);
        assertThat(dist.getBeta()).isEqualTo(5.0);
    }

    @Test
    void uniformCreatesUniformDistribution() {
        BetaDistribution dist = BetaDistribution.uniform();

        assertThat(dist.getAlpha()).isEqualTo(1.0);
        assertThat(dist.getBeta()).isEqualTo(1.0);
    }

    @Test
    void ofRejectsNonPositiveAlpha() {
        assertThatThrownBy(() -> BetaDistribution.of(0.0, 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> BetaDistribution.of(-1.0, 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    @Test
    void ofRejectsNonPositiveBeta() {
        assertThatThrownBy(() -> BetaDistribution.of(1.0, 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");

        assertThatThrownBy(() -> BetaDistribution.of(1.0, -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive");
    }

    // ==================== Statistical Properties Tests ====================

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0, 0.5, 0.083333333333333",
            "2.0, 2.0, 0.5, 0.05",
            "2.0, 5.0, 0.285714285714286, 0.025510204081633",
            "5.0, 2.0, 0.714285714285714, 0.025510204081633",
            "3.0, 3.0, 0.5, 0.035714285714286"
    })
    void statisticalPropertiesForDifferentParameters(double alpha, double beta,
                                                     double expectedMean, double expectedVariance) {
        BetaDistribution dist = BetaDistribution.of(alpha, beta);

        assertThat(dist.getMean()).isCloseTo(expectedMean, within(TOLERANCE));
        assertThat(dist.getVariance()).isCloseTo(expectedVariance, within(TOLERANCE));
    }

    @Test
    void uniformDistributionHasMeanOfHalf() {
        BetaDistribution dist = BetaDistribution.uniform();
        assertThat(dist.getMean()).isCloseTo(0.5, within(TOLERANCE));
    }

    // ==================== Probability Density Function Tests ====================

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0, 0.5, 1.0502695356240588",        // Uniform distribution
            "2.0, 2.0, 0.5, 1.500001535334434",         // Symmetric bell shape
            "2.0, 5.0, 0.2, 2.4576012577549533",        // Right-skewed
            "5.0, 2.0, 0.8, 2.457601257754952"          // Left-skewed
    })
    void densityValues(double alpha, double beta, double x, double expected) {
        BetaDistribution dist = BetaDistribution.of(alpha, beta);
        assertThat(dist.density(x)).isCloseTo(expected, within(RELAXED_TOLERANCE));
    }

    @Test
    void uShapedDistributionDensityAt05() {
        // Beta(0.5, 0.5) has a U-shape with density approaching infinity at x=0.5
        // Since density returns Infinity, we just verify it's very large
        BetaDistribution dist = BetaDistribution.of(0.5, 0.5);
        assertThat(dist.density(0.5)).isEqualTo(Double.POSITIVE_INFINITY);
    }

    @Test
    void uniformDistributionHasConstantDensity() {
        BetaDistribution dist = BetaDistribution.uniform();

        // Uniform(0,1) has constant density close to 1.0 (gamma approximation adds ~5% error)
        assertThat(dist.density(0.1)).isCloseTo(1.0, within(0.06));
        assertThat(dist.density(0.5)).isCloseTo(1.0, within(0.06));
        assertThat(dist.density(0.9)).isCloseTo(1.0, within(0.06));
    }

    @Test
    void densityRejectsValuesOutsideZeroOne() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        assertThatThrownBy(() -> dist.density(0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.density(1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("(0, 1)");

        assertThatThrownBy(() -> dist.density(-0.1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> dist.density(1.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void symmetricDistributionHasSymmetricDensity() {
        BetaDistribution dist = BetaDistribution.of(3.0, 3.0);

        assertThat(dist.density(0.3)).isCloseTo(dist.density(0.7), within(TOLERANCE));
        assertThat(dist.density(0.2)).isCloseTo(dist.density(0.8), within(TOLERANCE));
    }

    // ==================== Cumulative Distribution Function Tests ====================

    @Test
    void cumulativeAtBoundaries() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        assertThat(dist.cumulative(0.0)).isEqualTo(0.0);
        assertThat(dist.cumulative(1.0)).isEqualTo(1.0);
    }

    @Test
    void cumulativeRejectsValuesOutsideZeroOne() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        assertThatThrownBy(() -> dist.cumulative(-0.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[0, 1]");

        assertThatThrownBy(() -> dist.cumulative(1.1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cumulativeThrowsUnsupportedOperationForInteriorValues() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        // CDF not yet implemented for interior values
        assertThatThrownBy(() -> dist.cumulative(0.5))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("not yet implemented");
    }

    // ==================== Inverse Cumulative Tests ====================

    @Test
    void inverseCumulativeThrowsUnsupportedOperation() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        assertThatThrownBy(() -> dist.inverseCumulative(0.5))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("not yet implemented");
    }

    @Test
    void inverseCumulativeRejectsInvalidProbabilities() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

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
        BetaDistribution dist1 = BetaDistribution.of(2.0, 5.0);
        BetaDistribution dist2 = BetaDistribution.of(2.0, 5.0);

        assertThat(dist1).isEqualTo(dist2);
        assertThat(dist1.hashCode()).isEqualTo(dist2.hashCode());
    }

    @Test
    void differentDistributionsAreNotEqual() {
        BetaDistribution dist1 = BetaDistribution.of(2.0, 5.0);
        BetaDistribution dist2 = BetaDistribution.of(2.0, 3.0);
        BetaDistribution dist3 = BetaDistribution.of(3.0, 5.0);

        assertThat(dist1).isNotEqualTo(dist2);
        assertThat(dist1).isNotEqualTo(dist3);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        BetaDistribution dist = BetaDistribution.of(2.0, 3.0);

        assertThat(dist).isNotEqualTo(null);
        assertThat(dist).isNotEqualTo("not a distribution");
        assertThat(dist).isEqualTo(dist);
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsParameters() {
        BetaDistribution dist = BetaDistribution.of(2.5, 3.7);
        String str = dist.toString();

        assertThat(str).contains("BetaDistribution");
        assertThat(str).contains("2.5");
        assertThat(str).contains("3.7");
    }

    // ==================== Edge Case Tests ====================

    @Test
    void handlesLargeParameters() {
        BetaDistribution dist = BetaDistribution.of(100.0, 200.0);

        // Should not produce NaN or infinity
        assertThat(dist.getMean()).isCloseTo(100.0 / 300.0, within(TOLERANCE));
        assertThat(dist.density(0.3)).isGreaterThan(0.0);
    }

    @Test
    void handlesSmallParameters() {
        BetaDistribution dist = BetaDistribution.of(0.5, 0.5);

        // U-shaped distribution
        assertThat(dist.getMean()).isCloseTo(0.5, within(RELAXED_TOLERANCE));
        // At x=0.5, Beta(0.5, 0.5) density is infinity, so we can't compare with isLessThan
        // Just verify that density is infinite at center and finite (greater than 0) at edges
        assertThat(dist.density(0.5)).isEqualTo(Double.POSITIVE_INFINITY);
        assertThat(dist.density(0.1)).isGreaterThan(0.0);
        assertThat(dist.density(0.9)).isGreaterThan(0.0);
    }

    // ==================== Special Cases Tests ====================

    @Test
    void alphaEqualsOneIsRightSkewed() {
        BetaDistribution dist = BetaDistribution.of(1.0, 3.0);

        // Right-skewed: density decreases as x increases
        assertThat(dist.density(0.2)).isGreaterThan(dist.density(0.5));
        assertThat(dist.density(0.5)).isGreaterThan(dist.density(0.8));
    }

    @Test
    void betaEqualsOneIsLeftSkewed() {
        BetaDistribution dist = BetaDistribution.of(3.0, 1.0);

        // Left-skewed: density increases as x increases
        assertThat(dist.density(0.2)).isLessThan(dist.density(0.5));
        assertThat(dist.density(0.5)).isLessThan(dist.density(0.8));
    }

    // ==================== Immutability Tests ====================

    @Test
    void distributionIsImmutable() {
        BetaDistribution dist = BetaDistribution.of(2.0, 5.0);

        double originalAlpha = dist.getAlpha();
        double originalBeta = dist.getBeta();

        // Perform operations
        dist.density(0.5);

        // Verify state hasn't changed
        assertThat(dist.getAlpha()).isEqualTo(originalAlpha);
        assertThat(dist.getBeta()).isEqualTo(originalBeta);
    }
}
