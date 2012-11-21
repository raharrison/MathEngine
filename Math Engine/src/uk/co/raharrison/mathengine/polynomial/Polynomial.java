package uk.co.raharrison.mathengine.polynomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import uk.co.raharrison.mathengine.Complex;
import uk.co.raharrison.mathengine.MathUtils;

public class Polynomial
{
	protected Complex[] coefficients;

	public Polynomial()
	{
		coefficients = new Complex[1];
		coefficients[0] = Complex.ZERO;
	}

	public Polynomial(Complex... coeffs)
	{
		if (coeffs == null || coeffs.length < 1)
		{
			coefficients = new Complex[1];
			coefficients[0] = Complex.ZERO;
		}
		else
		{
			coefficients = coeffs.clone();
		}
	}

	public Polynomial(Complex c)
	{
		coefficients = new Complex[1];

		if (c == null)
			coefficients[0] = Complex.ZERO;
		else
			coefficients[0] = c;
	}

	public Polynomial(double... coeffs)
	{
		if (coeffs == null || coeffs.length < 1)
		{
			coefficients = new Complex[1];
			coefficients[0] = Complex.ZERO;
		}
		else
		{
			coefficients = new Complex[coeffs.length];
			for (int i = 0; i < coeffs.length; i++)
				coefficients[i] = new Complex(coeffs[i]);
		}
	}

	public Polynomial(double c)
	{
		coefficients = new Complex[1];

		coefficients[0] = new Complex(c);
	}

	public Polynomial(int deg)
	{
		coefficients = new Complex[deg + 1];

		for (int i = 0; i < coefficients.length; i++)
		{
			coefficients[i] = Complex.ZERO;
		}
	}

	public Polynomial(String polynomial)
	{
		ArrayList<PolynomialTerm> terms;

		try
		{
			terms = readPolyExpression(polynomial.replaceAll(" ", "").trim());
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Invalid polynomial string");
		}

		int degree = 0;

		for (PolynomialTerm T : terms)
		{
			if (T.getPower() > degree)
				degree = T.getPower();
		}

		this.coefficients = new Complex[degree + 1];

		for (int i = 0; i < coefficients.length; i++)
		{
			this.coefficients[i] = new Complex(0);
		}

		for (PolynomialTerm T : terms)
		{
			this.coefficients[T.getPower()] = this.coefficients[T.getPower()].add(new Complex(T
					.getCoefficient()));
		}
	}

	public Polynomial add(Polynomial q)
	{
		int degree = Math.max(getDegree(), q.getDegree());

		Complex[] coeffs = new Complex[degree + 1];

		for (int i = 0; i <= degree; i++)
		{
			if (i > getDegree())
				coeffs[i] = q.coefficients[i];
			else if (i > q.getDegree())
				coeffs[i] = coefficients[i];
			else
				coeffs[i] = coefficients[i].add(q.coefficients[i]);
		}

		return new Polynomial(coeffs);
	}

	public void appendCoefficient(int power, double coefficient)
	{
		coefficients[power] = coefficients[power].add(coefficient);
	}

	public void clean()
	{
		int i;

		for (i = getDegree(); i >= 0 && coefficients[i].equals(0); i--)
			;

		Complex[] coeffs = new Complex[i + 1];

		for (int k = 0; k <= i; k++)
			coeffs[k] = coefficients[k];

		coefficients = coeffs.clone();
	}

	@Override
	public Polynomial clone()
	{
		return new Polynomial(coefficients.clone());
	}

	public Polynomial derivative()
	{
		Complex[] buf = new Complex[this.getDegree()];

		for (int i = 0; i < buf.length; i++)
			buf[i] = coefficients[i + 1].multiply(i + 1);

		return new Polynomial(buf);
	}

	public Complex differentiate(Complex x)
	{
		Complex[] buf = new Complex[getDegree()];

		for (int i = 0; i < buf.length; i++)
			buf[i] = coefficients[i + 1].multiply(i + 1);

		return new Polynomial(buf).evaluate(x);
	}

