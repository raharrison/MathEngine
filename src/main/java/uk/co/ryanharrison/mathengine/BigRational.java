package uk.co.ryanharrison.mathengine;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Representation of a rational number without any overflow. This class is
 * immutable.
 */
public class BigRational extends Number implements Comparable<BigRational>, Cloneable, Serializable {
    /**
     * A fraction representing "2 / 1".
     */
    public static final BigRational TWO = new BigRational(2);

    /**
     * A fraction representing "1".
     */
    public static final BigRational ONE = new BigRational(1);

    /**
     * A fraction representing "0".
     */
    public static final BigRational ZERO = new BigRational(0);

    /**
     * A fraction representing "-1 / 1".
     */
    public static final BigRational MINUS_ONE = new BigRational(-1);

    /**
     * A fraction representing "4/5".
     */
    public static final BigRational FOUR_FIFTHS = new BigRational(4, 5);

    /**
     * A fraction representing "1/5".
     */
    public static final BigRational ONE_FIFTH = new BigRational(1, 5);

    /**
     * A fraction representing "1/2".
     */
    public static final BigRational ONE_HALF = new BigRational(1, 2);

    /**
     * A fraction representing "1/4".
     */
    public static final BigRational ONE_QUARTER = new BigRational(1, 4);

    /**
     * A fraction representing "1/3".
     */
    public static final BigRational ONE_THIRD = new BigRational(1, 3);

    /**
     * A fraction representing "3/5".
     */
    public static final BigRational THREE_FIFTHS = new BigRational(3, 5);

    /**
     * A fraction representing "3/4".
     */
    public static final BigRational THREE_QUARTERS = new BigRational(3, 4);

    /**
     * A fraction representing "2/5".
     */
    public static final BigRational TWO_FIFTHS = new BigRational(2, 5);

    /**
     * A fraction representing "2/4".
     */
    public static final BigRational TWO_QUARTERS = new BigRational(2, 4);

    /**
     * A fraction representing "2/3".
     */
    public static final BigRational TWO_THIRDS = new BigRational(2, 3);

    /**
     * Serializable version identifier.
     */
    private static final long serialVersionUID = -5630213147331578515L;

    /**
     * <code>BigInteger</code> representation of 100.
     */
    private static final BigInteger ONE_HUNDRED = BigInteger.valueOf(100);

    /**
     * The numerator.
     */
    private final BigInteger numerator;

    /**
     * The denominator.
     */
    private final BigInteger denominator;

    /**
     * <p>
     * Create a {@link BigRational} equivalent to the passed <tt>BigInteger</tt>
     * , ie "num / 1".
     * </p>
     *
     * @param num the numerator.
     */
    public BigRational(final BigInteger num) {
        this(num, BigInteger.ONE);
    }

    /**
     * Create a {@link BigRational} given the numerator and denominator as
     * {@code BigInteger}. The {@link BigRational} is reduced to lowest terms.
     *
     * @param num the numerator, must not be {@code null}.
     * @param den the denominator, must not be {@code null}.
     */
    public BigRational(BigInteger num, BigInteger den) {
        if (num == null || den == null)
            throw new IllegalArgumentException("Null argument");

        if (BigInteger.ZERO.equals(den)) {
            throw new IllegalArgumentException("Zero denominator");
        }
        if (BigInteger.ZERO.equals(num)) {
            numerator = BigInteger.ZERO;
            denominator = BigInteger.ONE;
        } else {

            // reduce numerator and denominator by greatest common denominator
            final BigInteger gcd = num.gcd(den);
            if (BigInteger.ONE.compareTo(gcd) < 0) {
                num = num.divide(gcd);
                den = den.divide(gcd);
            }

            // move sign to numerator
            if (BigInteger.ZERO.compareTo(den) > 0) {
                num = num.negate();
                den = den.negate();
            }

            // store the values in the final fields
            numerator = num;
            denominator = den;

        }
    }

