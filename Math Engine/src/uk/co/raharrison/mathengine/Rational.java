package uk.co.raharrison.mathengine;

import java.math.BigInteger;

/**
 * Representation of a rational number.
 */
public class Rational extends Number implements Comparable<Rational>
{
	private static final long serialVersionUID = 6542772102095700416L;

	/** A fraction representing "2 / 1". */
	public static final Rational TWO = new Rational(2, 1);

	/** A fraction representing "1". */
	public static final Rational ONE = new Rational(1, 1);

	/** A fraction representing "0". */
	public static final Rational ZERO = new Rational(0, 1);

	/** A fraction representing "4/5". */
	public static final Rational FOUR_FIFTHS = new Rational(4, 5);

	/** A fraction representing "1/5". */
	public static final Rational ONE_FIFTH = new Rational(1, 5);

	/** A fraction representing "1/2". */
	public static final Rational ONE_HALF = new Rational(1, 2);

	/** A fraction representing "1/4". */
	public static final Rational ONE_QUARTER = new Rational(1, 4);

	/** A fraction representing "1/3". */
	public static final Rational ONE_THIRD = new Rational(1, 3);

	/** A fraction representing "3/5". */
	public static final Rational THREE_FIFTHS = new Rational(3, 5);

	/** A fraction representing "3/4". */
	public static final Rational THREE_QUARTERS = new Rational(3, 4);

	/** A fraction representing "2/5". */
	public static final Rational TWO_FIFTHS = new Rational(2, 5);

	/** A fraction representing "2/4". */
	public static final Rational TWO_QUARTERS = new Rational(2, 4);

	/** A fraction representing "2/3". */
	public static final Rational TWO_THIRDS = new Rational(2, 3);