	public Polynomial divide(Complex d)
	{
		Complex[] coeffs = new Complex[getDegree() + 1];

		for (int i = 0; i < coeffs.length; i++)
			coeffs[i] = coefficients[i].divide(d);

		return new Polynomial(coeffs);
	}

	public Polynomial divide(double d)
	{
		Complex[] coeffs = new Complex[getDegree() + 1];

		for (int i = 0; i < coeffs.length; i++)
			coeffs[i] = coefficients[i].leftDivide(d);

		return new Polynomial(coeffs);
	}

	public Polynomial divide(Polynomial div)
	{
		return this.divide(div, true);
	}

	private Polynomial divide(Polynomial div, boolean returnQuotient)
	{
		int maxdeg = div.getDegree();

		if (maxdeg == 0)
		{
			if (returnQuotient)
				return this.divide(div.getCoefficient(0));
			else
				return new Polynomial(0);
		}

		// rem is the remainder "carried down" in long division
		Polynomial rem = this.clone();
		// running is the "running total" of the quotient
		Polynomial running = new Polynomial(Math.abs(this.getDegree() - maxdeg));

		for (int i = this.getDegree(); i >= maxdeg; i--)
		{
			running.safeSetCoefficient(i - maxdeg, rem.getCoefficient(i));
			rem = this.subtract(div.multiply(running));
		}

		running.clean();
		rem.clean();

		if (returnQuotient)
			return running;
		else
			return rem;
	}

	public Complex evaluate(Complex x)
	{
		Complex buf = coefficients[getDegree()];

		for (int i = getDegree() - 1; i >= 0; i--)
		{
			buf = coefficients[i].add(x.multiply(buf));
		}

		return buf;
	}

	public FactorisedPolynomial factorise()
	{
		// this is to be returned
		FactorisedPolynomial p = new FactorisedPolynomial();

		// cannot factorise polynomial of degree 0 or 1
		if (this.getDegree() <= 1)
		{
			p.Factor = new Polynomial[] { this.clone() };
			p.Power = new int[] { 1 };

			return p;
		}

		Complex[] roots = this.roots();

		Polynomial[] factor = new Polynomial[roots.length];
		int[] power = new int[roots.length];

		power[0] = 1;
		factor[0] = new Polynomial(new Complex[] {
				Complex.round(coefficients[getDegree()].uminus().multiply(roots[0]), 10),
				coefficients[getDegree()] });

		for (int i = 1; i < roots.length; i++)
		{
			power[i] = 1;
			factor[i] = new Polynomial(new Complex[] { Complex.round(roots[i].uminus(), 12),
					Complex.ONE });
		}

		p.Factor = factor;
		p.Power = power;

		return p;
	}

	public Complex getCoefficient(int i)
	{
		if (i > getDegree() || i < 0)
			return Complex.ZERO;

		return coefficients[i];
	}

	public int getDegree()
	{
		return this.coefficients.length - 1;
	}

	public int getNonZeroDegree()
	{

		for (int i = coefficients.length - 1; i >= 0; i--)
		{
			if (coefficients[i].notEquals(Complex.ZERO))
			{
				return i;
			}
		}

		return 0;
	}

	public Polynomial integral()
	{
		Complex[] buf = new Complex[getDegree() + 2];
		buf[0] = Complex.ZERO; // this value can be arbitrary, in fact

		for (int i = 1; i < buf.length; i++)
			buf[i] = coefficients[i - 1].leftDivide(i);

		return new Polynomial(buf);
	}

	public Complex integrate(Complex a, Complex b)
	{
		Complex[] buf = new Complex[getDegree() + 2];
		buf[0] = Complex.ZERO; // this value can be arbitrary, in fact

		for (int i = 1; i < buf.length; i++)
			buf[i] = coefficients[i - 1].leftDivide(i);

		Polynomial p = new Polynomial(buf);

		return p.evaluate(b).subtract(p.evaluate(a));
	}

