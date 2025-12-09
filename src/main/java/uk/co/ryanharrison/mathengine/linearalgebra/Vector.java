package uk.co.ryanharrison.mathengine.linearalgebra;

import uk.co.ryanharrison.mathengine.utils.StatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Immutable implementation of a mathematical vector with arbitrary dimensions.
 * <p>
 * A vector is a one-dimensional array of real numbers that can represent points, directions,
 * or quantities in n-dimensional space. This implementation provides comprehensive support
 * for vector arithmetic, element-wise operations, and common mathematical operations.
 * </p>
 *
 * <h2>Mathematical Operations:</h2>
 * <ul>
 *     <li><b>Arithmetic</b>: addition, subtraction, multiplication, division (element-wise and scalar)</li>
 *     <li><b>Vector products</b>: dot product, cross product (3D only)</li>
 *     <li><b>Norms</b>: Euclidean norm (L2), squared norm</li>
 *     <li><b>Transformations</b>: normalization to unit vector, element-wise functions (exp, log, sqrt, pow)</li>
 *     <li><b>Statistics</b>: sum, mean, min, max</li>
 * </ul>
 *
 * <h2>Immutability:</h2>
 * <p>
 * All operations return new Vector instances. The underlying values array is never modified
 * after construction. This ensures thread-safety and prevents unexpected side effects.
 * </p>
 *
 * <h2>Size Normalization:</h2>
 * <p>
 * When performing binary operations on vectors of different sizes, the smaller vector is
 * conceptually extended with zeros to match the larger vector's size. For example:
 * <br>
 * {1, 2, 3} + {4, 5} = {1, 2, 3} + {4, 5, 0} = {5, 7, 3}
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Create vectors using factory methods
 * Vector v1 = Vector.of(1.0, 2.0, 3.0);
 * Vector v2 = Vector.of(4.0, 5.0, 6.0);
 *
 * // Vector arithmetic
 * Vector sum = v1.add(v2);              // {5, 7, 9}
 * Vector diff = v1.subtract(v2);         // {-3, -3, -3}
 * Vector scaled = v1.multiply(2.0);      // {2, 4, 6}
 *
 * // Dot product
 * double dot = v1.dotProduct(v2);        // 32.0
 *
 * // Cross product (3D only)
 * Vector cross = v1.crossProduct(v2);    // {-3, 6, -3}
 *
 * // Normalization
 * Vector unit = v1.getUnitVector();      // {0.267, 0.535, 0.802}
 * double norm = v1.getNorm();            // 3.742
 *
 * // Element-wise operations
 * Vector squared = v1.pow(2.0);          // {1, 4, 9}
 * Vector exponential = v1.exp();         // {2.718, 7.389, 20.086}
 *
 * // Statistical operations
 * double mean = v1.mean();               // 2.0
 * double max = v1.max();                 // 3.0
 *
 * // Special constructors
 * Vector zeros = Vector.zeros(5);        // {0, 0, 0, 0, 0}
 * Vector ones = Vector.ones(3);          // {1, 1, 1}
 * Vector filled = Vector.filled(4, 7.5); // {7.5, 7.5, 7.5, 7.5}
 * Vector parsed = Vector.parse("{1,2,3}"); // {1, 2, 3}
 * }</pre>
 *
 * @author Ryan Harrison
 */
public final class Vector {
    /**
     * The values that this vector holds. Immutable after construction.
     */
    private final double[] values;

    /**
     * The size of this vector. Cached for performance.
     */
    private final int size;

    /**
     * Package-private constructor for internal use.
     * Creates a new Vector with the specified values.
     * <p>
     * The array is defensively copied to ensure immutability.
     * </p>
     *
     * @param values the values of the vector (will be copied)
     * @throws IllegalArgumentException if values is null or empty
     */
    Vector(double[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Vector values cannot be null or empty");
        }
        this.values = Arrays.copyOf(values, values.length);
        this.size = values.length;
    }

