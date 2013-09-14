package uk.co.ryanharrison.mathengine.regression;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Base class for all RegressionModel tests. Contains methods and variables for
 * use in all regression model tests
 */
public abstract class RegressionModelTest
{
	/** Sample X coordinates values for tests */
	protected static final double[] xValues = { 2, 3, 4, 5, 6, 8, 10, 11 };

	/** Sample Y coordinates values for tests */
	protected static final double[] yValues = { 21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12,
			55.98 };
	
	/**
	 * The RegressionModel for use in tests. This is initiated before the tests are executed
	 */
	protected RegressionModel model = null;
	
	/**
	 * Set up the RegressionModel and compute the coefficients
	 */
	@BeforeClass
	protected abstract void setUpRegressionModel();

	/**
	 * Test that regression model calculates the correct coefficients when given
	 * coordinate values
	 */
	@Test
	public abstract void testRegressionModelCoefficients();

	/**
	 * Test that regression model can be evaluated at a point after computation
	 */
	@Test
	public abstract void testRegressionModelEvaluation();
}
