package uk.co.raharrison.mathengine.solvers;

import uk.co.raharrison.mathengine.Function;

public final class BrentSolver extends RootBracketingMethod
{
	public BrentSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;

		double a = this.lowerBound;
		double b = this.upperBound;

		double c = 0, d = 0, e = 0, fa, fb, fc = 0, tol, m, p, q, r, s;

		int iterations = 0;
		double t = this.tolerance;

		fa = f.evaluateAt(a);
		fb = f.evaluateAt(b);

		boolean label_int = true;
		boolean label_ext = true;

		while (label_int || label_ext)
		{
			if (label_int)
			{
				c = a;
				fc = fa;
				d = e = b - a;
				label_int = false;
			}

			if (label_ext)
			{
				if (Math.abs(fc) < Math.abs(fb))
				{
					a = b;
					b = c;
					c = a;
					fa = fb;
					fb = fc;
					fc = fa;
				}
				label_ext = false;
			}

			iterations++;

			tol = 2.0 * t * Math.abs(b) + t;
			m = 0.5 * (c - b);
			if (Math.abs(m) > tol && !(fb == 0)) // exact comparison with 0 is
													// OK here
			{
				// See if bisection is forced
				if (Math.abs(e) < tol || Math.abs(fa) <= Math.abs(fb))
				{
					d = e = m;
				}
				else
				{
					s = fb / fa;
					if (a == c)
					{
						// linear interpolation
						p = 2.0 * m * s;
						q = 1.0 - s;
					}
					else
					{
						// Inverse quadratic interpolation
						q = fa / fc;
						r = fb / fc;
						p = s * (2.0 * m * q * (q - r) - (b - a) * (r - 1.0));
						q = (q - 1.0) * (r - 1.0) * (s - 1.0);
					}
					if (p > 0.0)
						q = -q;
					else
						p = -p;
					s = e;
					e = d;
					if (2.0 * p < 3.0 * m * q - Math.abs(tol * q) && p < Math.abs(0.5 * s * q))
						d = p / q;
					else
						d = e = m;
				}
				a = b;
				fa = fb;
				if (Math.abs(d) > tol)
					b += d;
				else if (m > 0.0)
					b += tol;
				else
					b -= tol;

				if (Math.abs(b - a) < tolerance
						&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
				{
					return b;
				}

				if (iterations == this.iterations)
				{
					if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
						return b;

					throw new UnsupportedOperationException(
							"Unable to find the root within specified tolerance after "
									+ iterations + " iterations");
				}

				fb = f.evaluateAt(b);
				if (fb > 0.0 && fc > 0.0 || fb <= 0.0 && fc <= 0.0)
					label_int = true;

				label_ext = true;
			}
		}

		return b;
	}
}