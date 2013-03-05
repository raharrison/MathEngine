package uk.co.raharrison.mathengine.regression;

public class LogarithmicRegressionModel extends LinearRegressionModel
{
	private double[] newx;

	public LogarithmicRegressionModel(double[] x, double[] y)
	{
		super(x, y);
		newx = null;
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
			newx = new double[x.length];

			for (int i = 0; i < x.length; i++)
			{
				newx[i] = Math.log(x[i]);
			}

			LinearRegressionModel m = new LinearRegressionModel(newx, y);
			m.compute();

			double[] linear = m.getCoefficients();

			this.a = linear[0];
			this.b = linear[1];
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
		return a + b * Math.log(x);
	}

	@Override
	public double getCoefficientOfDetermination(BiasType biasType)
	{
		if (biasType == BiasType.Biased)
		{
			LinearRegressionModel m = new LinearRegressionModel(newx, y);
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
			LinearRegressionModel m = new LinearRegressionModel(newx, y);
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
		return String.format("%f + %f * ln(x)", a, b);
	}
}
