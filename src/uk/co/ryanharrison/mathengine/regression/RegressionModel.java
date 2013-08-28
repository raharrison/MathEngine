package uk.co.ryanharrison.mathengine.regression;

import uk.co.ryanharrison.mathengine.StatUtils;

public abstract class RegressionModel
{
	protected double[] x;

	protected double[] y;

	public RegressionModel(double[] x, double[] y)
	{
		this.x = x;
		this.y = y;
	}

	public abstract void compute();

	public abstract double evaluateAt(double x);

	public double getCoefficientOfDetermination()
	{
		double yMean = StatUtils.mean(y);

		double sse = 0;
		double sstot = 0;

		for (int i = 0; i < y.length; i++)
		{
			sse += Math.pow(y[i] - evaluateAt(x[i]), 2);

			sstot += Math.pow(y[i] - yMean, 2);
		}

		return 1 - sse / sstot;
	}

	public abstract double getCoefficientOfDetermination(BiasType biasType);

	public abstract double[] getCoefficients();

	public double getCorrelationCoefficent()
	{
		return Math.sqrt(getCoefficientOfDetermination());
	}

	public abstract double getCorrelationCoefficient(BiasType biasType);

	public double[] getResiduals()
	{
		double[] results = new double[y.length];

		for (int i = 0; i < y.length; i++)
		{
			results[i] = y[i] - evaluateAt(x[i]);
		}

		return results;
	}

	public double[] getX()
	{
		return x;
	}

	public double[] getY()
	{
		return y;
	}

	public void setX(double[] x)
	{
		this.x = x;
	}

	public void setY(double[] y)
	{
		this.y = y;
	}
}
