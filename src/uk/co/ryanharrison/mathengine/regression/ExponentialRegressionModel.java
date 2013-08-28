package uk.co.ryanharrison.mathengine.regression;

public class ExponentialRegressionModel extends LinearRegressionModel
{
	private double[] newy;

	public ExponentialRegressionModel(double[] x, double[] y)
	{
		super(x, y);
	}

	@Override
	public void compute()
	{
		// throws exception if regression can not be performed
		if (x.length < 2 | y.length < 2)
		{
			throw new IllegalArgumentException("Must have more than two values");
		}

		if (x.length == y.length)
		{
			newy = new double[y.length];

			for (int i = 0; i < x.length; i++)
			{
				newy[i] = Math.log(y[i]);
			}

			LinearRegressionModel m = new LinearRegressionModel(x, newy);
			m.compute();

			double[] linear = m.getCoefficients();

			a = Math.exp(linear[0]);

			b = linear[1];
		}
		else
		{
			throw new ArrayIndexOutOfBoundsException(
					"Must have same number of values in each array");
		}
	}

	@Override
	public double evaluateAt(double x)
	{
		return a * Math.exp(b * x);
	}

	@Override
	public double getCoefficientOfDetermination(BiasType biasType)
	{
		if (biasType == BiasType.Biased)
		{
			LinearRegressionModel m = new LinearRegressionModel(x, newy);
			m.compute();

			return m.getCoefficientOfDetermination();
		}
		else
		{
			return super.getCoefficientOfDetermination();
		}
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
		if (biasType == BiasType.Biased)
		{
			LinearRegressionModel m = new LinearRegressionModel(x, newy);
			m.compute();

			return Math.sqrt(m.getCoefficientOfDetermination());
		}
		else
		{
			return super.getCorrelationCoefficent();
		}
	}

	@Override
	public String toString()
	{
		return String.format("%f * e ^ %f * x", a, b);
	}
}
