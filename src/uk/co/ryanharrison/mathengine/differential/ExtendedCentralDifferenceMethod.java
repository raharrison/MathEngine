package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

/**
 * {@link NumericalDifferentiationMethod} that uses the extended central
 * difference formulas to estimate the derivatives of a target function
 * 
 * @author Ryan Harrison
 * 
 */
public class ExtendedCentralDifferenceMethod extends NumericalDifferentiationMethod
{
	/**
	 * Create a new {@link ExtendedCentralDifferenceMethod} instance with the
	 * specified target function
	 * 
	 * @param function
	 *            The target function to numerically estimate the derivatives of
	 */
	public ExtendedCentralDifferenceMethod(Function function)
	{
		super(function);
	}

	/**
	 * Create a new {@link ExtendedCentralDifferenceMethod} instance with the
	 * specified target function and change in x
	 * 
	 * @param function
	 *            The target function to numerically estimate the derivatives of
	 * @param h
	 *            The change in the value of x to use when numerically
	 *            estimating the derivative using finite difference
	 *            approximations. This value should be sufficiently small to
	 *            increase accuracy but large enough to prevent rounding errors
	 */
	public ExtendedCentralDifferenceMethod(Function function, double h)
	{
		super(function, h);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double deriveFirst()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (f.evaluateAt(x - 2 * h) - 8 * f.evaluateAt(x - h) + 8 * f.evaluateAt(x + h) - f
				.evaluateAt(x + 2 * h)) / 12 / h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double deriveFourth()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluateAt(x - 3 * h) + 12 * f.evaluateAt(x - 2 * h) - 39 * f.evaluateAt(x - h)
				+ 56 * f.evaluateAt(x) - 39 * f.evaluateAt(x + h) + 12 * f.evaluateAt(x + 2 * h) - f
					.evaluateAt(x + 3 * h)) / 6 / h / h / h / h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double deriveSecond()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (-f.evaluateAt(x - 2 * h) + 16 * f.evaluateAt(x - h) - 30 * f.evaluateAt(x) + 16
				* f.evaluateAt(x + h) - f.evaluateAt(x + 2 * h))
				/ 12 / h / h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double deriveThird()
	{
		double x = targetPoint;
		double h = this.h;
		Function f = this.targetFunction;

		return (f.evaluateAt(x - 3 * h) - 8 * f.evaluateAt(x - 2 * h) + 13 * f.evaluateAt(x - h)
				- 13 * f.evaluateAt(x + h) + 8 * f.evaluateAt(x + 2 * h) - f.evaluateAt(x + 3 * h))
				/ 8 / h / h / h;
	}
}
