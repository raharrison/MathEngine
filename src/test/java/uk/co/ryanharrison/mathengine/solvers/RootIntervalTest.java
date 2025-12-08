package uk.co.ryanharrison.mathengine.solvers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link RootInterval}.
 */
class RootIntervalTest {

    private static final double TOLERANCE = 1e-9;

    // ==================== Construction Tests ====================

    @Test
    void ofCreatesIntervalWithGivenBounds() {
        RootInterval interval = RootInterval.of(0.0, 5.0);

        assertThat(interval.getLower()).isEqualTo(0.0);
        assertThat(interval.getUpper()).isEqualTo(5.0);
    }

    @Test
    void ofCreatesIntervalWithNegativeBounds() {
        RootInterval interval = RootInterval.of(-10.0, -2.0);

        assertThat(interval.getLower()).isEqualTo(-10.0);
        assertThat(interval.getUpper()).isEqualTo(-2.0);
    }

    @Test
    void ofCreatesIntervalSpanningZero() {
        RootInterval interval = RootInterval.of(-5.0, 5.0);

        assertThat(interval.getLower()).isEqualTo(-5.0);
        assertThat(interval.getUpper()).isEqualTo(5.0);
    }

    @Test
    void ofRejectsEqualBounds() {
        assertThatThrownBy(() -> RootInterval.of(5.0, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    @Test
    void ofRejectsLowerGreaterThanUpper() {
        assertThatThrownBy(() -> RootInterval.of(10.0, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be less than upper bound");
    }

    // ==================== Validation Tests ====================

    @Test
    void ofRejectsNaNLowerBound() {
        assertThatThrownBy(() -> RootInterval.of(Double.NaN, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be finite");
    }

    @Test
    void ofRejectsNaNUpperBound() {
        assertThatThrownBy(() -> RootInterval.of(0.0, Double.NaN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Upper bound must be finite");
    }

    @Test
    void ofRejectsInfiniteLowerBound() {
        assertThatThrownBy(() -> RootInterval.of(Double.POSITIVE_INFINITY, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be finite");

        assertThatThrownBy(() -> RootInterval.of(Double.NEGATIVE_INFINITY, 5.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lower bound must be finite");
    }

    @Test
    void ofRejectsInfiniteUpperBound() {
        assertThatThrownBy(() -> RootInterval.of(0.0, Double.POSITIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Upper bound must be finite");

        assertThatThrownBy(() -> RootInterval.of(0.0, Double.NEGATIVE_INFINITY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Upper bound must be finite");
    }

    // ==================== Property Tests ====================

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 1.0",      // Simple positive interval
            "-4.0, 4.0, 0.0",     // Symmetric around zero
            "-10.0, -5.0, -7.5",  // Negative interval
            "1.5, 2.5, 2.0",      // Fractional values
            "0.0, 0.001, 0.0005"  // Very small interval
    })
    void midpointIsCorrect(double lower, double upper, double expectedMidpoint) {
        RootInterval interval = RootInterval.of(lower, upper);
        assertThat(interval.midpoint()).isCloseTo(expectedMidpoint, within(TOLERANCE));
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 2.0, 2.0",      // Simple width
            "-4.0, 4.0, 8.0",     // Symmetric interval
            "-10.0, -5.0, 5.0",   // Negative bounds
            "1.5, 2.5, 1.0",      // Fractional values
            "0.0, 0.001, 0.001"   // Very small width
    })
    void widthIsCorrect(double lower, double upper, double expectedWidth) {
        RootInterval interval = RootInterval.of(lower, upper);
        assertThat(interval.width()).isCloseTo(expectedWidth, within(TOLERANCE));
    }

    @Test
    void widthIsAlwaysPositive() {
        RootInterval interval1 = RootInterval.of(-10.0, 5.0);
        RootInterval interval2 = RootInterval.of(0.0, 100.0);
        RootInterval interval3 = RootInterval.of(-50.0, -10.0);

        assertThat(interval1.width()).isGreaterThan(0.0);
        assertThat(interval2.width()).isGreaterThan(0.0);
        assertThat(interval3.width()).isGreaterThan(0.0);
    }

    // ==================== Contains Method Tests ====================

    @Test
    void containsReturnsTrueForValueInInterval() {
        RootInterval interval = RootInterval.of(0.0, 10.0);

        assertThat(interval.contains(5.0)).isTrue();
        assertThat(interval.contains(2.5)).isTrue();
        assertThat(interval.contains(7.8)).isTrue();
    }

    @Test
    void containsReturnsTrueForBoundaryValues() {
        RootInterval interval = RootInterval.of(0.0, 10.0);

        assertThat(interval.contains(0.0)).isTrue();   // Lower bound
        assertThat(interval.contains(10.0)).isTrue();  // Upper bound
    }

    @Test
    void containsReturnsFalseForValueOutsideInterval() {
        RootInterval interval = RootInterval.of(0.0, 10.0);

        assertThat(interval.contains(-1.0)).isFalse();
        assertThat(interval.contains(11.0)).isFalse();
        assertThat(interval.contains(-100.0)).isFalse();
        assertThat(interval.contains(100.0)).isFalse();
    }

    @Test
    void containsWorksWithNegativeInterval() {
        RootInterval interval = RootInterval.of(-10.0, -5.0);

        assertThat(interval.contains(-7.5)).isTrue();
        assertThat(interval.contains(-10.0)).isTrue();
        assertThat(interval.contains(-5.0)).isTrue();
        assertThat(interval.contains(-4.0)).isFalse();
        assertThat(interval.contains(-11.0)).isFalse();
    }

    // ==================== Overlaps Method Tests ====================

    @Test
    void overlapsReturnsTrueForOverlappingIntervals() {
        RootInterval interval1 = RootInterval.of(0.0, 10.0);
        RootInterval interval2 = RootInterval.of(5.0, 15.0);

        assertThat(interval1.overlaps(interval2)).isTrue();
        assertThat(interval2.overlaps(interval1)).isTrue();
    }

    @Test
    void overlapsReturnsTrueForNestedIntervals() {
        RootInterval outer = RootInterval.of(0.0, 10.0);
        RootInterval inner = RootInterval.of(3.0, 7.0);

        assertThat(outer.overlaps(inner)).isTrue();
        assertThat(inner.overlaps(outer)).isTrue();
    }

    @Test
    void overlapsReturnsTrueForIdenticalIntervals() {
        RootInterval interval1 = RootInterval.of(0.0, 10.0);
        RootInterval interval2 = RootInterval.of(0.0, 10.0);

        assertThat(interval1.overlaps(interval2)).isTrue();
        assertThat(interval2.overlaps(interval1)).isTrue();
    }

    @Test
    void overlapsReturnsTrueForAdjacentIntervals() {
        RootInterval interval1 = RootInterval.of(0.0, 5.0);
        RootInterval interval2 = RootInterval.of(5.0, 10.0);

        // Adjacent intervals with shared boundary should overlap
        assertThat(interval1.overlaps(interval2)).isTrue();
        assertThat(interval2.overlaps(interval1)).isTrue();
    }

    @Test
    void overlapsReturnsFalseForNonOverlappingIntervals() {
        RootInterval interval1 = RootInterval.of(0.0, 5.0);
        RootInterval interval2 = RootInterval.of(10.0, 15.0);

        assertThat(interval1.overlaps(interval2)).isFalse();
        assertThat(interval2.overlaps(interval1)).isFalse();
    }

    @Test
    void overlapsWorksWithNegativeIntervals() {
        RootInterval interval1 = RootInterval.of(-10.0, -5.0);
        RootInterval interval2 = RootInterval.of(-7.0, -2.0);
        RootInterval interval3 = RootInterval.of(-20.0, -15.0);

        assertThat(interval1.overlaps(interval2)).isTrue();
        assertThat(interval1.overlaps(interval3)).isFalse();
    }

    // ==================== Equality Tests ====================

    @Test
    void equalIntervalsAreEqual() {
        RootInterval interval1 = RootInterval.of(0.0, 10.0);
        RootInterval interval2 = RootInterval.of(0.0, 10.0);

        assertThat(interval1).isEqualTo(interval2);
        assertThat(interval1.hashCode()).isEqualTo(interval2.hashCode());
    }

    @Test
    void differentIntervalsAreNotEqual() {
        RootInterval interval1 = RootInterval.of(0.0, 10.0);
        RootInterval interval2 = RootInterval.of(0.0, 5.0);
        RootInterval interval3 = RootInterval.of(5.0, 10.0);

        assertThat(interval1).isNotEqualTo(interval2);
        assertThat(interval1).isNotEqualTo(interval3);
        assertThat(interval2).isNotEqualTo(interval3);
    }

    @Test
    void equalsHandlesSameInstance() {
        RootInterval interval = RootInterval.of(0.0, 10.0);

        assertThat(interval).isEqualTo(interval);
    }

    @Test
    void equalsHandlesNullAndDifferentTypes() {
        RootInterval interval = RootInterval.of(0.0, 10.0);

        assertThat(interval).isNotEqualTo(null);
        assertThat(interval).isNotEqualTo("not an interval");
        assertThat(interval).isNotEqualTo(42);
    }

    @Test
    void hashCodeIsConsistent() {
        RootInterval interval1 = RootInterval.of(5.0, 15.0);
        RootInterval interval2 = RootInterval.of(5.0, 15.0);
        RootInterval interval3 = RootInterval.of(5.0, 20.0);

        // Equal objects must have equal hash codes
        assertThat(interval1.hashCode()).isEqualTo(interval2.hashCode());

        // Unequal objects should (ideally) have different hash codes
        // Note: This is not strictly required but desirable
        assertThat(interval1.hashCode()).isNotEqualTo(interval3.hashCode());
    }

    // ==================== toString Tests ====================

    @Test
    void toStringContainsBounds() {
        RootInterval interval = RootInterval.of(1.5, 8.75);
        String str = interval.toString();

        assertThat(str).contains("RootInterval");
        assertThat(str).contains("1.5");
        assertThat(str).contains("8.75");
    }

    @Test
    void toStringHandlesNegativeValues() {
        RootInterval interval = RootInterval.of(-10.5, -2.3);
        String str = interval.toString();

        assertThat(str).contains("RootInterval");
        assertThat(str).contains("-10.5");
        assertThat(str).contains("-2.3");
    }

    // ==================== Edge Cases ====================

    @Test
    void handlesVerySmallInterval() {
        RootInterval interval = RootInterval.of(0.0, 1e-10);

        assertThat(interval.getLower()).isEqualTo(0.0);
        assertThat(interval.getUpper()).isEqualTo(1e-10);
        assertThat(interval.width()).isCloseTo(1e-10, within(1e-15));
        assertThat(interval.midpoint()).isCloseTo(5e-11, within(1e-15));
    }

    @Test
    void handlesVeryLargeInterval() {
        RootInterval interval = RootInterval.of(-1e10, 1e10);

        assertThat(interval.getLower()).isEqualTo(-1e10);
        assertThat(interval.getUpper()).isEqualTo(1e10);
        assertThat(interval.width()).isCloseTo(2e10, within(1.0));
        assertThat(interval.midpoint()).isCloseTo(0.0, within(1.0));
    }

    @Test
    void handlesNearEqualBounds() {
        double lower = 1.0;
        double upper = 1.0 + 1e-9;
        RootInterval interval = RootInterval.of(lower, upper);

        assertThat(interval.width()).isCloseTo(1e-9, within(1e-14));
        assertThat(interval.contains(lower)).isTrue();
        assertThat(interval.contains(upper)).isTrue();
    }

    @Test
    void handlesVeryLargeValues() {
        RootInterval interval = RootInterval.of(1e100, 2e100);

        assertThat(interval.getLower()).isEqualTo(1e100);
        assertThat(interval.getUpper()).isEqualTo(2e100);
        assertThat(interval.width()).isEqualTo(1e100);
        assertThat(interval.contains(1.5e100)).isTrue();
    }

    @Test
    void handlesVerySmallNegativeValues() {
        RootInterval interval = RootInterval.of(-1e-10, -1e-11);

        assertThat(interval.getLower()).isEqualTo(-1e-10);
        assertThat(interval.getUpper()).isEqualTo(-1e-11);
        assertThat(interval.width()).isCloseTo(9e-11, within(1e-15));
    }

    @Test
    void midpointDoesNotOverflow() {
        // Test that midpoint calculation doesn't overflow with large values
        double lower = 1e100;
        double upper = 2e100;
        RootInterval interval = RootInterval.of(lower, upper);

        double midpoint = interval.midpoint();
        assertThat(midpoint).isFinite();
        assertThat(midpoint).isGreaterThan(lower);
        assertThat(midpoint).isLessThan(upper);
    }

    // ==================== Immutability Tests ====================

    @Test
    void intervalIsImmutable() {
        RootInterval interval = RootInterval.of(5.0, 15.0);

        double originalLower = interval.getLower();
        double originalUpper = interval.getUpper();

        // Perform operations that might attempt to modify state
        interval.midpoint();
        interval.width();
        interval.contains(10.0);
        interval.overlaps(RootInterval.of(0.0, 20.0));

        // Verify state hasn't changed
        assertThat(interval.getLower()).isEqualTo(originalLower);
        assertThat(interval.getUpper()).isEqualTo(originalUpper);
    }
}
