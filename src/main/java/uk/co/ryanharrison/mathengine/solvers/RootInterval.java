package uk.co.ryanharrison.mathengine.solvers;

import java.util.Objects;

/**
 * Immutable value object representing an interval [lower, upper] that may contain a root.
 * <p>
 * A root interval is used in root-finding algorithms to represent a bracket where a
 * continuous function changes sign, indicating the presence of at least one root by the
 * Intermediate Value Theorem.
 * </p>
 *
 * <h2>Mathematical Definition:</h2>
 * For a continuous function f(x) and an interval [a, b]:
 * <ul>
 *     <li>If f(a) × f(b) < 0, then there exists at least one c ∈ (a, b) where f(c) = 0</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * RootInterval interval = RootInterval.of(0.0, 2.0);
 * double mid = interval.midpoint();  // 1.0
 * double width = interval.width();   // 2.0
 * boolean contains = interval.contains(1.5);  // true
 * }</pre>
 *
 * @see EquationSolver
 */
public final class RootInterval {

    private final double lower;
    private final double upper;

    /**
     * Private constructor. Use {@link #of(double, double)} to create instances.
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     */
    private RootInterval(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Creates a new root interval with the specified bounds.
     *
     * @param lower the lower bound of the interval
     * @param upper the upper bound of the interval
     * @return a new RootInterval instance
     * @throws IllegalArgumentException if lower >= upper
     * @throws IllegalArgumentException if either bound is NaN or infinite
     */
    public static RootInterval of(double lower, double upper) {
        if (Double.isNaN(lower) || Double.isInfinite(lower)) {
            throw new IllegalArgumentException("Lower bound must be finite, got: " + lower);
        }
        if (Double.isNaN(upper) || Double.isInfinite(upper)) {
            throw new IllegalArgumentException("Upper bound must be finite, got: " + upper);
        }
        if (lower >= upper) {
            throw new IllegalArgumentException(
                    String.format("Lower bound must be less than upper bound, got: [%.6g, %.6g]",
                            lower, upper));
        }
        return new RootInterval(lower, upper);
    }

    /**
     * Returns the lower bound of this interval.
     *
     * @return the lower bound
     */
    public double getLower() {
        return lower;
    }

    /**
     * Returns the upper bound of this interval.
     *
     * @return the upper bound
     */
    public double getUpper() {
        return upper;
    }

    /**
     * Returns the midpoint of this interval.
     * <p>
     * The midpoint is calculated as: (lower + upper) / 2
     * </p>
     *
     * @return the midpoint of the interval
     */
    public double midpoint() {
        return (lower + upper) * 0.5;
    }

    /**
     * Returns the width of this interval.
     * <p>
     * The width is calculated as: upper - lower
     * </p>
     *
     * @return the width of the interval (always positive)
     */
    public double width() {
        return upper - lower;
    }

    /**
     * Checks if the specified value is contained within this interval (inclusive).
     *
     * @param x the value to check
     * @return true if lower <= x <= upper, false otherwise
     */
    public boolean contains(double x) {
        return x >= lower && x <= upper;
    }

    /**
     * Checks if this interval overlaps with another interval.
     *
     * @param other the other interval to check
     * @return true if the intervals overlap, false otherwise
     */
    public boolean overlaps(RootInterval other) {
        return this.lower <= other.upper && other.lower <= this.upper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RootInterval that)) return false;
        return Double.compare(that.lower, lower) == 0 &&
                Double.compare(that.upper, upper) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper);
    }

    @Override
    public String toString() {
        return String.format("RootInterval[%.6g, %.6g]", lower, upper);
    }
}
