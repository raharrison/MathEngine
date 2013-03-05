package uk.co.raharrison.mathengine.solvers;

import uk.co.raharrison.mathengine.Function;

public final class RiddersSolver extends RootBracketingMethod
{
	public RiddersSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;

		double a = lowerBound;
		double b = upperBound;

		double x = b;
		double xOld = b;

		double fa = f.evaluateAt(a);
		double fb = f.evaluateAt(b);

		int iterations = 1;

		while (iterations <= this.iterations)
		{
			double c = 0.5 * (a + b);
			double fc = f.evaluateAt(c);
			double s = Math.sqrt(Math.pow(fc, 2) - fa * fb);

			double dx = (c - a) * fc / s;

			if (fa - fb < 0.0)
				dx = -dx;

			x = c + dx;
			double fx = f.evaluateAt(x);

			if (Math.abs(x - xOld) < tolerance /* * Math.Max(Math.Abs(x), 1.0) */
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			xOld = x;

			if (fc * fx > 0.0)
			{
				if (fa * fx < 0.0)
				{
					b = x;
					fb = fx;
				}
				else
				{
					a = x;
					fa = fx;
				}
			}
			else
			{
				a = c;
				b = x;
				fa = fc;
				fb = fx;
			}

			iterations++;
		}

		if (convergenceCriteria == ConvergenceCriteria.NumberOfIterations)
		{
			return x;
		}

		throw new UnsupportedOperationException(
				"Unable to find the root within specified tolerance after " + iterations
						+ " iterations");
	}
}
