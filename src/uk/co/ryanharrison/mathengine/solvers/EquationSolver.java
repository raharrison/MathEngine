package uk.co.ryanharrison.mathengine.solvers;

import java.util.ArrayList;

import uk.co.ryanharrison.mathengine.Function;

public abstract class EquationSolver
{
	protected Function targetFunction;
	protected double tolerance;
	protected int iterations;
	protected ConvergenceCriteria convergenceCriteria;

	public EquationSolver()
	{
		iterations = 1;
		tolerance = 1E-5;
		convergenceCriteria = ConvergenceCriteria.NumberOfIterations;
	}

	protected void checkRootFindingParams()
	{
		if (iterations <= 0)
		{
			throw new IllegalArgumentException("Iterations cannot be less than zero");
		}
	}

	protected ArrayList<RootInterval> findBrackets(Function function, double lower, double upper,
			int subdivs)
	{
		ArrayList<RootInterval> bracks = new ArrayList<RootInterval>(subdivs);

		double dx = (upper - lower) / subdivs; // midpoint
		double x = lower;
		double fp = function.evaluateAt(x); // evaluate at lower

		for (int j = 0; j < subdivs; j++)
		{
			x += dx;
			double fc = function.evaluateAt(x);

			if (fc * fp < 0.0D)
			{
				bracks.add(new RootInterval(x - dx, x));
			}

			fp = fc;
		}

		return bracks;
	}

	public ConvergenceCriteria getConvergenceCriteria()
	{
		return convergenceCriteria;
	}

	public int getIterations()
	{
		return this.iterations;
	}

	public Function getTargetFunction()
	{
		return this.targetFunction;
	}

	public double getTolerance()
	{
		return this.tolerance;
	}

	public void setConvergenceCriteria(ConvergenceCriteria convergenceCriteria)
	{
		this.convergenceCriteria = convergenceCriteria;
	}

	public void setIterations(int iterations)
	{
		this.iterations = iterations;
	}

	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	public void setTolerance(double tolerance)
	{
		this.tolerance = Math.abs(tolerance);
	}

	public abstract double solve();
}
