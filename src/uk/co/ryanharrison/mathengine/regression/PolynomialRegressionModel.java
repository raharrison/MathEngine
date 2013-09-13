package uk.co.ryanharrison.mathengine.regression;

import java.util.Arrays;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;
import uk.co.ryanharrison.mathengine.linearalgebra.QRDecomposition;

public class PolynomialRegressionModel extends RegressionModel
{
	private double[] coefficients;
	private int degree;

	public PolynomialRegressionModel(double[] x, double[] y, int degree)
	{
		super(x, y);
		coefficients = null;
		this.degree = degree;
	}

	@Override
	public void compute()
	{
		// build Vandermonde matrix
		if (x.length - 1 < degree | y.length - 1 < degree)
		{
			throw new IllegalArgumentException("Must have at least " + (degree + 1) + " values");
		}

		if (x.length == y.length)
		{
			double[][] vandermonde = new double[x.length][degree + 1];
			for (int i = 0; i < x.length; i++)
			{
				for (int j = 0; j <= degree; j++)
				{
					vandermonde[i][j] = Math.pow(x[i], j);
				}
			}

			Matrix X = new Matrix(vandermonde);

			// create matrix from vector
			Matrix Y = new Matrix(y, x.length);

			// find least squares solution
			QRDecomposition qr = new QRDecomposition(X);
			Matrix beta = qr.solve(Y);
			coefficients = beta.getColumnPackedCopy();
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
		double result = 0;

		for (int i = 0; i < coefficients.length; i++)
		{
			result += coefficients[i] * Math.pow(x, i);
		}

		return result;
	}

	@Override
	public double getCoefficientOfDetermination(BiasType biasType)
	{
		return super.getCoefficientOfDetermination();
	}

	@Override
	public double[] getCoefficients()
	{
		return coefficients;
	}

	@Override
	public double getCorrelationCoefficient(BiasType biasType)
	{
		return super.getCorrelationCoefficent();
	}

	public double[] getPolynomialCoefficients()
	{
		return coefficients;
	}

	@Override
	public String toString()
	{
		return Arrays.toString(coefficients);
	}
}
