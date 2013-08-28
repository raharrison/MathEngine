package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;

public final class SecantSolver extends RootBracketingMethod
{
	public SecantSolver(Function targetFunction)
	{
		super(targetFunction);
	}

	@Override
	public double solve()
	{
		checkRootFindingParams();

		Function f = this.targetFunction;

		double a = lowerBound;
		double b = upperBound;

		double fb = f.evaluateAt(b);
		double x = b - (b - a) * fb / (fb - f.evaluateAt(a));

		int iteration = 1;

		while (iteration <= this.iterations)
		{
			x = b - (b - a) * fb / (fb - f.evaluateAt(a));
			a = b;
			b = x;
			fb = f.evaluateAt(b);

			if (Math.abs(fb) < tolerance
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
