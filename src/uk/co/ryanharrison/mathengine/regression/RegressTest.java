package uk.co.ryanharrison.mathengine.regression;

public class RegressTest
{
	public static void main(String[] args)
	{
		double[] x = { 2, 3, 4, 5, 6, 8, 10, 11.53542 };

		double[] y = { 21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98 };

		RegressionModel[] models = { new LinearRegressionModel(x, y), };

		for (RegressionModel m : models)
		{
			m.compute();

			System.out.println(m.toString());
		}
	}
}