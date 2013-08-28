package uk.co.ryanharrison.mathengine.solvers;

import uk.co.ryanharrison.mathengine.Function;
import uk.co.ryanharrison.mathengine.differential.ExtendedCentralDifferenceMethod;

public final class NewtonBisectionSolver extends RootBracketingMethod
{
	private Function derivativefunction;
	private boolean useNumericalDiff;

	public NewtonBisectionSolver(Function targetFunction)
	{
		super(targetFunction);
		this.setUseNumericalDiff(true);
	}

	public NewtonBisectionSolver(Function targetFunction, Function derivativeFunction)
	{
		super(targetFunction);
		this.setDerivativefunction(derivativeFunction);
		this.setUseNumericalDiff(false);
	}

	public Function getDerivativefunction()
	{
		return derivativefunction;
	}

	public boolean isUseNumericalDiff()
	{
		return useNumericalDiff;
	}

	public void setDerivativefunction(Function derivativefunction)
	{
		this.derivativefunction = derivativefunction;
	}

	public void setUseNumericalDiff(boolean useNumericalDiff)
	{
		if (!useNumericalDiff && derivativefunction == null)
			throw new IllegalArgumentException("Derivative function has not been set");

		this.useNumericalDiff = useNumericalDiff;
	}

	@Override
	public double solve()
	{
		super.checkRootFindingParams();

		Function f = this.targetFunction;
		ExtendedCentralDifferenceMethod diff = new ExtendedCentralDifferenceMethod(targetFunction);

		double a = upperBound;
		double b = lowerBound;

		double fa = f.evaluateAt(a);

		double x = 0.5 * (a + b);

		int iterations = 1;

		while (iterations <= this.iterations)
		{
			double fx = f.evaluateAt(x);

			if (Math.abs(fx) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
			}

			if (fa * fx < 0.0D)
			{
				b = x;
			}
			else
			{
				a = x;
			}

			double dfx;

			if (useNumericalDiff)
			{
				diff.setTargetPoint(x);
				dfx = diff.deriveFirst();
			}
			else
			{
				dfx = derivativefunction.evaluateAt(x);
			}

			double dx = -fx / dfx;

			x = x + dx;

			if ((b - x) * (x - a) < 0.0D)
			{
				dx = 0.5 * (b - a);
				x = a + dx;
			}

			if (Math.abs(dx) < tolerance
					&& convergenceCriteria == ConvergenceCriteria.WithinTolerance)
			{
				return x;
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
