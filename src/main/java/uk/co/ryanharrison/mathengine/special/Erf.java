package uk.co.ryanharrison.mathengine.special;

/**
 * Utility class providing static methods for computing the Error function and its complement.
 * <p>
 * The <b>Error function</b> (erf) is a special function of sigmoid shape that occurs in probability,
 * statistics, and partial differential equations. It is defined as:
 * </p>
 * <pre>
 * erf(x) = (2/√π) ∫₀ˣ e^(-t²) dt
 * </pre>
 * <p>
 * The <b>Complementary error function</b> (erfc) is defined as:
 * </p>
 * <pre>
 * erfc(x) = 1 - erf(x) = (2/√π) ∫ₓ^∞ e^(-t²) dt
 * </pre>
 * <p>
 * <b>Properties:</b>
 * </p>
 * <ul>
 *     <li>erf(x) is an odd function: erf(-x) = -erf(x)</li>
 *     <li>erf(0) = 0</li>
 *     <li>erf(∞) = 1, erf(-∞) = -1</li>
 *     <li>Range of erf: [-1, 1]</li>
 *     <li>Range of erfc: [0, 2]</li>
 *     <li>erfc(x) + erf(x) = 1 for all x</li>
 * </ul>
 * <p>
 * <b>Implementation:</b> This implementation uses the Abramowitz and Stegun approximation
 * (formula 7.1.26) which provides accuracy to approximately 1.5 × 10⁻⁷ for all x.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Calculate error function at x = 1
 * double erfValue = Erf.erf(1.0);  // ≈ 0.8427
 *
 * // Calculate complementary error function
 * double erfcValue = Erf.erfc(1.0);  // ≈ 0.1573
 *
 * // Verify the relationship: erf(x) + erfc(x) = 1
 * double x = 2.5;
 * double sum = Erf.erf(x) + Erf.erfc(x);  // = 1.0
 *
 * // Use symmetry property: erf(-x) = -erf(x)
 * double symmetric = Erf.erf(-1.0);  // ≈ -0.8427
 * }</pre>
 *
 * @author Ryan Harrison
 * @see <a href="http://www.johndcook.com/csharp_erf.html">Original C# implementation by John D. Cook</a>
 * @see <a href="https://en.wikipedia.org/wiki/Error_function">Error function - Wikipedia</a>
 */
public final class Erf {

    /**
     * Coefficient a₁ in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double A1 = 0.254829592;

    /**
     * Coefficient a₂ in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double A2 = -0.284496736;

    /**
     * Coefficient a₃ in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double A3 = 1.421413741;

    /**
     * Coefficient a₄ in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double A4 = -1.453152027;

    /**
     * Coefficient a₅ in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double A5 = 1.061405429;

    /**
     * Coefficient p in the Abramowitz and Stegun approximation formula 7.1.26.
     */
    private static final double P = 0.3275911;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Erf() {
    }

    /**
     * Calculates the Error function at the given point.
     * <p>
     * The error function is defined as:
     * <pre>
     * erf(x) = (2/√π) ∫₀ˣ e^(-t²) dt
     * </pre>
     * This implementation uses the Abramowitz and Stegun approximation formula 7.1.26,
     * which provides accuracy to approximately 1.5 × 10⁻⁷.
     * </p>
     * <p>
     * The error function has the following properties:
     * </p>
     * <ul>
     *     <li>erf(0) = 0</li>
     *     <li>erf(-x) = -erf(x) (odd function)</li>
     *     <li>erf(∞) = 1</li>
     *     <li>Range: [-1, 1]</li>
     * </ul>
     *
     * @param x the point at which to evaluate the error function
     * @return the value of erf(x), in the range [-1, 1]
     * @see <a href="http://www.johndcook.com/csharp_erf.html">Implementation reference</a>
     */
    public static double erf(double x) {
        // Save the sign of x (erf is an odd function)
        int sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = Math.abs(x);

        // Abramowitz and Stegun formula 7.1.26
        double t = 1.0 / (1.0 + P * x);
        double y = 1.0 - (((((A5 * t + A4) * t) + A3) * t + A2) * t + A1) * t * Math.exp(-x * x);

        return sign * y;
    }

    /**
     * Calculates the complementary Error function at the given point.
     * <p>
     * The complementary error function is defined as:
     * <pre>
     * erfc(x) = 1 - erf(x) = (2/√π) ∫ₓ^∞ e^(-t²) dt
     * </pre>
     * This is useful for computing probabilities in the tail of the normal distribution
     * and provides better numerical stability for large positive values of x.
     * </p>
     * <p>
     * Properties:
     * </p>
     * <ul>
     *     <li>erfc(0) = 1</li>
     *     <li>erfc(∞) = 0</li>
     *     <li>erfc(x) + erf(x) = 1 for all x</li>
     *     <li>Range: [0, 2]</li>
     * </ul>
     *
     * @param x the point at which to evaluate the complementary error function
     * @return the value of erfc(x) = 1 - erf(x), in the range [0, 2]
     * @see #erf(double)
     */
    public static double erfc(double x) {
        return 1.0 - erf(x);
    }
}