    /**
     * Create a fraction given the double value.
     * <p>
     * This constructor behaves <em>differently</em> from
     * {@link #BigRational(double, double, int)}. It converts the double value
     * exactly, considering its internal bits representation. This works for all
     * values except NaN and infinities and does not requires any loop or
     * convergence threshold.
     * </p>
     * <p>
     * Since this conversion is exact and since double numbers are sometimes
     * approximated, the fraction created may seem strange in some cases. For
     * example, calling <code>new BigFraction(1.0 / 3.0)</code> does
     * <em>not</em> create the fraction 1/3, but the fraction 6004799503160661 /
     * 18014398509481984 because the double number passed to the constructor is
     * not exactly 1/3 (this number cannot be stored exactly in IEEE754).
     * </p>
     *
     * @param value the double value to convert to a fraction.
     * @see #BigRational(double, double, int)
     */
    public BigRational(final double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("Value is NaN");
        }
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException("Value is infinite");
        }

        // compute m and k such that value = m * 2^k
        final long bits = Double.doubleToLongBits(value);
        final long sign = bits & 0x8000000000000000L;
        final long exponent = bits & 0x7ff0000000000000L;
        long m = bits & 0x000fffffffffffffL;
        if (exponent != 0) {
            // this was a normalized number, add the implicit most significant
            // bit
            m |= 0x0010000000000000L;
        }
        if (sign != 0) {
            m = -m;
        }
        int k = ((int) (exponent >> 52)) - 1075;
        while (((m & 0x001ffffffffffffeL) != 0) && ((m & 0x1) == 0)) {
            m = m >> 1;
            ++k;
        }

        if (k < 0) {
            numerator = BigInteger.valueOf(m);
            denominator = BigInteger.ZERO.flipBit(-k);
        } else {
            numerator = BigInteger.valueOf(m).multiply(BigInteger.ZERO.flipBit(k));
            denominator = BigInteger.ONE;
        }

    }

    /**
     * Construct a new fraction given a string value representing the number
     *
     * @param str A string containing the number (e.g "3.1415")
     */
    public BigRational(String str) {
        this(Double.parseDouble(str));
    }

    /**
     * Create a fraction given the double value and maximum error allowed.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     *
     * @param value         the double value to convert to a fraction.
     * @param epsilon       maximum error allowed. The resulting fraction is within
     *                      <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxIterations maximum number of convergents.
     * @see #BigRational(double)
     */
    public BigRational(final double value, final double epsilon, final int maxIterations) {
        this(value, epsilon, Integer.MAX_VALUE, maxIterations);
    }

    /**
     * Create a fraction given the double value and either the maximum error
     * allowed or the maximum number of denominator digits.
     * <p>
     * <p>
     * NOTE: This constructor is called with EITHER - a valid epsilon value and
     * the maxDenominator set to Integer.MAX_VALUE (that way the maxDenominator
     * has no effect). OR - a valid maxDenominator value and the epsilon value
     * set to zero (that way epsilon only has effect if there is an exact match
     * before the maxDenominator value is reached).
     * </p>
     * <p>
     * <p>
     * It has been done this way so that the same code can be (re)used for both
     * scenarios. However this could be confusing to users if it were part of
     * the public API and this constructor should therefore remain PRIVATE.
     * </p>
     * <p>
     * See JIRA issue ticket MATH-181 for more details:
     * <p>
     * https://issues.apache.org/jira/browse/MATH-181
     *
     * @param value          the double value to convert to a fraction.
     * @param epsilon        maximum error allowed. The resulting fraction is within
     *                       <code>epsilon</code> of <code>value</code>, in absolute terms.
     * @param maxDenominator maximum denominator value allowed.
     * @param maxIterations  maximum number of convergents.
     */
    private BigRational(final double value, final double epsilon, final int maxDenominator,
                        int maxIterations) {
        long overflow = Integer.MAX_VALUE;
        double r0 = value;
        long a0 = (long) Math.floor(r0);
        if (a0 > overflow) {
            throw new RuntimeException("Cannot convert Rational");
        }

        // check for (almost) integer arguments, which should not go
        // to iterations.
        if (Math.abs(a0 - value) < epsilon) {
            numerator = BigInteger.valueOf(a0);
            denominator = BigInteger.ONE;
            return;
        }

        long p0 = 1;
        long q0 = 0;
        long p1 = a0;
        long q1 = 1;

        long p2 = 0;
        long q2 = 1;

        int n = 0;
        boolean stop = false;
        do {
            ++n;
            final double r1 = 1.0 / (r0 - a0);
            final long a1 = (long) Math.floor(r1);
            p2 = (a1 * p1) + p0;
            q2 = (a1 * q1) + q0;
            if ((p2 > overflow) || (q2 > overflow)) {
                throw new RuntimeException("Cannot convert Rational");
            }

            final double convergent = (double) p2 / (double) q2;
            if ((n < maxIterations) && (Math.abs(convergent - value) > epsilon)
                    && (q2 < maxDenominator)) {
                p0 = p1;
                p1 = p2;
                q0 = q1;
                q1 = q2;
                a0 = a1;
                r0 = r1;
            } else {
                stop = true;
            }
        }
        while (!stop);

        if (n >= maxIterations) {
            throw new RuntimeException("Cannot convert Rational");
        }

        if (q2 < maxDenominator) {
            numerator = BigInteger.valueOf(p2);
            denominator = BigInteger.valueOf(q2);
        } else {
            numerator = BigInteger.valueOf(p1);
            denominator = BigInteger.valueOf(q1);
        }
    }

    /**
     * Create a fraction given the double value and maximum denominator.
     * <p>
     * References:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
     * Continued Fraction</a> equations (11) and (22)-(26)</li>
     * </ul>
     * </p>
     *
     * @param value          the double value to convert to a fraction.
     * @param maxDenominator The maximum allowed value for denominator.
     */
    public BigRational(final double value, final int maxDenominator) {
        this(value, 0, maxDenominator, 100);
    }

    /**
     * <p>
     * Create a {@link BigRational} equivalent to the passed <tt>int</tt>, ie
     * "num / 1".
     * </p>
     *
     * @param num the numerator.
     */
    public BigRational(final int num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigRational} given the numerator and denominator as
     * simple <tt>int</tt>. The {@link BigRational} is reduced to lowest terms.
     * </p>
     *
     * @param num the numerator.
     * @param den the denominator.
     */
    public BigRational(final int num, final int den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Create a {@link BigRational} equivalent to the passed long, ie "num / 1".
     * </p>
     *
     * @param num the numerator.
     */
    public BigRational(final long num) {
        this(BigInteger.valueOf(num), BigInteger.ONE);
    }

    /**
     * <p>
     * Create a {@link BigRational} given the numerator and denominator as
     * simple <tt>long</tt>. The {@link BigRational} is reduced to lowest terms.
     * </p>
     *
     * @param num the numerator.
     * @param den the denominator.
     */
    public BigRational(final long num, final long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    /**
     * <p>
     * Returns the absolute value of this {@link BigRational}.
     * </p>
     *
     * @return the absolute value as a {@link BigRational}.
     */
    public BigRational abs() {
        return (BigInteger.ZERO.compareTo(numerator) <= 0) ? this : negate();
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed {@link BigInteger},
     * returning the result in reduced form.
     * </p>
     *
     * @param bg the {@link BigInteger} to add, must'nt be <code>null</code>.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigRational add(final BigInteger bg) {
        if (bg == null)
            throw new IllegalArgumentException("Fraction is null");

        return new BigRational(numerator.add(denominator.multiply(bg)), denominator);
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>integer</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param i the <tt>integer</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigRational add(final int i) {
        return add(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Adds the value of this fraction to the passed <tt>long</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param l the <tt>long</tt> to add.
     * @return a <code>BigFraction</code> instance with the resulting values.
     */
    public BigRational add(final long l) {
        return add(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Adds the value of this fraction to another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction the {@link BigRational} to add, must not be <code>null</code>.
     * @return a {@link BigRational} instance with the resulting values.
     */
    public BigRational add(final BigRational fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;

        if (denominator.equals(fraction.denominator)) {
            num = numerator.add(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).add((fraction.numerator)
                    .multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigRational(num, den);

    }

    @Override
    public Object clone() {
        return new BigRational(this.numerator, this.denominator);
    }

    /**
     * <p>
     * Compares this object to another based on size.
     * </p>
     *
     * @param object the object to compare to, must not be <code>null</code>.
     * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
     * than <tt>object</tt>, 0 if they are equal.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final BigRational object) {
        BigInteger nOd = numerator.multiply(object.denominator);
        BigInteger dOn = denominator.multiply(object.numerator);
        return nOd.compareTo(dOn);
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code BigInteger}, ie
     * {@code this * 1 / bg}, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@code BigInteger} to divide by, must not be {@code null}
     * @return a {@link BigRational} instance with the resulting values
     */
    public BigRational divide(final BigInteger bg) {
        if (bg == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        if (BigInteger.ZERO.equals(bg)) {
            throw new IllegalArgumentException("Zero denominator");
        }
        return new BigRational(numerator, denominator.multiply(bg));
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code int}, ie
     * {@code this * 1 / i}, returning the result in reduced form.
     * </p>
     *
     * @param i the {@code int} to divide by
     * @return a {@link BigRational} instance with the resulting values
     */
    public BigRational divide(final int i) {
        return divide(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Divide the value of this fraction by the passed {@code long}, ie
     * {@code this * 1 / l}, returning the result in reduced form.
     * </p>
     *
     * @param l the {@code long} to divide by
     * @return a {@link BigRational} instance with the resulting values
     */
    public BigRational divide(final long l) {
        return divide(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Divide the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction Fraction to divide by, must not be {@code null}.
     * @return a {@link BigRational} instance with the resulting values.
     */
    public BigRational divide(final BigRational fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        if (BigInteger.ZERO.equals(fraction.numerator)) {
            throw new IllegalArgumentException("Zero denominator");
        }

        return multiply(fraction.reciprocal());
    }

    /**
     * <p>
     * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     *
     * @return the fraction as a <tt>double</tt>
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        double result = numerator.doubleValue() / denominator.doubleValue();
        if (Double.isNaN(result)) {
            // Numerator and/or denominator must be out of range:
            // Calculate how far to shift them to put them in range.
            int shift = Math.max(numerator.bitLength(), denominator.bitLength())
                    - Math.getExponent(Double.MAX_VALUE);
            result = numerator.shiftRight(shift).doubleValue()
                    / denominator.shiftRight(shift).doubleValue();
        }
        return result;
    }

    /**
     * <p>
     * Test for the equality of two fractions. If the lowest term numerator and
     * denominators are the same for both fractions, the two fractions are
     * considered to be equal.
     * </p>
     *
     * @param other fraction to test for equality to this fraction, can be
     *              <code>null</code>.
     * @return true if two fractions are equal, false if object is
     * <code>null</code>, not an instance of {@link BigRational}, or not
     * equal to this fraction instance.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        boolean ret = false;

        if (this == other) {
            ret = true;
        } else if (other instanceof BigRational) {
            BigRational rhs = ((BigRational) other).reduce();
            BigRational thisOne = this.reduce();
            ret = thisOne.numerator.equals(rhs.numerator)
                    && thisOne.denominator.equals(rhs.denominator);
        }

        return ret;
    }

    /**
     * <p>
     * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
     * the numerator divided by denominator.
     * </p>
     *
     * @return the fraction as a <tt>float</tt>.
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        float result = numerator.floatValue() / denominator.floatValue();
        if (Double.isNaN(result)) {
            // Numerator and/or denominator must be out of range:
            // Calculate how far to shift them to put them in range.
            int shift = Math.max(numerator.bitLength(), denominator.bitLength())
                    - Math.getExponent(Float.MAX_VALUE);
            result = numerator.shiftRight(shift).floatValue()
                    / denominator.shiftRight(shift).floatValue();
        }
        return result;
    }

    /**
     * <p>
     * Access the denominator as a <code>BigInteger</code>.
     * </p>
     *
     * @return the denominator as a <code>BigInteger</code>.
     */
    public BigInteger getDenominator() {
        return denominator;
    }

    /**
     * <p>
     * Access the numerator as a <code>BigInteger</code>.
     * </p>
     *
     * @return the numerator as a <code>BigInteger</code>.
     */
    public BigInteger getNumerator() {
        return numerator;
    }

    /**
     * <p>
     * Gets a hashCode for the fraction.
     * </p>
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return 37 * (37 * 17 + numerator.hashCode()) + denominator.hashCode();
    }

    /**
     * <p>
     * Gets the fraction as an <tt>int</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     *
     * @return the whole number fraction part.
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return numerator.divide(denominator).intValue();
    }

    /**
     * <p>
     * Gets the fraction as a <tt>long</tt>. This returns the whole number part
     * of the fraction.
     * </p>
     *
     * @return the whole number fraction part.
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return numerator.divide(denominator).longValue();
    }

    /**
     * <p>
     * Multiplies the value of this fraction by the passed
     * <code>BigInteger</code>, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@code BigInteger} to multiply by.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigRational multiply(final BigInteger bg) {
        if (bg == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        return new BigRational(bg.multiply(numerator), denominator);
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>int</tt>, returning
     * the result in reduced form.
     * </p>
     *
     * @param i the <tt>int</tt> to multiply by.
     * @return a {@link BigRational} instance with the resulting values.
     */
    public BigRational multiply(final int i) {
        return multiply(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Multiply the value of this fraction by the passed <tt>long</tt>,
     * returning the result in reduced form.
     * </p>
     *
     * @param l the <tt>long</tt> to multiply by.
     * @return a {@link BigRational} instance with the resulting values.
     */
    public BigRational multiply(final long l) {
        return multiply(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Multiplies the value of this fraction by another, returning the result in
     * reduced form.
     * </p>
     *
     * @param fraction Fraction to multiply by, must not be {@code null}.
     * @return a {@link BigRational} instance with the resulting values.
     */
    public BigRational multiply(final BigRational fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        if (numerator.equals(BigInteger.ZERO) || fraction.numerator.equals(BigInteger.ZERO)) {
            return ZERO;
        }
        return new BigRational(numerator.multiply(fraction.numerator),
                denominator.multiply(fraction.denominator));
    }

    /**
     * <p>
     * Return the additive inverse of this fraction, returning the result in
     * reduced form.
     * </p>
     *
     * @return the negation of this fraction.
     */
    public BigRational negate() {
        return new BigRational(numerator.negate(), denominator);
    }

    /**
     * <p>
     * Gets the fraction percentage as a <tt>double</tt>. This calculates the
     * fraction as the numerator divided by denominator multiplied by 100.
     * </p>
     *
     * @return the fraction percentage as a <tt>double</tt>.
     */
    public double percentageValue() {
        return multiply(ONE_HUNDRED).doubleValue();
    }

    /**
     * <p>
     * Returns a {@code BigFraction} whose value is
     * {@code (this<sup>exponent</sup>)}, returning the result in reduced form.
     * </p>
     *
     * @param exponent exponent to which this {@code BigFraction} is to be raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public BigRational pow(final int exponent) {
        if (exponent < 0) {
            return new BigRational(denominator.pow(-exponent), numerator.pow(-exponent));
        }
        return new BigRational(numerator.pow(exponent), denominator.pow(exponent));
    }

    /**
     * Raise a BigInteger to a long power.
     *
     * @param k Number to raise.
     * @param e Exponent (must be positive or zero).
     * @return k<sup>e</sup>
     */
    private static BigInteger pow(final BigInteger k, long e) {
        if (e < 0) {
            throw new IllegalArgumentException("Exponent must be positive");
        }

        BigInteger result = BigInteger.ONE;
        BigInteger k2p = k;
        while (e != 0) {
            if ((e & 0x1) != 0) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e = e >> 1;
        }

        return result;

    }

    /**
     * Raise a BigInteger to a BigInteger power.
     *
     * @param k Number to raise.
     * @param e Exponent (must be positive or zero).
     * @return k<sup>e</sup>
     */
    public static BigInteger pow(final BigInteger k, BigInteger e) {
        if (e.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Exponent must be positive");
        }

        BigInteger result = BigInteger.ONE;
        BigInteger k2p = k;
        while (!BigInteger.ZERO.equals(e)) {
            if (e.testBit(0)) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
            e = e.shiftRight(1);
        }

        return result;
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent exponent to which this <code>BigFraction</code> is to be
     *                 raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigRational pow(final long exponent) {
        if (exponent < 0) {
            return new BigRational(pow(denominator, -exponent), pow(numerator, -exponent));
        }
        return new BigRational(pow(numerator, exponent), pow(denominator, exponent));
    }

    /**
     * <p>
     * Returns a <code>BigFraction</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent exponent to which this <code>BigFraction</code> is to be
     *                 raised.
     * @return <tt>this<sup>exponent</sup></tt> as a <code>BigFraction</code>.
     */
    public BigRational pow(final BigInteger exponent) {
        if (exponent.compareTo(BigInteger.ZERO) < 0) {
            final BigInteger eNeg = exponent.negate();
            return new BigRational(pow(denominator, eNeg), pow(numerator, eNeg));
        }
        return new BigRational(pow(numerator, exponent), pow(denominator, exponent));
    }

    /**
     * <p>
     * Returns a <code>double</code> whose value is
     * <tt>(this<sup>exponent</sup>)</tt>, returning the result in reduced form.
     * </p>
     *
     * @param exponent exponent to which this <code>BigFraction</code> is to be
     *                 raised.
     * @return <tt>this<sup>exponent</sup></tt>.
     */
    public double pow(final double exponent) {
        return Math.pow(numerator.doubleValue(), exponent)
                / Math.pow(denominator.doubleValue(), exponent);
    }

    /**
     * <p>
     * Return the multiplicative inverse of this fraction.
     * </p>
     *
     * @return the reciprocal fraction.
     */
    public BigRational reciprocal() {
        return new BigRational(denominator, numerator);
    }

    /**
     * <p>
     * Reduce this <code>BigFraction</code> to its lowest terms.
     * </p>
     *
     * @return the reduced <code>BigFraction</code>. It doesn't change anything
     * if the fraction can be reduced.
     */
    public BigRational reduce() {
        final BigInteger gcd = numerator.gcd(denominator);
        return new BigRational(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * <p>
     * Subtracts the value of an {@link BigInteger} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param bg the {@link BigInteger} to subtract, cannot be {@code null}.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigRational subtract(final BigInteger bg) {
        if (bg == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        return new BigRational(numerator.subtract(denominator.multiply(bg)), denominator);
    }

    /**
     * <p>
     * Subtracts the value of an {@code integer} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param i the {@code integer} to subtract.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigRational subtract(final int i) {
        return subtract(BigInteger.valueOf(i));
    }

    /**
     * <p>
     * Subtracts the value of a {@code long} from the value of this
     * {@code BigFraction}, returning the result in reduced form.
     * </p>
     *
     * @param l the {@code long} to subtract.
     * @return a {@code BigFraction} instance with the resulting values.
     */
    public BigRational subtract(final long l) {
        return subtract(BigInteger.valueOf(l));
    }

    /**
     * <p>
     * Subtracts the value of another fraction from the value of this one,
     * returning the result in reduced form.
     * </p>
     *
     * @param fraction {@link BigRational} to subtract, must not be {@code null}.
     * @return a {@link BigRational} instance with the resulting values
     */
    public BigRational subtract(final BigRational fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("Fraction is null");
        }
        if (ZERO.equals(fraction)) {
            return this;
        }

        BigInteger num = null;
        BigInteger den = null;
        if (denominator.equals(fraction.denominator)) {
            num = numerator.subtract(fraction.numerator);
            den = denominator;
        } else {
            num = (numerator.multiply(fraction.denominator)).subtract((fraction.numerator)
                    .multiply(denominator));
            den = denominator.multiply(fraction.denominator);
        }
        return new BigRational(num, den);

    }

    /**
     * <p>
     * Returns the <code>String</code> representing this fraction, ie
     * "num / dem" or just "num" if the denominator is one.
     * </p>
     *
     * @return a string representation of the fraction.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String str = null;
        if (BigInteger.ONE.equals(denominator)) {
            str = numerator.toString();
        } else if (BigInteger.ZERO.equals(numerator)) {
            str = "0";
        } else {
            str = numerator + "/" + denominator;
        }
        return str;
    }
}