	public boolean isZero()
	{
		for (int i = 0; i < coefficients.length; i++)
			if (coefficients[i].notEquals(0))
				return false;

		return true;
	}

	public Polynomial multiply(Complex d)
	{
		Complex[] coeffs = new Complex[getDegree() + 1];

		for (int i = 0; i < coeffs.length; i++)
			coeffs[i] = d.multiply(coefficients[i]);

		return new Polynomial(coeffs);
	}

	public Polynomial multiply(double d)
	{
		Complex[] coeffs = new Complex[getDegree() + 1];

		for (int i = 0; i < coeffs.length; i++)
			coeffs[i] = coefficients[i].multiply(d);

		return new Polynomial(coeffs);
	}

	public Polynomial multiply(Polynomial other)
	{
		Polynomial output;

		output = new Polynomial(other.getDegree() + this.getDegree());

		for (int a = 0; a <= this.getNonZeroDegree(); a++)
			for (int b = 0; b <= other.getNonZeroDegree(); b++)
				output.safeSetCoefficient(
						a + b,
						output.getCoefficient(a + b).add(
								other.getCoefficient(b).multiply(this.getCoefficient(a))));

		return output;

	}

	public void normalize()
	{
		this.clean();

		if (coefficients[getDegree()].notEquals(Complex.ONE))
			for (int k = 0; k <= getDegree(); k++)
				coefficients[k] = coefficients[k].divide(coefficients[getDegree()]);
	}

	@Deprecated
	public Polynomial oldDivide(Polynomial divisor)
	{
		Complex divise = divisor.coefficients[0].uminus();
		Complex[] coeff = this.coefficients.clone();

		Collections.reverse(Arrays.asList(coeff));

		if (divisor.getDegree() > 1)
			throw new IllegalArgumentException("Must provide linear divisor");
		else
		{
			for (int i = 1; i < coeff.length; i++)
			{
				coeff[i] = coeff[i].add(coeff[i - 1].multiply(divise));
			}
		}

		Complex[] expr = new Complex[coeff.length - 1];

		for (int i = 0; i < coeff.length - 1; i++)
		{
			expr[i] = coeff[i];
		}

		Collections.reverse(Arrays.asList(expr));

		Polynomial poly = new Polynomial(expr);

		return poly;
	}

	public Polynomial pow(int k)
	{
		if (k == 0)
			return monomial(0);
		else if (k == 1)
			return this.clone();
		else
		{
			// Polynomial p = (Polynomial) this.clone();
			return this.multiply(this.pow(k - 1));
		}
	}

	private ArrayList<PolynomialTerm> readPolyExpression(String PolyExpression)
	{
		ArrayList<PolynomialTerm> termCollection = new ArrayList<PolynomialTerm>();

		String NextChar = "";
		String NextTerm = "";

		for (int i = 0; i < PolyExpression.length(); i++)
		{
			NextChar = PolyExpression.substring(i, i + 1);
			if (NextChar.equals("-") | NextChar.equals("+") && i > 0
					&& NextTerm.charAt(NextTerm.length() - 1) != 'E')
			{
				PolynomialTerm TermItem = new PolynomialTerm(NextTerm);
				termCollection.add(TermItem);
				NextTerm = "";
			}
			NextTerm += NextChar;
		}
		PolynomialTerm Item = new PolynomialTerm(NextTerm);
		termCollection.add(Item);

		return termCollection;
	}

	public Polynomial remainder(Polynomial div)
	{
		return this.divide(div, false);
	}

	public Complex[] roots()
	{
		return roots(1E-12, 100);
	}

