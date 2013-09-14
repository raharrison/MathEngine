package uk.co.ryanharrison.mathengine.regression;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test the LinearRegressionModel class
 */
public class LinearRegressionModelTest extends RegressionModelTest
{
	/**
	 * Test that regression model calculates the correct coefficients when given
	 * coordinate values
	 */
	@Override
	@Test
	public void testRegressionModelCoefficients()
	{
		double[] coefficients = model.getCoefficients();
		double expectedA = 9.476277128547583;
		double expectedB = 4.193873121869783;
		assertEquals(expectedA, coefficients[0], 0.0);
		assertEquals(expectedB, coefficients[1], 0.0);
	}

	/**
	 * Test that regression model can be evaluated at a point after computation
	 */
	@Override
	@Test
	public void testRegressionModelEvaluation()
	{
		double point = 12.74;
		double expected = 62.90622070116862;
		double actual = model.evaluateAt(point);
		assertEquals(expected, actual, 0.0);
	}

	/**
	 * Initialise the LinearRegressionModel and compute the coefficients
	 */
	@Override
	@Before
	public void setUpRegressionModel()
	{
		model = new LinearRegressionModel(xValues, yValues);
		model.compute();
	}
}