	/** A fraction representing "-1 / 1". */
	public static final Rational MINUS_ONE = new Rational(-1, 1);

	
	public static Rational getReducedFraction(int numerator, int denominator)
	{
		if (denominator == 0)
		{
			throw new IllegalArgumentException("Zero denominator");
		}
		if (numerator == 0)
		{
			return ZERO; // normalize zero.
		}
		// allow 2^k/-2^31 as a valid fraction (where k>0)
		if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0)
		{
			numerator /= 2;
			denominator /= 2;
		}
		if (denominator < 0)
		{
			if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE)
			{
				throw new UnsupportedOperationException("Overflow in Rational");
			}
			numerator = -numerator;
			denominator = -denominator;
		}
		// simplify fraction.
		int gcd = MathUtils.gcd(numerator, denominator);
		numerator /= gcd;
		denominator /= gcd;
		return new Rational(numerator, denominator);
	}

	/** The denominator. */
	private final int denominator;

	/** The numerator. */
	private final int numerator;

	/**
	 * Create a fraction given the double value.
	 * 
	 * @param value
	 *            the double value to convert to a fraction.
	 */
	public Rational(double value)
	{
		this(value, 1.0e-5, 100);
	}

	/**
	 * Create a fraction given the double value and maximum error allowed.
	 * @param value
	 *            the double value to convert to a fraction.
	 * @param epsilon
	 *            maximum error allowed. The resulting fraction is within
	 *            {@code epsilon} of {@code value}, in absolute terms.
	 * @param maxIterations
	 *            maximum number of convergents
	 */
	public Rational(double value, double epsilon, int maxIterations)
	{
		int maxDenominator = Integer.MAX_VALUE;
		long overflow = Integer.MAX_VALUE;
		double r0 = value;
		long a0 = (long) Math.floor(r0);
		if (a0 > overflow)
		{
			throw new UnsupportedOperationException("Cannot convert Rational");
		}

		// check for (almost) integer arguments, which should not go
		// to iterations.
		if (Math.abs(a0 - value) < epsilon)
		{
			this.numerator = (int) a0;
			this.denominator = 1;
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
		do
		{
			++n;
			double r1 = 1.0 / (r0 - a0);
			long a1 = (long) Math.floor(r1);
			p2 = (a1 * p1) + p0;
			q2 = (a1 * q1) + q0;
			if ((p2 > overflow) || (q2 > overflow))
			{
				throw new UnsupportedOperationException("Cannot convert Rational");
			}

			double convergent = (double) p2 / (double) q2;
			if (n < maxIterations && Math.abs(convergent - value) > epsilon && q2 < maxDenominator)
			{
				p0 = p1;
				p1 = p2;
				q0 = q1;
				q1 = q2;
				a0 = a1;
				r0 = r1;
			}
			else
			{
				stop = true;
			}
		}
		while (!stop);

		if (n >= maxIterations)
		{
			throw new UnsupportedOperationException("Cannot convert Rational");
		}

		if (q2 < maxDenominator)
		{
			this.numerator = (int) p2;
			this.denominator = (int) q2;
		}
		else
		{
			this.numerator = (int) p1;
			this.denominator = (int) q1;
		}
	}

	/**
	 * Create a fraction from an int. The fraction is num / 1.
	 * 
	 * @param num
	 *            the numerator.
	 */
	public Rational(int num)
	{
		this(num, 1);
	}

	/**
	 * Create a fraction given the numerator and denominator. The fraction is
	 * reduced to lowest terms.
	 * 
	 * @param num
	 *            the numerator.
	 * @param den
	 *            the denominator.
	 */
	public Rational(int num, int den)
	{
		if (den == 0)
		{
			throw new IllegalArgumentException("Zero denominator");
		}
		if (den < 0)
		{
			if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE)
			{
				throw new UnsupportedOperationException("Overflow in Rational");
			}
			num = -num;
			den = -den;
		}
		// reduce numerator and denominator by greatest common denominator.
		final int d = MathUtils.gcd(num, den);
		if (d > 1)
		{
			num /= d;
			den /= d;
		}

		// move sign to numerator.
		if (den < 0)
		{
			num = -num;
			den = -den;
		}
		this.numerator = num;
		this.denominator = den;
	}

	/**
	 * Returns the absolute value of this fraction.
	 * 
	 * @return the absolute value.
	 */
	public Rational abs()
	{
		Rational ret;
		if (numerator >= 0)
		{
			ret = this;
		}
		else
		{
			ret = negate();
		}
		return ret;
	}

	/**
	 * Add an integer to the fraction.
	 * 
	 * @param i
	 *            the <tt>integer</tt> to add.
	 * @return this + i
	 */
	public Rational add(final int i)
	{
		return new Rational(numerator + i * denominator, denominator);
	}

	/**
	 * <p>
	 * Adds the value of this fraction to another, returning the result in
	 * reduced form. The algorithm follows Knuth, 4.5.1.
	 * </p>
	 * 
	 * @param fraction
	 *            the fraction to add, must not be {@code null}
	 * @return a {@code Rational} instance with the resulting values
	 */
	public Rational add(Rational fraction)
	{
		return addSub(fraction, true /* add */);
	}

	/**
	 * Implement add and subtract using algorithm described in Knuth 4.5.1.
	 * 
	 * @param fraction
	 *            the fraction to subtract, must not be {@code null}
	 * @param isAdd
	 *            true to add, false to subtract
	 * @return a {@code Rational} instance with the resulting values
	 */
	private Rational addSub(Rational fraction, boolean isAdd)
	{
		if (fraction == null)
		{
			throw new NullPointerException("Rational is null");
		}
		// zero is identity for addition.
		if (numerator == 0)
		{
			return isAdd ? fraction : fraction.negate();
		}
		if (fraction.numerator == 0)
		{
			return this;
		}
		// if denominators are randomly distributed, d1 will be 1 about 61%
		// of the time.
		int d1 = MathUtils.gcd(denominator, fraction.denominator);
		if (d1 == 1)
		{
			// result is ( (u*v' +/- u'v) / u'v')
			int uvp = numerator * fraction.denominator;
			int upv = fraction.numerator * denominator;
			return new Rational(isAdd ? uvp + upv : uvp - upv, denominator * fraction.denominator);
		}
		// the quantity 't' requires 65 bits of precision; see knuth 4.5.1
		// exercise 7. we're going to use a BigInteger.
		// t = u(v'/d1) +/- v(u'/d1)
		BigInteger uvp = BigInteger.valueOf(numerator).multiply(
				BigInteger.valueOf(fraction.denominator / d1));
		BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(
				BigInteger.valueOf(denominator / d1));
		BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
		// but d2 doesn't need extra precision because
		// d2 = gcd(t,d1) = gcd(t mod d1, d1)
		int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
		int d2 = (tmodd1 == 0) ? d1 : MathUtils.gcd(tmodd1, d1);

		// result is (t/d2) / (u'/d1)(v'/d2)
		BigInteger w = t.divide(BigInteger.valueOf(d2));
		if (w.bitLength() > 31)
		{
			throw new UnsupportedOperationException("Numerator overflow after multiplication");
		}
		return new Rational(w.intValue(), (denominator / d1) * (fraction.denominator / d2));
	}

	/**
	 * Compares this object to another based on size.
	 * 
	 * @param object
	 *            the object to compare to
	 * @return -1 if this is less than <tt>object</tt>, +1 if this is greater
	 *         than <tt>object</tt>, 0 if they are equal.
	 */
	@Override
	public int compareTo(Rational object)
	{
		long nOd = ((long) numerator) * object.denominator;
		long dOn = ((long) denominator) * object.numerator;
		return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
	}

	/**
	 * Divide the fraction by an integer.
	 * 
	 * @param i
	 *            the <tt>integer</tt> to divide by.
	 * @return this * i
	 */
	public Rational divide(final int i)
	{
		return new Rational(numerator, denominator * i);
	}

	/**
	 * <p>
	 * Divide the value of this fraction by another.
	 * </p>
	 * 
	 * @param fraction
	 *            the fraction to divide by, must not be {@code null}
	 * @return a {@code Rational} instance with the resulting values
	 */
	public Rational divide(Rational fraction)
	{
		if (fraction == null)
		{
			throw new NullPointerException("Rational is null");
		}
		if (fraction.numerator == 0)
		{
			throw new UnsupportedOperationException("Zero rational to divide by");
		}
		
		return multiply(fraction.reciprocal());
	}

	/**
	 * Gets the fraction as a <tt>double</tt>. This calculates the fraction as
	 * the numerator divided by denominator.
	 * 
	 * @return the fraction as a <tt>double</tt>
	 */
	@Override
	public double doubleValue()
	{
		return (double) numerator / (double) denominator;
	}

	/**
	 * Test for the equality of two fractions. If the lowest term numerator and
	 * denominators are the same for both fractions, the two fractions are
	 * considered to be equal.
	 * 
	 * @param other
	 *            fraction to test for equality to this fraction
	 * @return true if two fractions are equal, false if object is <tt>null</tt>
	 *         , not an instance of {@link Rational}, or not equal to this
	 *         fraction instance.
	 */
	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other instanceof Rational)
		{
			// since fractions are always in lowest terms, numerators and
			// denominators can be compared directly for equality.
			Rational rhs = (Rational) other;
			return (numerator == rhs.numerator) && (denominator == rhs.denominator);
		}
		return false;
	}

	/**
	 * Gets the fraction as a <tt>float</tt>. This calculates the fraction as
	 * the numerator divided by denominator.
	 * 
	 * @return the fraction as a <tt>float</tt>
	 */
	@Override
	public float floatValue()
	{
		return (float) doubleValue();
	}

	/**
	 * Access the denominator.
	 * 
	 * @return the denominator.
	 */
	public int getDenominator()
	{
		return denominator;
	}

	/**
	 * Access the numerator.
	 * 
	 * @return the numerator.
	 */
	public int getNumerator()
	{
		return numerator;
	}

	/**
	 * Gets a hashCode for the fraction.
	 * 
	 * @return a hash code value for this object
	 */
	@Override
	public int hashCode()
	{
		return 37 * (37 * 17 + numerator) + denominator;
	}

	/**
	 * Gets the fraction as an <tt>int</tt>. This returns the whole number part
	 * of the fraction.
	 * 
	 * @return the whole number fraction part
	 */
	@Override
	public int intValue()
	{
		return (int) doubleValue();
	}

	/**
	 * Gets the fraction as a <tt>long</tt>. This returns the whole number part
	 * of the fraction.
	 * 
	 * @return the whole number fraction part
	 */
	@Override
	public long longValue()
	{
		return (long) doubleValue();
	}

	/**
	 * Multiply the fraction by an integer.
	 * 
	 * @param i
	 *            the <tt>integer</tt> to multiply by.
	 * @return this * i
	 */
	public Rational multiply(final int i)
	{
		return new Rational(numerator * i, denominator);
	}

	/**
	 * <p>
	 * Multiplies the value of this fraction by another, returning the result in
	 * reduced form.
	 * </p>
	 * 
	 * @param fraction
	 *            the fraction to multiply by, must not be {@code null}
	 * @return a {@code Rational} instance with the resulting values
	 */
	public Rational multiply(Rational fraction)
	{
		if (fraction == null)
		{
			throw new NullPointerException("Rational is null");
		}
		if (numerator == 0 || fraction.numerator == 0)
		{
			return ZERO;
		}
		// knuth 4.5.1
		// make sure we don't overflow unless the result *must* overflow.
		int d1 = MathUtils.gcd(numerator, fraction.denominator);
		int d2 = MathUtils.gcd(fraction.numerator, denominator);
		
		return getReducedFraction((numerator / d1) * (fraction.numerator / d2), (denominator / d2)
				* (fraction.denominator / d1));
	}

	/**
	 * Return the additive inverse of this fraction.
	 * 
	 * @return the negation of this fraction.
	 */
	public Rational negate()
	{
		if (numerator == Integer.MIN_VALUE)
		{
			throw new UnsupportedOperationException("Overflow in Rational");
		}
		return new Rational(-numerator, denominator);
	}

	/**
	 * <p>
	 * Gets the fraction percentage as a <tt>double</tt>. This calculates the
	 * fraction as the numerator divided by denominator multiplied by 100.
	 * </p>
	 * 
	 * @return the fraction percentage as a <tt>double</tt>.
	 */
	public double percentageValue()
	{
		return multiply(100).doubleValue();
	}

	/**
	 * Return the multiplicative inverse of this fraction.
	 * 
	 * @return the reciprocal fraction
	 */
	public Rational reciprocal()
	{
		return new Rational(denominator, numerator);
	}

	/**
	 * Subtract an integer from the fraction.
	 * 
	 * @param i
	 *            the <tt>integer</tt> to subtract.
	 * @return this - i
	 */
	public Rational subtract(final int i)
	{
		return new Rational(numerator - i * denominator, denominator);
	}

	/**
	 * <p>
	 * Subtracts the value of another fraction from the value of this one,
	 * returning the result in reduced form.
	 * </p>
	 * 
	 * @param fraction
	 *            the fraction to subtract, must not be {@code null}
	 * @return a {@code Rational} instance with the resulting values
	 */
	public Rational subtract(Rational fraction)
	{
		return addSub(fraction, false /* subtract */);
	}

	/**
	 * <p>
	 * Returns the {@code String} representing this fraction, ie "num / dem" or
	 * just "num" if the denominator is one.
	 * </p>
	 * 
	 * @return a string representation of the fraction.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String str = null;
		if (denominator == 1)
		{
			str = Integer.toString(numerator);
		}
		else if (numerator == 0)
		{
			str = "0";
		}
		else
		{
			str = numerator + " / " + denominator;
		}
		return str;
	}
}
