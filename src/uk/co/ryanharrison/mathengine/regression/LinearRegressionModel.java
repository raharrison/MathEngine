package uk.co.ryanharrison.mathengine.regression;

import uk.co.ryanharrison.mathengine.StatUtils;

public class LinearRegressionModel extends RegressionModel
{
	protected double a, b;

	public LinearRegressionModel(double[] x, double[] y)
	{
		super(x, y);
		a = b = 0;
	}

	@Override
	public void compute()
	{
		double a, b;

		// throws exception if regression can not be performed
		if (x.length < 2 | y.length < 2)
		{
			throw new IllegalArgumentException("Must have more than two values");
		}

		if (x.length == y.length)
		{
			b = StatUtils.covariance(x, y) / StatUtils.variance(x);

			a = StatUtils.mean(y) - b * StatUtils.mean(x);
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException(
					"Must have same number of values in each array");
		}

		this.a = a;
		this.b = b;
	}

	@Override
	public double evaluateAt(double x)
	{
		return a + b * x;
	}

	public double getA()
	{
		return a;
	}

	public double getB()
	{
		return b;
	}

	@Override
	public double getCoefficientOfDetermination(BiasType biasType)
	{
		return super.getCoefficientOfDetermination();
	}

	@Override
	public double[] getCoefficients()
	{
		double[] results = new double[2];

		results[0] = a;
		results[1] = b;

		return results;
	}

	@Override
	public double getCorrelationCoefficient(BiasType biasType)
	{
		return super.getCorrelationCoefficent();
	}

	@Override
	public String toString()
	{
		return String.format("%f + %f * x", a, b);
	}
}
