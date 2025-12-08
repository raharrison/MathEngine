package uk.co.ryanharrison.mathengine.special;

/**
 * Utility class providing the mathematical Beta function.
 * <p>
 * The Beta function, also known as the Euler integral of the first kind, is a special
 * function defined by the integral:
 * <br>
 * B(z, w) = ∫₀¹ t^(z-1) * (1-t)^(w-1) dt
 * </p>
 * <p>
 * It can be expressed in terms of the Gamma function as:
 * <br>
 * <b>B(z, w) = Γ(z) * Γ(w) / Γ(z + w)</b>
 * </p>
 * <p>
 * The Beta function has the following key properties:
 * </p>
 * <ul>
 *     <li><b>Symmetry</b>: B(z, w) = B(w, z)</li>
 *     <li><b>Domain</b>: Both z and w must be positive real numbers</li>
 *     <li><b>Special values</b>:
 *         <ul>
 *             <li>B(1, 1) = 1</li>
 *             <li>B(z, 1) = 1/z</li>
 *             <li>B(0.5, 0.5) = π</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Calculate B(2, 3)
 * double result = Beta.beta(2.0, 3.0);
 * // result ≈ 0.0833 (which equals 1/12)
 *
 * // Calculate B(0.5, 0.5)
 * double piValue = Beta.beta(0.5, 0.5);
 * // piValue ≈ 3.14159... (equals π)
 *
 * // Verify symmetry property
 * double b1 = Beta.beta(3.0, 5.0);
 * double b2 = Beta.beta(5.0, 3.0);
 * // b1 == b2
 * }</pre>
 *
 * <p>
 * This implementation computes the Beta function using the logarithmic form to avoid
 * numerical overflow for large parameter values:
 * <br>
 * B(z, w) = exp(ln Γ(z) + ln Γ(w) - ln Γ(z + w))
 * </p>
 *
 * @author Ryan Harrison
 * @see Gamma
 */
public final class Beta {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Beta() {
    }

    /**
     * Calculates the Beta function B(z, w).
     * <p>
     * The Beta function is computed using the relationship with the Gamma function:
     * <br>
     * B(z, w) = Γ(z) * Γ(w) / Γ(z + w)
     * </p>
     * <p>
     * The implementation uses logarithmic computation to prevent numerical overflow:
     * <br>
     * B(z, w) = exp(ln Γ(z) + ln Γ(w) - ln Γ(z + w))
     * </p>
     *
     * @param z the first parameter of the Beta function, must be positive
     * @param w the second parameter of the Beta function, must be positive
     * @return the value of B(z, w)
     * @throws IllegalArgumentException if {@code z <= 0} or {@code w <= 0}
     */
    public static double beta(final double z, final double w) {
        if (z <= 0.0) {
            throw new IllegalArgumentException(
                    "First parameter must be positive, got: " + z);
        }
        if (w <= 0.0) {
            throw new IllegalArgumentException(
                    "Second parameter must be positive, got: " + w);
        }

        return Math.exp(Gamma.gammaLn(z) + Gamma.gammaLn(w) - Gamma.gammaLn(z + w));
    }

}
