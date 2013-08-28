package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

public abstract class NumericalDifferentiationMethod
{
	protected double targetPoint;
	protected double h;
	protected Function targetFunction;
	protected boolean optimize;

	public NumericalDifferentiationMethod(Function function)
	{
		this(function, 0.01);
		this.optimize = true;
	}

	public NumericalDifferentiationMethod(Function function, double h)
	{
		this.targetFunction = function;
		this.h = h;
		this.optimize = false;
		this.targetPoint = 1;
	}

	public abstract double deriveFirst();

	public abstract double deriveFourth();

	public abstract double deriveSecond();

	public abstract double deriveThird();

	public double getH()
	{
		return h;
	}

	public Function getTargetFunction()
	{
		return targetFunction;
	}

	public double getTargetPoint()
	{
		return targetPoint;
	}

	public boolean isOptimize()
	{
		return optimize;
	}

	protected double optimizeH(double x)
	{
		if (Math.abs(x - 0) < 1E-9)
			return h;

		double h = Math.sqrt(2.2E-16) * x;
		double temp = x + h;
		return temp - x;
	}

	public void setH(double h)
	{
		this.h = h;
	}

	public void setOptimize(boolean optimize)
	{
		this.optimize = optimize;
	}

	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	public void setTargetPoint(double targetPoint)
	{
		this.targetPoint = targetPoint;
	}
}
