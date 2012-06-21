package uk.co.raharrison.mathengine.solvers;

import uk.co.raharrison.mathengine.Function;

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

		double fa = f.evaluate(a);

		int iteration = 1;

		while (iteration <= this.iterations)
		{
			x = a - (b - a) * fa / (f.evaluate(b) - fa);
			double fx = f.evaluate(x);

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
