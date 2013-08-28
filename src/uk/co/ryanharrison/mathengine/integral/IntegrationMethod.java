package uk.co.ryanharrison.mathengine.integral;

import uk.co.ryanharrison.mathengine.Function;

public abstract class IntegrationMethod
{
	protected int iterations;
	protected Function targetFunction;
	protected double lower;
	protected double upper;

	public IntegrationMethod(Function function)
	{
		this.setTargetFunction(function);
		this.setIterations(10);
		this.setUpper(1);
		this.setLower(0);
	}

	public int getIterations()
	{
		return iterations;
	}

	public double getLower()
	{
		return lower;
	}

	public Function getTargetFunction()
	{
		return targetFunction;
	}

	public double getUpper()
	{
		return upper;
	}

	public abstract double integrate();

	public void setIterations(int iterations)
	{
		this.iterations = Math.abs(iterations);
	}

	public void setLower(double lower)
	{
		this.lower = lower;
	}

	public void setTargetFunction(Function targetFunction)
	{
		this.targetFunction = targetFunction;
	}

	public void setUpper(double upper)
	{
		this.upper = upper;
	}
}
