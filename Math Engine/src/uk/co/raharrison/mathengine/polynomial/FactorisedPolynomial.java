package uk.co.raharrison.mathengine.polynomial;

import uk.co.raharrison.mathengine.Complex;
import uk.co.raharrison.mathengine.MathUtils;

// Factorised polynomial p := set of polynomials p_1,...,p_k and their
// corresponding
// powers n_1,...,n_k, such that p = (p_1)^(n_1)*...*(p_k)^(n_k).
public class FactorisedPolynomial
{
	// Set of factors the polynomial consists of.
	public Polynomial[] Factor;

	// Set of powers, where Factor[i] is lifted
	// to Power[i].
	public int[] Power;

	public Complex evaluate(Complex x)
	{
		Complex z = Complex.ONE;

		for (int i = 0; i < Factor.length; i++)
		{
			z = z.multiply(Complex.Pow(Factor[i].evaluate(x), Power[i]));
		}

		return z;
	}

	public Polynomial expand()
	{
		Polynomial q = new Polynomial(Complex.ONE);

		for (int i = 0; i < Factor.length; i++)
		{
			for (int j = 0; j < Power[i]; j++)
				q = q.multiply(Factor[i]);

			q.clean();
		}

		// clean...
		for (int k = 0; k <= q.getDegree(); k++)
		{
			q.coefficients[k].setRe(MathUtils.round(q.coefficients[k].getRe(), 12));
			q.coefficients[k].setIm(MathUtils.round(q.coefficients[k].getIm(), 12));
		}

		return q;
	}

	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < Factor.length; i++)
		{
			b.append("(" + Factor[i].toString() + ")^" + Integer.toString(Power[i]) + " ");
		}

		return b.toString();
	}
}
