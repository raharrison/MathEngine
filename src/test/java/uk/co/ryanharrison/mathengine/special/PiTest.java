package uk.co.ryanharrison.mathengine.special;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Pi} class.
 * <p>
 * Tests the computation of π to arbitrary precision using Machin's formula,
 * verifying correctness against known digits of pi, immutability guarantees,
 * and proper error handling.
 * </p>
 */
class PiTest {

    /**
     * Known value of pi to 100 decimal places for verification.
     * Source: https://www.piday.org/million/
     */
    private static final String PI_100_DIGITS = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";

    /**
     * Tolerance for floating-point comparisons in tests that convert to double.
     */
    private static final double TOLERANCE = 1e-15;

    // ==================== Construction Tests ====================

    /**
     * Tests that the factory method withDigits creates a Pi instance with correct configuration.
     */
    @Test
    void withDigitsCreatesInstanceWithCorrectDigits() {
        Pi pi = Pi.withDigits(50);

        assertThat(pi.getDigits()).isEqualTo(50);
    }

    /**
     * Tests that the builder pattern creates a Pi instance with configured digits.
     */
    @Test
    void builderCreatesInstanceWithConfiguredDigits() {
        Pi pi = Pi.builder()
                .digits(75)
                .build();

        assertThat(pi.getDigits()).isEqualTo(75);
    }

    /**
     * Tests that the builder uses default digits when not explicitly set.
     */
    @Test
    void builderUsesDefaultDigits() {
        Pi pi = Pi.builder().build();

        assertThat(pi.getDigits()).isEqualTo(10);
    }

    // ==================== Validation Tests ====================