	public Complex[] roots(double tolerance, int max_iterations)
	{
		Polynomial q = this.clone();
		q.normalize();

		Complex[] z = new Complex[q.getDegree()]; // approx. for roots
		Complex[] w = new Complex[q.getDegree()]; // Weierstraﬂ corrections

		// init z
		for (int k = 0; k < q.getDegree(); k++)
			// z[k] = (new Complex(.4, .9)) ^ k;
			z[k] = Complex.exp(Complex.I.multiply(2 * Math.PI * k).leftDivide(q.getDegree()));

		for (int iter = 0; iter < max_iterations && maxValue(q, z) > tolerance; iter++)
			for (int i = 0; i < 10; i++)
			{
				for (int k = 0; k < q.getDegree(); k++)
					w[k] = q.evaluate(z[k]).divide(weierNull(z, k));

				for (int k = 0; k < q.getDegree(); k++)
					z[k] = z[k].subtract(w[k]);
			}

		// clean...
		for (int t = 0; t < q.getDegree(); t++)
		{
			z[t].setRe(MathUtils.round(z[t].getRe(), 12));
			z[t].setIm(MathUtils.round(z[t].getIm(), 12));
		}

		return z;
	}

	// Sets the value of the coefficient r, expanding the array if necessary
	public void safeSetCoefficient(int r, Complex value)
	{
		if (r < 0)
		{
			throw new IllegalArgumentException("Invalid degree");
		}
		if (r > getDegree())
		{ // out of range, must extend the array

			int newlength;

			newlength = Math.max(coefficients.length, r + 1);

			Complex[] newcoeffs = new Complex[newlength];

			for (int i = 0; i < coefficients.length; i++)
				newcoeffs[i] = coefficients[i];

			newcoeffs[r] = value;
			coefficients = newcoeffs;
		}
		else
			// within range, can just set the value
			coefficients[r] = value;

	}

	public Polynomial subtract(Polynomial q)
	{
		return this.add(q.uminus());
	}

	@Override
	public String toString()
	{
		if (this.isZero())
			return "0";
		else
		{
			String s = "";

			for (int i = 0; i < getDegree() + 1; i++)
			{
				if (coefficients[i] != Complex.ZERO)
				{
					if (coefficients[i] == Complex.I)
						s += "i";
					else if (coefficients[i] != Complex.ONE)
					{
						if (coefficients[i].isReal() && coefficients[i].getRe() > 0)
							s += coefficients[i].toString();
						else
							s += "(" + coefficients[i].toString() + ")";

					}
					else if (/* Coefficients[i] == Complex.One && */i == 0)
						s += 1;

					if (i == 1)
						s += "x";
					else if (i > 1)
						s += "x^" + Integer.toString(i);
				}

				if (i < getDegree() && coefficients[i + 1].notEquals(0) && s.length() > 0)
					s += " + ";
			}

			return s;
		}
	}

	public Polynomial uminus()
	{
		Complex[] coeffs = new Complex[getDegree() + 1];

		for (int i = 0; i < coeffs.length; i++)
			coeffs[i] = coefficients[i].uminus();

		return new Polynomial(coeffs);
	}

	public static boolean isPolynomial(String polynomial)
	{
		try
		{
			new Polynomial(polynomial);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static double maxValue(Polynomial p, Complex[] z)
	{
		double buf = 0;

		for (int i = 0; i < z.length; i++)
		{
			if (Complex.abs(p.evaluate(z[i])) > buf)
				buf = Complex.abs(p.evaluate(z[i]));
		}

		return buf;
	}

	public static Polynomial monomial(int degree)
	{
		if (degree == 0)
			return new Polynomial(1);

		Complex[] coeffs = new Complex[degree + 1];

		for (int i = 0; i < degree; i++)
			coeffs[i] = Complex.ZERO;

		coeffs[degree] = Complex.ONE;

		return new Polynomial(coeffs);
	}

	// / <summary>
	// / For g(x) = (x-z_0)*...*(x-z_n), this method returns
	// / g'(z_k) = \prod_{j != k} (z_k - z_j).
	// / </summary>
	// / <param name="z"></param>
	// / <returns></returns>
	private static Complex weierNull(Complex[] z, int k)
	{
		if (k < 0 || k >= z.length)
			throw new IllegalArgumentException();

		Complex buf = Complex.ONE;

		for (int j = 0; j < z.length; j++)
			if (j != k)
				buf = buf.multiply(z[k].subtract(z[j]));

		return buf;
	}
}
