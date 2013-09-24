package uk.co.ryanharrison.mathengine.differential;

import uk.co.ryanharrison.mathengine.Function;

/**
 * {@link NumericalDifferentiationMethod} which uses the method of divided
 * differences to estimate the derivatives.
 * 
 * @author Ryan Harrison
 * 
 */
public class DividedDifferenceMethod extends NumericalDifferentiationMethod
{
	/** The direction of the differences to use */
	private DifferencesDirection direction;

	/**
	 * Construct a new {@link DividedDifferenceMethod} instance with the
	 * specified target {@link Function}
	 * 
	 * This constructor will default the {@link DifferencesDirection} to Central
	 * 
	 * @param function
	 *            The target function to numerically estimate the derivatives of
	 */
	public DividedDifferenceMethod(Function function)
	{
		this(function, DifferencesDirection.Central);
	}

	/**
	 * Construct a new {@link DividedDifferenceMethod} instance with the
	 * specified target {@link Function} and differences direction
	 * 
	 * This constructor will default the {@link DifferencesDirection} to Central
	 * 
	 * @param function
	 *            The target function to numerically estimate the derivatives of
	 * @param direction
	 *            The direction of the differences to use
	 */
	public DividedDifferenceMethod(Function function, DifferencesDirection direction)
	{
		super(function);
		this.direction = direction;
	}

	/**
	 * Construct a new {@link DividedDifferenceMethod} instance with the
	 * specified target {@link Function}, difference direction and change in x
	 * 
	 * This constructor will default the {@link DifferencesDirection} to Central
	 * 
	 * @param function
	 *            The target function to numerically estimate the derivatives of
	 * @param direction
	 *            The direction of the differences to use
	 * @param h
	 *            The change in the value of x to use when numerically
	 *            estimating the derivative using finite difference
	 *            approximations. This value should be sufficiently small to
	 *            increase accuracy but large enough to prevent rounding errors
	 */
	public DividedDifferenceMethod(Function function, DifferencesDirection direction, double h)
	{
		super(function, h);
		this.direction = direction;
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

		switch (this.direction)
		{
			case Forward:
				return (-3 * f.evaluateAt(x) + 4 * f.evaluateAt(x + h) - f.evaluateAt(x + 2 * h))
						/ 2 / h;
			case Central:
				return (f.evaluateAt(x + h) - f.evaluateAt(x - h)) / 2 / h;
			case Backward:
				return (3 * f.evaluateAt(x) - 4 * f.evaluateAt(x - h) + f.evaluateAt(x - 2 * h))
						/ 2 / h;
		}

		return x;
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

		switch (this.direction)
		{
			case Forward:
				return (3 * f.evaluateAt(x) - 14 * f.evaluateAt(x + h) + 26
						* f.evaluateAt(x + 2 * h) - 24 * f.evaluateAt(x + 3 * h) + 11
						* f.evaluateAt(x + 4 * h) - 2 * f.evaluateAt(x + 5 * h))
						/ h / h / h / h;
			case Central:
				return (f.evaluateAt(x - 2 * h) - 4 * f.evaluateAt(x - h) + 6 * f.evaluateAt(x) - 4
						* f.evaluateAt(x + h) + f.evaluateAt(x + 2 * h))
						/ h / h / h / h;
			case Backward:
				return (3 * f.evaluateAt(x) - 14 * f.evaluateAt(x - h) + 26
						* f.evaluateAt(x - 2 * h) - 24 * f.evaluateAt(x - 3 * h) + 11
						* f.evaluateAt(x - 4 * h) - 2 * f.evaluateAt(x - 5 * h))
						/ h / h / h / h;
		}

		return x;
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

		switch (this.direction)
		{
			case Forward:
				return (2 * f.evaluateAt(x) - 5 * f.evaluateAt(x + h) + 4 * f.evaluateAt(x + 2 * h) - f
						.evaluateAt(x + 3 * h)) / h / h;
			case Central:
				return (f.evaluateAt(x - h) - 2 * f.evaluateAt(x) + f.evaluateAt(x + h)) / h / h;
			case Backward:
				return (2 * f.evaluateAt(x) - 5 * f.evaluateAt(x - h) + 4 * f.evaluateAt(x - 2 * h) - f
						.evaluateAt(x - 3 * h)) / h / h;
		}

		return x;
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

		switch (this.direction)
		{
			case Forward:
				return (-5 * f.evaluateAt(x) + 18 * f.evaluateAt(x + h) - 24
						* f.evaluateAt(x + 2 * h) + 14 * f.evaluateAt(x + 3 * h) - 3 * f
						.evaluateAt(x + 4 * h)) / 2 / h / h / h;
			case Central:
				return (-f.evaluateAt(x - 2 * h) + 2 * f.evaluateAt(x - h) - 2
						* f.evaluateAt(x + h) + f.evaluateAt(x + 2 * h))
						/ 2 / h / h / h;
			case Backward:
				return (5 * f.evaluateAt(x) - 18 * f.evaluateAt(x - h) + 24
						* f.evaluateAt(x - 2 * h) - 14 * f.evaluateAt(x - 3 * h) + 3 * f
						.evaluateAt(x - 4 * h)) / 2 / h / h / h;
		}

		return x;
	}
}
