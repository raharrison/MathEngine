package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

public final class FalsePositionSolver extends RootBracketingMethod
{
	public FalsePositionSolver(Function targetFunction)
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

		double fa = f.evaluateAt(a);

		int iteration = 1;

		while (iteration <= this.iterations)
		{
			x = a - (b - a) * fa / (f.evaluateAt(b) - fa);
			double fx = f.evaluateAt(x);

			if (fa * fx > 0)
			{
				a = x;
			}
			else
			{
				b = x;
			}

			if (Math.abs(fx) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			iteration++;
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