    /**
     * Tests that withDigits rejects zero as invalid input.
     */
    @Test
    void withDigitsRejectsZero() {
        assertThatThrownBy(() -> Pi.withDigits(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("0");
    }

    /**
     * Tests that withDigits rejects negative values as invalid input.
     */
    @Test
    void withDigitsRejectsNegativeDigits() {
        assertThatThrownBy(() -> Pi.withDigits(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("-10");
    }

    /**
     * Tests that static compute method rejects zero as invalid input.
     */
    @Test
    void computeRejectsZero() {
        assertThatThrownBy(() -> Pi.compute(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("0");
    }

    /**
     * Tests that static compute method rejects negative values as invalid input.
     */
    @Test
    void computeRejectsNegativeDigits() {
        assertThatThrownBy(() -> Pi.compute(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("-5");
    }

    /**
     * Tests that builder rejects zero digits.
     */
    @Test
    void builderRejectsZeroDigits() {
        assertThatThrownBy(() -> Pi.builder().digits(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("0");
    }

    /**
     * Tests that builder rejects negative digits.
     */
    @Test
    void builderRejectsNegativeDigits() {
        assertThatThrownBy(() -> Pi.builder().digits(-20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Digits must be positive")
                .hasMessageContaining("-20");
    }

    // ==================== Computation Correctness Tests ====================

    /**
     * Tests that pi is computed correctly to various precision levels using parameterized tests.
     * Verifies against known digits of pi.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 3.1",
            "2, 3.14",
            "5, 3.14159",
            "10, 3.1415926535",
            "15, 3.141592653589793",
            "20, 3.14159265358979323846",
            "30, 3.141592653589793238462643383279",
            "50, 3.14159265358979323846264338327950288419716939937510"
    })
    void computeReturnsCorrectPiForVariousPrecisions(int digits, String expectedPi) {
        BigDecimal result = Pi.compute(digits);

        assertThat(result.toString()).isEqualTo(expectedPi);
        assertThat(result.scale()).isEqualTo(digits);
    }

    /**
     * Tests that the execute method on Pi instance returns correct value.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 3.1",
            "2, 3.14",
            "5, 3.14159",
            "10, 3.1415926535",
            "20, 3.14159265358979323846"
    })
    void executeReturnsCorrectPiForVariousPrecisions(int digits, String expectedPi) {
        Pi pi = Pi.withDigits(digits);
        BigDecimal result = pi.execute();

        assertThat(result.toString()).isEqualTo(expectedPi);
        assertThat(result.scale()).isEqualTo(digits);
    }

    /**
     * Tests computation to 100 digits matches known value.
     */
    @Test
    void computeReturnsCorrectPiTo100Digits() {
        BigDecimal result = Pi.compute(100);

        assertThat(result.toString()).isEqualTo(PI_100_DIGITS);
        assertThat(result.scale()).isEqualTo(100);
    }

    /**
     * Tests that pi is approximately 3.14159 when converted to double.
     */
    @Test
    void computedPiIsApproximatelyCorrectAsDouble() {
        BigDecimal pi = Pi.compute(15);

        assertThat(pi.doubleValue()).isCloseTo(Math.PI, within(TOLERANCE));
    }

    /**
     * Tests that very high precision computation (500 digits) completes successfully.
     */
    @Test
    void computeHandlesVeryHighPrecision() {
        BigDecimal result = Pi.compute(500);

        assertThat(result.scale()).isEqualTo(500);
        // Verify it starts with known digits
        assertThat(result.toString()).startsWith("3.14159265358979323846264338327950288419716939937510");
    }

    // ==================== Static vs Instance Method Tests ====================

    /**
     * Tests that static compute method and instance execute method return identical results.
     */
    @ParameterizedTest
    @CsvSource({
            "5",
            "10",
            "25",
            "50",
            "100"
    })
    void staticComputeAndInstanceExecuteReturnSameResult(int digits) {
        BigDecimal staticResult = Pi.compute(digits);
        BigDecimal instanceResult = Pi.withDigits(digits).execute();

        assertThat(instanceResult).isEqualTo(staticResult);
    }

    /**
     * Tests that builder-constructed instance returns same result as static method.
     */
    @Test
    void builderInstanceReturnsCorrectResult() {
        int digits = 30;
        BigDecimal staticResult = Pi.compute(digits);

        Pi pi = Pi.builder()
                .digits(digits)
                .build();
        BigDecimal builderResult = pi.execute();

        assertThat(builderResult).isEqualTo(staticResult);
    }

    // ==================== Immutability Tests ====================

    /**
     * Tests that calling execute multiple times on the same instance returns identical results.
     * This verifies immutability - the instance state doesn't change.
     */
    @Test
    void executeIsIdempotentAndImmutable() {
        Pi pi = Pi.withDigits(25);

        BigDecimal result1 = pi.execute();
        BigDecimal result2 = pi.execute();
        BigDecimal result3 = pi.execute();

        assertThat(result1).isEqualTo(result2);
        assertThat(result2).isEqualTo(result3);
        assertThat(pi.getDigits()).isEqualTo(25);  // Configuration unchanged
    }

    /**
     * Tests that the instance configuration (digits) cannot be modified after construction.
     * This is enforced by not providing any setter methods.
     */
    @Test
    void instanceIsImmutableAfterConstruction() {
        Pi pi = Pi.withDigits(42);
        int originalDigits = pi.getDigits();

        // Perform multiple operations
        pi.execute();
        pi.execute();
        pi.toString();
        pi.hashCode();

        // Verify digits hasn't changed
        assertThat(pi.getDigits()).isEqualTo(originalDigits);
    }

    // ==================== Equals and HashCode Tests ====================

    /**
     * Tests that two Pi instances with same digits are equal.
     */
    @Test
    void equalsReturnsTrueForSameDigits() {
        Pi pi1 = Pi.withDigits(20);
        Pi pi2 = Pi.withDigits(20);

        assertThat(pi1).isEqualTo(pi2);
        assertThat(pi2).isEqualTo(pi1);
    }

    /**
     * Tests that two Pi instances with different digits are not equal.
     */
    @Test
    void equalsReturnsFalseForDifferentDigits() {
        Pi pi1 = Pi.withDigits(20);
        Pi pi2 = Pi.withDigits(30);

        assertThat(pi1).isNotEqualTo(pi2);
    }

    /**
     * Tests that equals returns true when comparing instance to itself.
     */
    @Test
    void equalsReturnsTrueForSameInstance() {
        Pi pi = Pi.withDigits(15);

        assertThat(pi).isEqualTo(pi);
    }

    /**
     * Tests that equals returns false when comparing to null.
     */
    @Test
    void equalsReturnsFalseForNull() {
        Pi pi = Pi.withDigits(15);

        assertThat(pi).isNotEqualTo(null);
    }

    /**
     * Tests that equals returns false when comparing to different type.
     */
    @Test
    void equalsReturnsFalseForDifferentType() {
        Pi pi = Pi.withDigits(15);

        assertThat(pi).isNotEqualTo("Pi(15)");
        assertThat(pi).isNotEqualTo(15);
    }

    /**
     * Tests that equal instances have equal hash codes.
     */
    @Test
    void equalInstancesHaveSameHashCode() {
        Pi pi1 = Pi.withDigits(25);
        Pi pi2 = Pi.withDigits(25);

        assertThat(pi1.hashCode()).isEqualTo(pi2.hashCode());
    }

    /**
     * Tests that different instances typically have different hash codes.
     * (Not guaranteed by contract, but should be true in practice)
     */
    @Test
    void differentInstancesTypicallyHaveDifferentHashCodes() {
        Pi pi1 = Pi.withDigits(10);
        Pi pi2 = Pi.withDigits(20);
        Pi pi3 = Pi.withDigits(30);

        // While hash collisions are possible, these should be different
        assertThat(pi1.hashCode()).isNotEqualTo(pi2.hashCode());
        assertThat(pi2.hashCode()).isNotEqualTo(pi3.hashCode());
    }

    // ==================== ToString Tests ====================

    /**
     * Tests that toString returns a descriptive string representation.
     */
    @Test
    void toStringReturnsDescriptiveRepresentation() {
        Pi pi = Pi.withDigits(50);

        String result = pi.toString();

        assertThat(result).contains("Pi");
        assertThat(result).contains("50");
        assertThat(result).isEqualTo("Pi(digits=50)");
    }

    /**
     * Tests toString for various configurations.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 'Pi(digits=1)'",
            "10, 'Pi(digits=10)'",
            "100, 'Pi(digits=100)'",
            "1000, 'Pi(digits=1000)'"
    })
    void toStringFormatsCorrectly(int digits, String expected) {
        Pi pi = Pi.withDigits(digits);

        assertThat(pi.toString()).isEqualTo(expected);
    }

    // ==================== Edge Cases and Properties ====================

    /**
     * Tests that computing pi with minimum valid digits (1) works correctly.
     */
    @Test
    void computeWorksWithMinimumDigits() {
        BigDecimal result = Pi.compute(1);

        assertThat(result.toString()).isEqualTo("3.1");
        assertThat(result.scale()).isEqualTo(1);
    }

    /**
     * Tests that the computed value is always greater than 3.
     */
    @ParameterizedTest
    @CsvSource({
            "1", "5", "10", "20", "50", "100"
    })
    void computedPiIsAlwaysGreaterThanThree(int digits) {
        BigDecimal result = Pi.compute(digits);

        assertThat(result).isGreaterThan(BigDecimal.valueOf(3));
    }

    /**
     * Tests that the computed value is always less than 4.
     */
    @ParameterizedTest
    @CsvSource({
            "1", "5", "10", "20", "50", "100"
    })
    void computedPiIsAlwaysLessThanFour(int digits) {
        BigDecimal result = Pi.compute(digits);

        assertThat(result).isLessThan(BigDecimal.valueOf(4));
    }

    /**
     * Tests that increasing precision gives values that agree on the common digits.
     */
    @Test
    void increasingPrecisionAgreesOnCommonDigits() {
        BigDecimal pi10 = Pi.compute(10);
        BigDecimal pi20 = Pi.compute(20);
        BigDecimal pi50 = Pi.compute(50);

        // The first 10 digits should be the same
        assertThat(pi20.toString()).startsWith(pi10.toString());
        assertThat(pi50.toString()).startsWith(pi10.toString());
        assertThat(pi50.toString()).startsWith(pi20.toString());
    }

    /**
     * Tests that the scale (decimal places) of the result matches the requested digits.
     */
    @ParameterizedTest
    @CsvSource({
            "1", "5", "10", "15", "20", "50", "100"
    })
    void resultScaleMatchesRequestedDigits(int digits) {
        BigDecimal result = Pi.compute(digits);

        assertThat(result.scale()).isEqualTo(digits);
    }

    /**
     * Tests that builder method chaining works correctly.
     */
    @Test
    void builderSupportsMethodChaining() {
        Pi pi = Pi.builder()
                .digits(35)
                .build();

        assertThat(pi.getDigits()).isEqualTo(35);
        BigDecimal result = pi.execute();
        assertThat(result.scale()).isEqualTo(35);
    }
}
