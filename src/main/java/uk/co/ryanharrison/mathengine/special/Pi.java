package uk.co.ryanharrison.mathengine.special;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable implementation for computing the mathematical constant π (pi) to arbitrary precision.
 * <p>
 * This class uses <b>Machin's formula</b> to compute pi efficiently:
 * <br>
 * π/4 = 4·arctan(1/5) - arctan(1/239)
 * </p>
 * <p>
 * The arctangent values are computed using a power series expansion:
 * <br>
 * arctan(x) = x - x³/3 + x⁵/5 - x⁷/7 + x⁹/9 - ...
 * </p>
 * <p>
 * This approach provides excellent convergence properties and has been used
 * historically for high-precision pi calculations.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Static method for direct computation
 * BigDecimal pi50 = Pi.compute(50);
 * System.out.println(pi50);  // 3.14159265358979323846264338327950288419716939937510...
 *
 * // Factory method with instance
 * Pi piCalculator = Pi.withDigits(100);
 * BigDecimal pi100 = piCalculator.execute();
 *
 * // Using builder pattern
 * Pi pi = Pi.builder()
 *     .digits(20)
 *     .build();
 * BigDecimal result = pi.execute();
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class Pi {
    /**
     * Constant value of 4 used in Machin's formula computation.
     * Pre-computed as a BigDecimal to avoid repeated conversions.
     */
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);

    /**
     * Rounding mode used during intermediate calculations in the pi computation.
     * HALF_EVEN provides banker's rounding, which minimizes cumulative rounding errors
     * in iterative calculations.
     */
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * Number of digits of precision after the decimal point.
     */
    private final int digits;

    /**
     * Private constructor for creating Pi instances.
     * Use factory methods {@link #withDigits(int)} or {@link #builder()} instead.
     *
     * @param digits The number of decimal digits of precision
     * @throws IllegalArgumentException if digits is not positive
     */
    private Pi(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException("Digits must be positive, got: " + digits);
        }
        this.digits = digits;
    }

    /**
     * Creates a Pi calculator configured to compute pi to the specified number of decimal digits.
     *
     * @param digits The number of decimal digits of precision (must be positive)
     * @return A new Pi instance
     * @throws IllegalArgumentException if digits is not positive
     */
    public static Pi withDigits(int digits) {
        return new Pi(digits);
    }

    /**
     * Creates a builder for constructing Pi instances with fluent API.
     *
     * @return A new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Computes the value of π to the specified number of decimal digits.
     * <p>
     * This is a convenience method that directly returns the computed value
     * without creating a Pi instance.
     * </p>
     *
     * @param digits The number of decimal digits of precision (must be positive)
     * @return The value of π computed to the specified precision
     * @throws IllegalArgumentException if digits is not positive
     */
    public static BigDecimal compute(int digits) {
        if (digits <= 0) {
            throw new IllegalArgumentException("Digits must be positive, got: " + digits);
        }
        return computePi(digits);
    }

    /**
     * Calculates the value of π to the number of decimal digits specified during construction.
     * <p>
     * This method can be called multiple times and will always return the same result
     * for the same Pi instance (immutability guarantee).
     * </p>
     *
     * @return The value of π computed to the configured precision
     */
    public BigDecimal execute() {
        return computePi(digits);
    }

    /**
     * Returns the configured number of digits of precision.
     *
     * @return The number of decimal digits after the decimal point
     */
    public int getDigits() {
        return digits;
    }

    /**
     * Computes the arctangent of the inverse of the supplied integer using power series expansion.
     * <p>
     * The value is computed using the power series expansion for arctangent:
     * <br>
     * arctan(1/x) = 1/x - 1/(3x³) + 1/(5x⁵) - 1/(7x⁷) + ...
     * </p>
     * <p>
     * The series continues until the terms become smaller than the precision we're working with.
     * </p>
     *
     * @param inverseX The value x for which to calculate arctan(1/x)
     * @param scale    The number of digits of precision to use in intermediate calculations
     * @return The arctangent of 1/inverseX computed to the specified precision
     */
    private static BigDecimal arctan(int inverseX, int scale) {
        BigDecimal result, numer, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2 = BigDecimal.valueOf((long) inverseX * inverseX);

        numer = BigDecimal.ONE.divide(invX, scale, ROUNDING_MODE);

        result = numer;
        int i = 1;
        do {
            numer = numer.divide(invX2, scale, ROUNDING_MODE);
            int denom = 2 * i + 1;
            term = numer.divide(BigDecimal.valueOf(denom), scale, ROUNDING_MODE);
            if (i % 2 != 0) {
                result = result.subtract(term);
            } else {
                result = result.add(term);
            }
            i++;
        }
        while (term.compareTo(BigDecimal.ZERO) != 0);
        return result;
    }

    /**
     * Internal method to compute the value of π to the specified number of digits.
     * <p>
     * The value is computed using Machin's formula:
     * <br>
     * π/4 = 4·arctan(1/5) - arctan(1/239)
     * </p>
     * <p>
     * The calculation uses extra precision (digits + 5) for intermediate calculations
     * to ensure the final result is accurate to the requested number of digits.
     * </p>
     *
     * @param digits The number of digits of precision after the decimal point
     * @return The value of π to the specified number of decimal digits
     */
    private static BigDecimal computePi(int digits) {
        int scale = digits + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);
        return pi.setScale(digits, RoundingMode.DOWN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pi that)) return false;
        return digits == that.digits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(digits);
    }

    @Override
    public String toString() {
        return String.format("Pi(digits=%d)", digits);
    }

    /**
     * Builder for constructing Pi instances with a fluent API.
     */
    public static final class Builder {
        private int digits = 10;  // Default to 10 digits

        /**
         * Sets the number of decimal digits of precision.
         *
         * @param digits The number of decimal digits (must be positive)
         * @return This builder instance for method chaining
         * @throws IllegalArgumentException if digits is not positive
         */
        public Builder digits(int digits) {
            if (digits <= 0) {
                throw new IllegalArgumentException("Digits must be positive, got: " + digits);
            }
            this.digits = digits;
            return this;
        }

        /**
         * Builds a Pi instance with the configured parameters.
         *
         * @return A new Pi instance
         */
        public Pi build() {
            return new Pi(digits);
        }
    }
}