    /**
     * Creates a new Vector with the specified values.
     * <p>
     * This is the primary factory method for creating vectors from known values.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(1.0, 2.0, 3.0);
     * Vector v2 = Vector.of(4.0, 5.0);
     * }</pre>
     *
     * @param values the values of the vector (varargs)
     * @return a new immutable Vector containing the specified values
     * @throws IllegalArgumentException if no values are provided
     */
    public static Vector of(double... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Vector must have at least one value");
        }
        return new Vector(values);
    }

    /**
     * Creates a new Vector with the specified size, filled with zeros.
     * <p>
     * This is useful for initializing vectors that will be populated through operations
     * or as a base for numerical algorithms.
     * </p>
     *
     * <pre>{@code
     * Vector zeros = Vector.zeros(5);  // {0, 0, 0, 0, 0}
     * }</pre>
     *
     * @param size the size of the vector
     * @return a new Vector with all elements set to 0.0
     * @throws IllegalArgumentException if size is less than 1
     */
    public static Vector zeros(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Vector size must be at least 1, got: " + size);
        }
        return new Vector(new double[size]);
    }

    /**
     * Creates a new Vector with the specified size, filled with ones.
     * <p>
     * This is useful for creating uniform vectors or as a base for scaling operations.
     * </p>
     *
     * <pre>{@code
     * Vector ones = Vector.ones(3);  // {1, 1, 1}
     * }</pre>
     *
     * @param size the size of the vector
     * @return a new Vector with all elements set to 1.0
     * @throws IllegalArgumentException if size is less than 1
     */
    public static Vector ones(int size) {
        return filled(size, 1.0);
    }

    /**
     * Creates a new Vector with the specified size, filled with a specific value.
     * <p>
     * This is useful for creating constant vectors or initializing vectors with
     * a specific starting value.
     * </p>
     *
     * <pre>{@code
     * Vector filled = Vector.filled(4, 7.5);  // {7.5, 7.5, 7.5, 7.5}
     * Vector negatives = Vector.filled(3, -1.0);  // {-1, -1, -1}
     * }</pre>
     *
     * @param size  the size of the vector
     * @param value the value to fill the vector with
     * @return a new Vector with all elements set to the specified value
     * @throws IllegalArgumentException if size is less than 1
     */
    public static Vector filled(int size, double value) {
        if (size < 1) {
            throw new IllegalArgumentException("Vector size must be at least 1, got: " + size);
        }
        double[] array = new double[size];
        Arrays.fill(array, value);
        return new Vector(array);
    }

    /**
     * Parses a Vector from a string representation.
     * <p>
     * The string should be in the format "{value1, value2, value3, ...}" where values
     * are comma-separated numbers. Whitespace is ignored. The braces are optional.
     * </p>
     * <p>
     * Supports scientific notation (e.g., "1.5e-3") and negative numbers.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.parse("{1, 2, 3}");       // {1, 2, 3}
     * Vector v2 = Vector.parse("4.5, -2.1, 3e-2"); // {4.5, -2.1, 0.03}
     * Vector v3 = Vector.parse("{1}");             // {1}
     * }</pre>
     *
     * @param vector the string representation of the vector
     * @return a new Vector parsed from the string
     * @throws IllegalArgumentException if the string is null, empty, or contains no valid numbers
     * @throws NumberFormatException    if the string contains invalid number formats
     */
    public static Vector parse(String vector) {
        if (vector == null || vector.trim().isEmpty()) {
            throw new IllegalArgumentException("Vector string cannot be null or empty");
        }

        char[] chars = vector.toLowerCase().toCharArray();
        List<Double> nums = new ArrayList<>();
        StringBuilder number = new StringBuilder();

        for (char c : chars) {
            if (isNumericCharacter(c)) {
                number.append(c);
            } else {
                if (!number.isEmpty()) {
                    nums.add(Double.parseDouble(number.toString()));
                    number.setLength(0); // Clear the StringBuilder
                }
            }
        }

        if (!number.isEmpty()) {
            nums.add(Double.parseDouble(number.toString()));
        }

        if (nums.isEmpty()) {
            throw new IllegalArgumentException("Vector string contains no valid numbers: " + vector);
        }

        double[] values = new double[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            values[i] = nums.get(i);
        }

        return new Vector(values);
    }

    /**
     * Determines if a character can be considered numeric during parsing.
     * <p>
     * This includes digits, decimal points, minus signs (for negatives), and 'e' (for scientific notation).
     * </p>
     *
     * @param c the character to test
     * @return true if the character is part of a numeric representation, false otherwise
     */
    private static boolean isNumericCharacter(char c) {
        return Character.isDigit(c) || c == '.' || c == '-' || c == 'e';
    }

    /**
     * Adds a scalar value to each element of this vector.
     * <p>
     * Returns a new vector where each element is increased by the specified value.
     * The original vector remains unchanged.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.0, 3.0);
     * Vector result = v.add(5.0);  // {6, 7, 8}
     * }</pre>
     *
     * @param d the scalar value to add to each element
     * @return a new Vector with the scalar added to each element
     */
    public Vector add(double d) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = values[i] + d;
        }
        return new Vector(result);
    }

    /**
     * Adds another vector to this vector element-wise.
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size before addition.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(1.0, 2.0, 3.0);
     * Vector v2 = Vector.of(4.0, 5.0);
     * Vector sum = v1.add(v2);  // {5, 7, 3} (v2 treated as {4, 5, 0})
     * }</pre>
     *
     * @param vector the vector to add to this vector
     * @return a new Vector representing the element-wise sum
     * @throws IllegalArgumentException if vector is null
     */
    public Vector add(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot add null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double[] result = new double[v1.size];
        for (int i = 0; i < v1.size; i++) {
            result[i] = v1.values[i] + v2.values[i];
        }
        return new Vector(result);
    }

    /**
     * Subtracts a scalar value from each element of this vector.
     * <p>
     * Returns a new vector where each element is decreased by the specified value.
     * The original vector remains unchanged.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(5.0, 7.0, 9.0);
     * Vector result = v.subtract(2.0);  // {3, 5, 7}
     * }</pre>
     *
     * @param d the scalar value to subtract from each element
     * @return a new Vector with the scalar subtracted from each element
     */
    public Vector subtract(double d) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = values[i] - d;
        }
        return new Vector(result);
    }

    /**
     * Subtracts another vector from this vector element-wise.
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size before subtraction.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(5.0, 7.0, 9.0);
     * Vector v2 = Vector.of(1.0, 2.0);
     * Vector diff = v1.subtract(v2);  // {4, 5, 9} (v2 treated as {1, 2, 0})
     * }</pre>
     *
     * @param vector the vector to subtract from this vector
     * @return a new Vector representing the element-wise difference
     * @throws IllegalArgumentException if vector is null
     */
    public Vector subtract(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot subtract null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double[] result = new double[v1.size];
        for (int i = 0; i < v1.size; i++) {
            result[i] = v1.values[i] - v2.values[i];
        }
        return new Vector(result);
    }

    /**
     * Multiplies each element of this vector by a scalar value.
     * <p>
     * Returns a new vector where each element is scaled by the specified value.
     * The original vector remains unchanged.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.0, 3.0);
     * Vector result = v.multiply(3.0);  // {3, 6, 9}
     * }</pre>
     *
     * @param d the scalar value to multiply each element by
     * @return a new Vector with each element multiplied by the scalar
     */
    public Vector multiply(double d) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = values[i] * d;
        }
        return new Vector(result);
    }

    /**
     * Multiplies another vector with this vector element-wise (Hadamard product).
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size before multiplication.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(2.0, 3.0, 4.0);
     * Vector v2 = Vector.of(5.0, 6.0);
     * Vector product = v1.multiply(v2);  // {10, 18, 0} (v2 treated as {5, 6, 0})
     * }</pre>
     *
     * @param vector the vector to multiply with this vector
     * @return a new Vector representing the element-wise product
     * @throws IllegalArgumentException if vector is null
     */
    public Vector multiply(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot multiply by null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double[] result = new double[v1.size];
        for (int i = 0; i < v1.size; i++) {
            result[i] = v1.values[i] * v2.values[i];
        }
        return new Vector(result);
    }

    /**
     * Divides each element of this vector by a scalar value.
     * <p>
     * Returns a new vector where each element is divided by the specified value.
     * The original vector remains unchanged.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(6.0, 9.0, 12.0);
     * Vector result = v.divide(3.0);  // {2, 3, 4}
     * }</pre>
     *
     * @param d the scalar value to divide each element by
     * @return a new Vector with each element divided by the scalar
     * @throws IllegalArgumentException if d is zero
     */
    public Vector divide(double d) {
        if (d == 0.0) {
            throw new IllegalArgumentException("Cannot divide vector by zero");
        }

        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = values[i] / d;
        }
        return new Vector(result);
    }

    /**
     * Divides this vector by another vector element-wise.
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size before division.
     * </p>
     * <p>
     * <b>Warning:</b> Division by zero in any element will result in {@code Infinity} or {@code NaN}.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(10.0, 20.0, 30.0);
     * Vector v2 = Vector.of(2.0, 4.0);
     * Vector quotient = v1.divide(v2);  // {5, 5, Infinity} (v2 treated as {2, 4, 0})
     * }</pre>
     *
     * @param vector the vector to divide this vector by
     * @return a new Vector representing the element-wise quotient
     * @throws IllegalArgumentException if vector is null
     */
    public Vector divide(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot divide by null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double[] result = new double[v1.size];
        for (int i = 0; i < v1.size; i++) {
            result[i] = v1.values[i] / v2.values[i];
        }
        return new Vector(result);
    }

    /**
     * Raises each element of this vector to the specified power.
     * <p>
     * Returns a new vector where each element is raised to the power of k.
     * The original vector remains unchanged.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(2.0, 3.0, 4.0);
     * Vector squared = v.pow(2.0);     // {4, 9, 16}
     * Vector cubed = v.pow(3.0);       // {8, 27, 64}
     * Vector sqrt = v.pow(0.5);        // {1.414, 1.732, 2.0}
     * }</pre>
     *
     * @param k the exponent to raise each element to
     * @return a new Vector with each element raised to the power of k
     */
    public Vector pow(double k) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = Math.pow(values[i], k);
        }
        return new Vector(result);
    }

    /**
     * Raises each element of this vector to the power of the corresponding element in another vector.
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(2.0, 3.0, 4.0);
     * Vector v2 = Vector.of(3.0, 2.0);
     * Vector result = v1.pow(v2);  // {8, 9, 1} (v2 treated as {3, 2, 0}, since 4^0 = 1)
     * }</pre>
     *
     * @param vector the vector containing the exponents
     * @return a new Vector with each element raised to the corresponding exponent
     * @throws IllegalArgumentException if vector is null
     */
    public Vector pow(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot raise to power of null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double[] result = new double[v1.size];
        for (int i = 0; i < v1.size; i++) {
            result[i] = Math.pow(v1.values[i], v2.values[i]);
        }
        return new Vector(result);
    }

    /**
     * Calculates the square root of each element in this vector.
     * <p>
     * Returns a new vector where each element is the square root of the corresponding
     * element in this vector. For negative values, the result will be {@code NaN}.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(4.0, 9.0, 16.0);
     * Vector result = v.sqrt();  // {2, 3, 4}
     * }</pre>
     *
     * @return a new Vector containing the square root of each element
     */
    public Vector sqrt() {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = Math.sqrt(values[i]);
        }
        return new Vector(result);
    }

    /**
     * Calculates the natural logarithm (base e) of each element in this vector.
     * <p>
     * Returns a new vector where each element is the natural logarithm of the corresponding
     * element in this vector. For non-positive values, the result will be {@code NaN} or {@code -Infinity}.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, Math.E, Math.E * Math.E);
     * Vector result = v.log();  // {0, 1, 2}
     * }</pre>
     *
     * @return a new Vector containing the natural logarithm of each element
     */
    public Vector log() {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = Math.log(values[i]);
        }
        return new Vector(result);
    }

    /**
     * Calculates the exponential (e^x) of each element in this vector.
     * <p>
     * Returns a new vector where each element is e raised to the power of the corresponding
     * element in this vector.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(0.0, 1.0, 2.0);
     * Vector result = v.exp();  // {1, 2.718, 7.389}
     * }</pre>
     *
     * @return a new Vector containing the exponential of each element
     */
    public Vector exp() {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = Math.exp(values[i]);
        }
        return new Vector(result);
    }

    /**
     * Calculates the dot product (inner product) of this vector with another vector.
     * <p>
     * The dot product is the sum of the products of corresponding elements:
     * <br>
     * v1 · v2 = Σ(v1[i] * v2[i])
     * </p>
     * <p>
     * If the vectors have different sizes, the smaller vector is conceptually padded
     * with zeros to match the larger vector's size.
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(1.0, 2.0, 3.0);
     * Vector v2 = Vector.of(4.0, 5.0, 6.0);
     * double dot = v1.dotProduct(v2);  // 32.0 (1*4 + 2*5 + 3*6)
     * }</pre>
     *
     * @param vector the other vector to compute the dot product with
     * @return the dot product as a scalar value
     * @throws IllegalArgumentException if vector is null
     */
    public double dotProduct(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot compute dot product with null vector");
        }

        Vector[] normalized = normalizeVectorSizes(this, vector);
        Vector v1 = normalized[0];
        Vector v2 = normalized[1];

        double result = 0.0;
        for (int i = 0; i < v1.size; i++) {
            result += v1.values[i] * v2.values[i];
        }
        return result;
    }

    /**
     * Calculates the cross product of this vector with another vector.
     * <p>
     * The cross product is only defined for 3-dimensional vectors. It produces a vector
     * that is perpendicular to both input vectors, with magnitude equal to the area of
     * the parallelogram formed by the two vectors.
     * </p>
     * <p>
     * For vectors v1 = (a, b, c) and v2 = (d, e, f):
     * <br>
     * v1 × v2 = (bf - ce, cd - af, ae - bd)
     * </p>
     *
     * <pre>{@code
     * Vector v1 = Vector.of(1.0, 2.0, 3.0);
     * Vector v2 = Vector.of(4.0, 5.0, 6.0);
     * Vector cross = v1.crossProduct(v2);  // {-3, 6, -3}
     * }</pre>
     *
     * @param vector the other vector to compute the cross product with
     * @return a new Vector representing the cross product
     * @throws IllegalArgumentException if either vector is not 3-dimensional, or if vector is null
     */
    public Vector crossProduct(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Cannot compute cross product with null vector");
        }
        if (this.size != 3) {
            throw new IllegalArgumentException(
                    "First vector must be 3-dimensional for cross product, got size: " + this.size);
        }
        if (vector.size != 3) {
            throw new IllegalArgumentException(
                    "Second vector must be 3-dimensional for cross product, got size: " + vector.size);
        }

        double[] result = new double[3];
        result[0] = values[1] * vector.values[2] - values[2] * vector.values[1];
        result[1] = values[2] * vector.values[0] - values[0] * vector.values[2];
        result[2] = values[0] * vector.values[1] - values[1] * vector.values[0];

        return new Vector(result);
    }

    /**
     * Calculates the Euclidean norm (L2 norm or magnitude) of this vector.
     * <p>
     * The norm is the length of the vector in Euclidean space:
     * <br>
     * ||v|| = √(Σ(v[i]²))
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(3.0, 4.0);
     * double norm = v.getNorm();  // 5.0 (√(3² + 4²))
     * }</pre>
     *
     * @return the Euclidean norm as a non-negative scalar value
     */
    public double getNorm() {
        return Math.sqrt(getNormSquare());
    }

    /**
     * Calculates the squared Euclidean norm of this vector.
     * <p>
     * This is more efficient than {@link #getNorm()} when only relative magnitudes
     * are needed, as it avoids the square root calculation:
     * <br>
     * ||v||² = Σ(v[i]²)
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(3.0, 4.0);
     * double normSquare = v.getNormSquare();  // 25.0 (3² + 4²)
     * }</pre>
     *
     * @return the squared Euclidean norm as a non-negative scalar value
     */
    public double getNormSquare() {
        double result = 0.0;
        for (int i = 0; i < size; i++) {
            result += values[i] * values[i];
        }
        return result;
    }

    /**
     * Returns the unit vector (normalized vector) in the same direction as this vector.
     * <p>
     * The unit vector has the same direction as the original vector but with a magnitude
     * (norm) of 1. It is calculated by dividing each element by the vector's norm:
     * <br>
     * û = v / ||v||
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(3.0, 4.0);
     * Vector unit = v.getUnitVector();  // {0.6, 0.8} (norm = 1.0)
     * }</pre>
     *
     * @return a new Vector representing the unit vector
     * @throws IllegalArgumentException if the norm of this vector is zero (cannot normalize a zero vector)
     */
    public Vector getUnitVector() {
        double norm = getNorm();
        if (norm == 0.0) {
            throw new IllegalArgumentException(
                    "Cannot normalize a vector with norm of zero");
        }
        return divide(norm);
    }

    /**
     * Calculates the sum of all elements in this vector.
     * <p>
     * Returns the scalar sum: Σ(v[i])
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.0, 3.0, 4.0);
     * double sum = v.sum();  // 10.0
     * }</pre>
     *
     * @return the sum of all elements
     */
    public double sum() {
        return StatUtils.sum(values);
    }

    /**
     * Calculates the arithmetic mean (average) of all elements in this vector.
     * <p>
     * Returns the mean: (Σ(v[i])) / n
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.0, 3.0, 4.0);
     * double mean = v.mean();  // 2.5
     * }</pre>
     *
     * @return the arithmetic mean of all elements
     */
    public double mean() {
        return StatUtils.mean(values);
    }

    /**
     * Finds the minimum value in this vector.
     *
     * <pre>{@code
     * Vector v = Vector.of(3.0, 1.0, 4.0, 1.0, 5.0);
     * double min = v.min();  // 1.0
     * }</pre>
     *
     * @return the minimum value among all elements
     */
    public double min() {
        return StatUtils.min(values);
    }

    /**
     * Finds the maximum value in this vector.
     *
     * <pre>{@code
     * Vector v = Vector.of(3.0, 1.0, 4.0, 1.0, 5.0);
     * double max = v.max();  // 5.0
     * }</pre>
     *
     * @return the maximum value among all elements
     */
    public double max() {
        return StatUtils.max(values);
    }

    /**
     * Gets the value at the specified index.
     * <p>
     * Provides read-only access to individual elements. Index must be in the range [0, size).
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.0, 3.0);
     * double first = v.get(0);   // 1.0
     * double second = v.get(1);  // 2.0
     * }</pre>
     *
     * @param index the zero-based index of the element to retrieve
     * @return the value at the specified index
     * @throws IllegalArgumentException if index is negative or greater than or equal to size
     */
    public double get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException(
                    "Vector index out of range: " + index + " (size: " + size + ")");
        }
        return values[index];
    }

    /**
     * Returns a copy of the internal values array.
     * <p>
     * The returned array is a defensive copy, ensuring immutability. Modifying the
     * returned array will not affect this Vector instance.
     * </p>
     *
     * @return a copy of the values array
     */
    public double[] getElements() {
        return Arrays.copyOf(values, size);
    }

    /**
     * Returns a copy of the internal values array.
     * <p>
     * Alias for {@link #getElements()}. The returned array is a defensive copy,
     * ensuring immutability.
     * </p>
     *
     * @return a copy of the values array
     */
    public double[] toArray() {
        return getElements();
    }

    /**
     * Returns the size (dimension) of this vector.
     * <p>
     * The size represents the number of elements in the vector.
     * </p>
     *
     * @return the number of elements in this vector
     */
    public int getSize() {
        return size;
    }

    /**
     * Creates a defensive copy of this vector.
     * <p>
     * Returns a new Vector instance with a copy of the values array.
     * Since Vector is immutable, this is functionally equivalent to the original.
     * </p>
     *
     * @return a new Vector instance with copied values
     */
    @Override
    public Object clone() {
        return new Vector(values);
    }

    /**
     * Normalizes the sizes of two vectors by padding the smaller one with zeros.
     * <p>
     * This is an internal helper method used by binary operations. Returns a new array
     * containing two vectors of equal size. The original vectors are not modified.
     * </p>
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return an array containing two new vectors of equal size
     */
    private static Vector[] normalizeVectorSizes(Vector v1, Vector v2) {
        if (v1.size == v2.size) {
            return new Vector[]{v1, v2};
        }

        int maxSize = Math.max(v1.size, v2.size);

        double[] result1 = new double[maxSize];
        double[] result2 = new double[maxSize];

        System.arraycopy(v1.values, 0, result1, 0, v1.size);
        System.arraycopy(v2.values, 0, result2, 0, v2.size);

        // Arrays.fill already zeros out the array, so no need to explicitly set remaining values

        return new Vector[]{new Vector(result1), new Vector(result2)};
    }

    /**
     * Compares this vector to another object for equality.
     * <p>
     * Two vectors are considered equal if they have the same size and all corresponding
     * elements are exactly equal (using {@code ==} comparison for doubles).
     * </p>
     *
     * @param obj the object to compare with
     * @return true if the object is a Vector with identical values, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector vector = (Vector) obj;
        return Arrays.equals(values, vector.values);
    }

    /**
     * Returns a hash code for this vector.
     * <p>
     * The hash code is computed based on the values array using {@link Arrays#hashCode(double[])}.
     * This ensures that equal vectors have equal hash codes.
     * </p>
     *
     * @return the hash code value for this vector
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    /**
     * Returns a string representation of this vector.
     * <p>
     * The format is "{value1, value2, value3, ...}" where values are formatted
     * using standard double-to-string conversion.
     * </p>
     *
     * <pre>{@code
     * Vector v = Vector.of(1.0, 2.5, 3.0);
     * System.out.println(v);  // Prints: {1.0, 2.5, 3.0}
     * }</pre>
     *
     * @return a string representation of this vector
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }

        StringBuilder str = new StringBuilder("{");
        for (int i = 0; i < size - 1; i++) {
            str.append(values[i]).append(", ");
        }
        str.append(values[size - 1]).append("}");
        return str.toString();
    }
}
