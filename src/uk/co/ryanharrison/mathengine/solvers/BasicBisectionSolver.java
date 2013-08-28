package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

public final class BasicBisectionSolver extends RootBracketingMethod
{
	public BasicBisectionSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;

		double a = upperBound;
		double b = lowerBound;
		double x = b;

		int iterations = 1;

		while (iterations <= this.iterations)
		{
			x = (a + b) / 2;

			if (f.evaluateAt(x) == 0 || Math.abs(b - a) / 2 < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			if (f.evaluateAt(a) * f.evaluateAt(x) < 0)
			{
				b = x;
			}
			else
			{
				a = x;
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
