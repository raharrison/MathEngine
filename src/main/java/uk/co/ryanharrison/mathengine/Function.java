package uk.co.ryanharrison.mathengine;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.Evaluator;
import uk.co.ryanharrison.mathengine.parser.nodes.Node;

/**
 * Class representing a function of one variable that can be evaluated
 * 
 * @author Ryan Harrison
 * 
 */
public final class Function
{
	/** The equation that this function represents */
	private String equation;

	/** The angle unit of this function */
	private AngleUnit angleUnit;

	/** The variable of this function */
	private String variable;

	/**
	 * An Evaluator instance used when evaluating the function This gets lazily
	 * initialised when needed
	 * */
	private Evaluator evaluator;

	/**
	 * Construct a new function with the specified equation
	 * 
	 * @param equation
	 *            The equation
	 */
	public Function(String equation)
	{
		this(equation, "x", AngleUnit.Radians);
	}

	/**
	 * Construct a new function with the specified equation and angle unit
	 * 
	 * @param equation
	 *            The equation
	 * @param angleUnit
	 *            The angle unit
	 */
	public Function(String equation, AngleUnit angleUnit)
	{
		this(equation, "x", angleUnit);
	}

	/**
	 * COnstruct a new function with specified equation and variable
	 * 
	 * @param equation
	 *            The equation
	 * @param variable
	 *            The variable
	 */
	public Function(String equation, String variable)
	{
		this(equation, variable, AngleUnit.Radians);
	}

	/**
	 * Construct a new function with specified equation, variable and angle unit
	 * 
	 * @param equation
	 *            The equation
	 * @param variable
	 *            The variable
	 * @param angleUnit
	 *            The angle unit
	 */
	public Function(String equation, String variable, AngleUnit angleUnit)
	{
		this.equation = equation;
		this.variable = variable;
		this.angleUnit = angleUnit;
		this.evaluator = null;
	}

	/**
	 * Initialise the evaluator with the current equation, variable and
	 * evaluator. This method compiles the expression tree of the function in
	 * the Evaluator
	 */
	private void initEvaluator()
	{
		this.evaluator = Evaluator.newSimpleEvaluator();

		this.evaluator.addVariable(this.variable, "0");
		this.evaluator.compileTree(this.equation);
		this.evaluator.setAngleUnit(this.angleUnit);
	}

	/**
	 * Evaluate the function at a specified point
	 * 
	 * The variable of this function will become the value doing evaluation
	 * 
	 * @param at
	 *            The point to evaluate at
	 * @return The function evaluated at the specified point
	 */
	public double evaluateAt(double at)
	{
		return evaluateAt(Double.toString(at));
	}

	/**
	 * Evaluate the function at a specified point
	 * 
	 * The variable of this function will become the value doing evaluation
	 * 
	 * @param at
	 *            The point to evaluate at. This itself can be an expression
	 * @return The function evaluated at the specified point
	 */
	public double evaluateAt(String at)
	{
		// initialise the evaluator if not already done
		if (this.evaluator == null)
			initEvaluator();

		// add the variable and evaluate the cached tree to a double
		this.evaluator.addVariable(variable, at);
		return this.evaluator.evaluateCachedTreeDouble();
	}

	/**
	 * Get the angle unit of this function
	 * 
	 * @return This functions angle unit
	 */
	public AngleUnit getAngleUnit()
	{
		return this.angleUnit;
	}

	/**
	 * Get the equation of this function
	 * 
	 * @return This functions equation
	 */
	public String getEquation()
	{
		return this.equation;
	}

	/**
	 * Get the variable of this function
	 * 
	 * @return This functions variable
	 */
	public String getVariable()
	{
		return this.variable;
	}

	/**
	 * Get this function compiled into a Node expression tree
	 * 
	 * @return An expression tree of nodes representing this function
	 */
	public Node getCompiledExpression()
	{
		// lazy initialisation of the evaluator
		if (this.evaluator == null)
			initEvaluator();

		return evaluator.getCachedTree();
	}

	/**
	 * Set this functions angle unit
	 * 
	 * @param angleUnit
	 *            The new angle unit
	 */
	public void setAngleUnit(AngleUnit angleUnit)
	{
		this.angleUnit = angleUnit;
	}

	/**
	 * Convert this function into a String representation of the form
	 * f(variable) = equation
	 */
	@Override
	public String toString()
	{
		return String.format("f(%s) = %s", this.variable, this.equation);
	}
}
